package com.neritech.saas.ordemservico.service;

import com.neritech.saas.ordemservico.domain.FotoOS;
import com.neritech.saas.ordemservico.domain.ItemOSProduto;
import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.dto.*;
import com.neritech.saas.ordemservico.mapper.*;
import com.neritech.saas.ordemservico.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdemServicoService {

    private final OrdemServicoRepository repository;
    private final StatusOSRepository statusOSRepository;
    private final ItemOSServicoRepository itemOSServicoRepository;
    private final ItemOSProdutoRepository itemOSProdutoRepository;
    private final FotoOSRepository fotoOSRepository;
    private final OrdemServicoMapper mapper;
    private final ItemOSServicoMapper servicoMapper;
    private final ItemOSProdutoMapper produtoMapper;
    private final com.neritech.saas.estoque.service.MovimentacaoEstoqueService movimentacaoEstoqueService;
    private final com.neritech.saas.financeiro.service.ContasReceberService contasReceberService;
    private final com.neritech.saas.financeiro.repository.ContasReceberRepository contasReceberRepository;
    private final com.neritech.saas.financeiro.service.FaturaService faturaService;
    private final com.neritech.saas.cliente.repository.ClienteRepository clienteRepository;
    private final com.neritech.saas.veiculo.repository.VeiculoRepository veiculoRepository;
    private final com.neritech.saas.comunicacao.service.NotificacaoClienteService notificacaoClienteService;
    private final com.neritech.saas.comunicacao.service.EmailSenderService emailSenderService;

    public OrdemServicoResponse create(OrdemServicoRequest request) {
        if (repository.existsByEmpresaIdAndNumeroOS(request.empresaId(), request.numeroOS())) {
            throw new IllegalArgumentException("Número de OS já existe para esta empresa");
        }

        OrdemServico entity = mapper.toEntity(request);

        if (request.statusId() != null) {
            entity.setStatus(statusOSRepository.findById(request.statusId())
                    .orElseThrow(() -> new EntityNotFoundException("Status não encontrado")));
        }

        OrdemServico saved = repository.save(entity);
        return enrichResponse(mapper.toResponse(saved));
    }

    public OrdemServicoResponse criarVendaBalcao(VendaBalcaoRequest request) {
        // 1. Criar OS
        OrdemServicoResponse osResponse = create(request.ordemServico());
        
        OrdemServico entity = repository.findById(osResponse.id())
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Serviço não encontrada"));

        // 2. Adicionar itens
        if (request.produtos() != null && !request.produtos().isEmpty()) {
            for (ItemOSProdutoRequest itemReq : request.produtos()) {
                ItemOSProduto itemEntity = produtoMapper.toEntity(itemReq);
                itemEntity.setOrdemServico(entity);
                itemOSProdutoRepository.save(itemEntity);
            }
        }
        
        // 3. Finalizar OS (baixa de estoque e lançamento financeiro)
        processarFinalizacaoOS(entity);
        
        return findById(osResponse.id());
    }

    @Transactional(readOnly = true)
    public OrdemServicoResponse findById(Long id) {
        OrdemServico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Serviço não encontrada"));
        return enrichResponse(mapper.toResponse(entity));
    }

    @Transactional(readOnly = true)
    public Page<OrdemServicoResponse> findByEmpresaId(Long empresaId, String tipoStr, String search, Pageable pageable) {
        java.util.List<com.neritech.saas.ordemservico.domain.enums.TipoOS> tipos;
        if ("ORCAMENTO".equalsIgnoreCase(tipoStr)) {
            tipos = java.util.List.of(com.neritech.saas.ordemservico.domain.enums.TipoOS.ORCAMENTO);
        } else if ("VENDA_PRODUTO".equalsIgnoreCase(tipoStr)) {
            tipos = java.util.List.of(com.neritech.saas.ordemservico.domain.enums.TipoOS.VENDA_PRODUTO);
        } else {
            // Se for SERVICO, traz todos os tipos EXCETO Orçamento e VENDA_PRODUTO (PDV)
            tipos = java.util.Arrays.stream(com.neritech.saas.ordemservico.domain.enums.TipoOS.values())
                    .filter(t -> t != com.neritech.saas.ordemservico.domain.enums.TipoOS.ORCAMENTO &&
                                 t != com.neritech.saas.ordemservico.domain.enums.TipoOS.VENDA_PRODUTO)
                    .toList();
        }

        if (search != null && !search.trim().isEmpty()) {
            return repository.search(empresaId, tipos, search, pageable)
                    .map(mapper::toResponse);
        }
        return repository.findByEmpresaIdAndTiposIn(empresaId, tipos, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<OrdemServicoResponse> findByEmpresaIdAndStatusId(Long empresaId, Long statusId, Pageable pageable) {
        return repository.findByEmpresaIdAndStatusId(empresaId, statusId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<OrdemServicoResponse> findByClienteId(Long clienteId, Pageable pageable) {
        return repository.findByClienteId(clienteId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public java.util.Optional<OrdemServicoResponse> findByCpfAndNumeroOS(String cpf, String numeroOS) {
        return repository.findByCpfAndNumeroOS(cpf, numeroOS)
                .map(entity -> enrichResponse(mapper.toResponse(entity)));
    }

    public OrdemServicoResponse update(Long id, OrdemServicoRequest request) {
        OrdemServico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Serviço não encontrada"));

        Long oldStatusId = entity.getStatus() != null ? entity.getStatus().getId() : null;
        mapper.updateEntityFromRequest(request, entity);

        if (request.statusId() != null) {
            com.neritech.saas.ordemservico.domain.StatusOS newStatus = statusOSRepository.findById(request.statusId())
                    .orElseThrow(() -> new EntityNotFoundException("Status não encontrado"));
            
            // Verifica se está finalizando agora
            if (newStatus.getFinalizaOS() && (oldStatusId == null || !oldStatusId.equals(newStatus.getId()))) {
                processarFinalizacaoOS(entity);
            }
            
            // Verifica se deve notificar o cliente
            if (newStatus.getNotificaCliente() && (oldStatusId == null || !oldStatusId.equals(newStatus.getId()))) {
                notificacaoClienteService.enviarNotificacaoOS(entity, newStatus.getTemplateNotificacaoId());
            }

            entity.setStatus(newStatus);
        }

        OrdemServico updated = repository.save(entity);
        return enrichResponse(mapper.toResponse(updated));
    }

    private void processarFinalizacaoOS(OrdemServico os) {
        // 1. Baixa de Estoque
        List<ItemOSProduto> produtos = itemOSProdutoRepository.findByOrdemServicoId(os.getId());
        for (ItemOSProduto item : produtos) {
            if (item.getProdutoId() != null && item.getQuantidade() != null) {
                movimentacaoEstoqueService.create(new com.neritech.saas.estoque.dto.MovimentacaoEstoqueRequest(
                        os.getEmpresaId(),
                        item.getProdutoId(),
                        com.neritech.saas.estoque.domain.enums.TipoMovimentacao.SAIDA,
                        null, // subtipoMovimentacao
                        item.getQuantidade(),
                        item.getValorUnitario(),
                        item.getLocalizacaoEstoqueId(),
                        null, // localizacaoDestinoId
                        "ORDEM_SERVICO", // documentoTipo
                        os.getNumeroOS(), // documentoNumero
                        os.getId(), // documentoId
                        null, // fornecedorId
                        null, // loteNumero
                        null, // dataValidade
                        null, // dataFabricacao
                        "Saída automática por finalização da OS " + os.getNumeroOS(), // motivo
                        null, // observacoes
                        os.getMecanicoResponsavelId() != null ? os.getMecanicoResponsavelId() : os.getCriadoPor(), // usuarioResponsavel
                        os.getId() // ordemServicoId
                ));
            }
        }

        boolean isVendaBalcao = os.getTipoOS() == com.neritech.saas.ordemservico.domain.enums.TipoOS.VENDA_PRODUTO;
        var statusTitulo = isVendaBalcao ? com.neritech.saas.financeiro.domain.enums.StatusTitulo.PAGO : com.neritech.saas.financeiro.domain.enums.StatusTitulo.ABERTO;
        var valorRecebido = isVendaBalcao ? os.getValorTotal() : java.math.BigDecimal.ZERO;
        var saldoDevedor = isVendaBalcao ? java.math.BigDecimal.ZERO : os.getValorTotal();
        var dataRecebimento = isVendaBalcao ? java.time.LocalDate.now() : null;

        // 2. Lançamento Financeiro (Contas a Receber)
        java.util.Optional<com.neritech.saas.financeiro.domain.ContasReceber> crOpt = contasReceberRepository.findByEmpresaIdAndNumeroTitulo(os.getEmpresaId(), os.getNumeroOS());
        if (crOpt.isPresent()) {
            com.neritech.saas.financeiro.domain.ContasReceber cr = crOpt.get();
            cr.setValorNominal(os.getValorTotal() != null ? os.getValorTotal() : java.math.BigDecimal.ZERO);
            java.math.BigDecimal valorPago = cr.getValorPago() != null ? cr.getValorPago() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal pendente = cr.getValorNominal().subtract(valorPago);
            cr.setValorPendente(pendente);
            if (pendente.compareTo(java.math.BigDecimal.ZERO) <= 0) {
                cr.setStatus(com.neritech.saas.financeiro.domain.enums.StatusTitulo.PAGO);
                cr.setValorPendente(java.math.BigDecimal.ZERO);
            } else if (valorPago.compareTo(java.math.BigDecimal.ZERO) > 0) {
                cr.setStatus(com.neritech.saas.financeiro.domain.enums.StatusTitulo.PARCIALMENTE_PAGO);
            } else {
                cr.setStatus(com.neritech.saas.financeiro.domain.enums.StatusTitulo.ABERTO);
            }
            contasReceberRepository.save(cr);
        } else if (isVendaBalcao) {
            contasReceberService.create(os.getEmpresaId(), new com.neritech.saas.financeiro.dto.ContasReceberRequest(
                    "Venda Balcão " + os.getNumeroOS(),
                    os.getClienteId(),
                    null, // faturaId
                    java.time.LocalDate.now(), // dataEmissao
                    java.time.LocalDate.now(), // dataVencimento
                    dataRecebimento, // dataRecebimento
                    os.getValorTotal(), // valorOriginal
                    valorRecebido, // valorRecebido
                    java.math.BigDecimal.ZERO, // valorJuros
                    java.math.BigDecimal.ZERO, // valorMulta
                    os.getValorDesconto(), // valorDesconto
                    saldoDevedor, // saldoDevedor
                    statusTitulo,
                    com.neritech.saas.financeiro.domain.enums.TipoTitulo.VENDA_BALCAO,
                    os.getFormaPagamentoId(),
                    null, // contaBancariaId
                    null, // centroCustoId
                    null, // planoContasId
                    os.getNumeroOS(), // numeroTitulo
                    "Gerado automaticamente pela Venda Balcão " + os.getNumeroOS() // observacoes
            ));
        }

        // 3. Fatura (Para visualização de pagamentos no front-end)
        if (!isVendaBalcao) {
            faturaService.create(os.getEmpresaId(), new com.neritech.saas.financeiro.dto.FaturaRequest(
                    os.getNumeroOS(), // numeroFatura
                    os.getClienteId(),
                    os.getId(), // ordemServicoId
                    com.neritech.saas.financeiro.domain.enums.TipoFatura.SERVICO,
                    java.time.LocalDate.now(), // dataEmissao
                    java.time.LocalDate.now().plusDays(30), // dataVencimento
                    os.getValorDesconto(), // valorDescontos
                    java.math.BigDecimal.ZERO, // valorAcrescimos
                    com.neritech.saas.financeiro.domain.enums.StatusFatura.ABERTA, // status
                    os.getFormaPagamentoId(), // formaPagamentoId
                    null, // condicaoPagamentoId
                    "Fatura gerada automaticamente pela Ordem de Serviço " + os.getNumeroOS(), // observacoes
                    null, // observacoesInternas
                    os.getValorServicos() != null ? os.getValorServicos() : java.math.BigDecimal.ZERO, // valorServicos
                    os.getValorProdutos() != null ? os.getValorProdutos() : java.math.BigDecimal.ZERO, // valorProdutos
                    os.getValorTotal() != null ? os.getValorTotal() : java.math.BigDecimal.ZERO, // valorTotal
                    new java.util.ArrayList<>() // itens
            ));
        }
    }

    public void enviarOrcamentoEmail(Long id, String emailDestino) {
        OrdemServico os = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Ordem de Serviço não encontrada"));

        // Buscar dados do cliente
        com.neritech.saas.cliente.domain.Cliente cliente = null;
        if (os.getClienteId() != null) {
            cliente = clienteRepository.findById(os.getClienteId()).orElse(null);
        }

        // Se não foi passado um e-mail, tenta usar o do cliente
        String destinatario = (emailDestino != null && !emailDestino.isBlank())
                ? emailDestino
                : (cliente != null ? cliente.getEmail() : null);

        if (destinatario == null || destinatario.isBlank()) {
            throw new IllegalArgumentException("E-mail do destinatário não informado e cliente não possui e-mail cadastrado.");
        }

        String nomeCliente = cliente != null
                ? (cliente.getNomeCompleto() != null ? cliente.getNomeCompleto() : cliente.getRazaoSocial())
                : "Cliente";

        // Buscar itens
        List<ItemOSServico> servicos = itemOSServicoRepository.findByOrdemServicoId(id);
        List<ItemOSProduto> produtos = itemOSProdutoRepository.findByOrdemServicoId(id);

        // Montar HTML do e-mail
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang='pt-BR'><head><meta charset='UTF-8'/><style>");
        html.append("body{font-family:Arial,sans-serif;background:#f4f6fa;margin:0;padding:20px;}");
        html.append(".container{max-width:640px;margin:0 auto;background:#fff;border-radius:12px;overflow:hidden;box-shadow:0 4px 24px rgba(0,0,0,.08);}");
        html.append(".header{background:linear-gradient(135deg,#1e3a8a,#3b82f6);padding:32px 24px;text-align:center;color:#fff;}");
        html.append(".header h1{margin:0;font-size:22px;letter-spacing:.5px;}");
        html.append(".header p{margin:6px 0 0;opacity:.85;font-size:14px;}");
        html.append(".badge{display:inline-block;background:rgba(255,255,255,.2);border-radius:20px;padding:4px 14px;font-size:13px;margin-top:10px;}");
        html.append(".content{padding:28px 24px;}");
        html.append(".section-title{font-size:11px;font-weight:700;text-transform:uppercase;letter-spacing:.8px;color:#6b7280;border-bottom:2px solid #f1f5f9;padding-bottom:8px;margin-bottom:14px;}");
        html.append(".info-grid{display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-bottom:20px;}");
        html.append(".info-item label{font-size:11px;color:#9ca3af;display:block;margin-bottom:2px;}");
        html.append(".info-item span{font-size:14px;font-weight:600;color:#111827;}");
        html.append("table{width:100%;border-collapse:collapse;margin-bottom:16px;font-size:13px;}");
        html.append("th{background:#f8fafc;color:#6b7280;font-size:10px;text-transform:uppercase;padding:8px 10px;text-align:left;border-bottom:1px solid #e5e7eb;}");
        html.append("td{padding:9px 10px;border-bottom:1px solid #f1f5f9;color:#374151;}");
        html.append(".total-row{background:#eff6ff;}");
        html.append(".total-row td{font-weight:700;font-size:15px;color:#1d4ed8;}");
        html.append(".obs-box{background:#f8fafc;border-left:4px solid #3b82f6;padding:14px;border-radius:4px;font-size:13px;color:#374151;margin-bottom:20px;}");
        html.append(".footer{background:#f8fafc;padding:20px 24px;text-align:center;font-size:12px;color:#9ca3af;}");
        html.append("</style></head><body>");
        html.append("<div class='container'>");

        // Cabeçalho
        String tipo = os.getTipoOS() != null && os.getTipoOS().name().equals("ORCAMENTO") ? "Orçamento" : "Ordem de Serviço";
        html.append("<div class='header'>");
        html.append("<h1>").append(tipo).append("</h1>");
        html.append("<p>Prezado(a) <strong>").append(nomeCliente).append("</strong>, segue abaixo o detalhamento do seu ").append(tipo.toLowerCase()).append(".</p>");
        html.append("<div class='badge'>#").append(os.getNumeroOS()).append("</div>");
        html.append("</div>");

        // Corpo
        html.append("<div class='content'>");

        // Informações gerais
        html.append("<p class='section-title'>Informações Gerais</p>");
        html.append("<div class='info-grid'>");
        html.append("<div class='info-item'><label>Nº ").append(tipo).append("</label><span>#").append(os.getNumeroOS()).append("</span></div>");
        html.append("<div class='info-item'><label>Cliente</label><span>").append(nomeCliente).append("</span></div>");
        if (os.getDataAbertura() != null) {
            html.append("<div class='info-item'><label>Data de Abertura</label><span>")
                .append(os.getDataAbertura().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .append("</span></div>");
        }
        if (os.getDataPromessa() != null) {
            html.append("<div class='info-item'><label>Previsão de Entrega</label><span>")
                .append(os.getDataPromessa().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .append("</span></div>");
        }
        html.append("</div>");

        // Serviços
        if (!servicos.isEmpty()) {
            html.append("<p class='section-title'>Serviços</p>");
            html.append("<table><tr><th>Descrição</th><th>Qtd</th><th>Valor Unit.</th><th>Total</th></tr>");
            for (ItemOSServico s : servicos) {
                html.append("<tr><td>").append(s.getDescricao() != null ? s.getDescricao() : "Serviço").append("</td>")
                    .append("<td>").append(s.getQuantidade() != null ? s.getQuantidade() : 1).append("</td>")
                    .append("<td>R$ ").append(s.getValorUnitario() != null ? String.format("%.2f", s.getValorUnitario()).replace('.', ',') : "0,00").append("</td>")
                    .append("<td>R$ ").append(s.getValorTotal() != null ? String.format("%.2f", s.getValorTotal()).replace('.', ',') : "0,00").append("</td></tr>");
            }
            html.append("</table>");
        }

        // Produtos
        if (!produtos.isEmpty()) {
            html.append("<p class='section-title'>Peças e Produtos</p>");
            html.append("<table><tr><th>Descrição</th><th>Qtd</th><th>Valor Unit.</th><th>Total</th></tr>");
            for (ItemOSProduto p : produtos) {
                html.append("<tr><td>").append(p.getDescricao() != null ? p.getDescricao() : "Produto").append("</td>")
                    .append("<td>").append(p.getQuantidade() != null ? p.getQuantidade() : 1).append("</td>")
                    .append("<td>R$ ").append(p.getValorUnitario() != null ? String.format("%.2f", p.getValorUnitario()).replace('.', ',') : "0,00").append("</td>")
                    .append("<td>R$ ").append(p.getValorTotal() != null ? String.format("%.2f", p.getValorTotal()).replace('.', ',') : "0,00").append("</td></tr>");
            }
            html.append("</table>");
        }

        // Total
        html.append("<table><tr class='total-row'><td colspan='3'><strong>VALOR TOTAL</strong></td>")
            .append("<td><strong>R$ ").append(os.getValorTotal() != null ? String.format("%.2f", os.getValorTotal()).replace('.', ',') : "0,00").append("</strong></td></tr></table>");

        // Observações para o cliente
        if (os.getObservacoesCliente() != null && !os.getObservacoesCliente().isBlank()) {
            html.append("<p class='section-title'>Observações</p>");
            html.append("<div class='obs-box'>").append(os.getObservacoesCliente()).append("</div>");
        }

        html.append("</div>"); // content

        // Rodapé
        html.append("<div class='footer'>");
        html.append("<p>Para dúvidas ou aprovação, entre em contato conosco.</p>");
        html.append("<p>Este e-mail foi gerado automaticamente. Por favor, não responda diretamente.</p>");
        html.append("</div>");

        html.append("</div></body></html>");

        String assunto = "[NeriTech Auto] " + tipo + " #" + os.getNumeroOS() + " — " + nomeCliente;
        emailSenderService.sendEmail(destinatario, assunto, html.toString());
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Ordem de Serviço não encontrada");
        }
        repository.deleteById(id);
    }

    /**
     * Popula as coleções aninhadas (serviços, peças, fotos) no DTO de resposta.
     */
    private OrdemServicoResponse enrichResponse(OrdemServicoResponse response) {
        Long osId = response.id();

        // Buscar Serviços
        List<ItemOSServicoResponse> servicos = itemOSServicoRepository.findByOrdemServicoId(osId)
                .stream()
                .map(servicoMapper::toResponse)
                .collect(Collectors.toList());

        // Buscar Produtos (Peças)
        List<ItemOSProdutoResponse> produtos = itemOSProdutoRepository.findByOrdemServicoId(osId)
                .stream()
                .map(produtoMapper::toResponse)
                .collect(Collectors.toList());

        // Buscar Fotos
        List<FotoOSResponse> fotos = fotoOSRepository.findByOrdemServicoIdOrderByIdAsc(osId)
                .stream()
                .map(this::mapFotoToResponse)
                .collect(Collectors.toList());

        // Retornar nova instância do record com as listas populadas
        return new OrdemServicoResponse(
                response.id(), response.empresaId(), response.numeroOS(), response.clienteId(),
                response.veiculoId(), response.statusId(), response.tipoOS(), response.prioridade(),
                response.dataAbertura(), response.dataPromessa(), response.dataInicioExecucao(),
                response.dataFimExecucao(), response.dataEntrega(), response.quilometragemEntrada(),
                response.quilometragemSaida(), response.nivelCombustivelEntrada(), response.nivelCombustivelSaida(),
                response.consultorResponsavelId(), response.mecanicoResponsavelId(), response.equipeExecucao(),
                response.problemaRelatado(), response.solucaoAplicada(), response.observacoesInternas(),
                response.observacoesCliente(), response.comentarios(), response.valorServicos(), response.valorProdutos(),
                response.valorDesconto(), response.valorAcrescimo(), response.valorTotal(),
                response.formaPagamentoId(), response.condicaoPagamentoId(), response.valorEntrada(),
                response.valorFinanciado(), response.aprovadoCliente(), response.dataAprovacaoCliente(),
                response.metodoAprovacao(), response.garantiaDias(), response.dataVencimentoGarantia(),
                response.nfeEmitida(), response.numeroNFe(), response.notaAvaliacaoCliente(),
                response.tempoTotalExecucaoMinutos(), response.margemLucroRealizada(), response.dataCadastro(),
                response.dataAtualizacao(), response.versao(), response.nomeCliente(),
                response.placaVeiculo(), response.nomeVeiculo(), response.statusNome(), response.statusCor(),
                servicos, produtos, fotos
        );
    }

    private FotoOSResponse mapFotoToResponse(FotoOS entity) {
        return new FotoOSResponse(
                entity.getId(),
                entity.getEmpresaId(),
                entity.getOrdemServicoId(),
                entity.getArquivoUrl(),
                entity.getContentType(),
                entity.getTamanho(),
                entity.getDescricao(),
                entity.getDataCadastro(),
                entity.getDataAtualizacao()
        );
    }
}
