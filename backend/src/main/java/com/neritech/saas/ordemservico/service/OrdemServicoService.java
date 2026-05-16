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
    private final com.neritech.saas.cliente.repository.ClienteRepository clienteRepository;
    private final com.neritech.saas.veiculo.repository.VeiculoRepository veiculoRepository;
    private final com.neritech.saas.comunicacao.service.NotificacaoClienteService notificacaoClienteService;

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
        } else {
            // Se for SERVICO (ou qualquer outra coisa), traz todos os tipos EXCETO Orçamento.
            tipos = java.util.Arrays.stream(com.neritech.saas.ordemservico.domain.enums.TipoOS.values())
                    .filter(t -> t != com.neritech.saas.ordemservico.domain.enums.TipoOS.ORCAMENTO)
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

        // 2. Lançamento Financeiro (Contas a Receber)
        contasReceberService.create(os.getEmpresaId(), new com.neritech.saas.financeiro.dto.ContasReceberRequest(
                "Recebimento OS " + os.getNumeroOS(),
                os.getClienteId(),
                null, // faturaId
                java.time.LocalDate.now(), // dataEmissao
                java.time.LocalDate.now().plusDays(30), // dataVencimento
                null, // dataRecebimento
                os.getValorTotal(), // valorOriginal
                java.math.BigDecimal.ZERO, // valorRecebido
                java.math.BigDecimal.ZERO, // valorJuros
                java.math.BigDecimal.ZERO, // valorMulta
                os.getValorDesconto(), // valorDesconto
                os.getValorTotal(), // saldoDevedor
                com.neritech.saas.financeiro.domain.enums.StatusTitulo.ABERTO,
                com.neritech.saas.financeiro.domain.enums.TipoTitulo.OS,
                os.getFormaPagamentoId(),
                null, // contaBancariaId
                null, // centroCustoId
                null, // planoContasId
                os.getNumeroOS(), // numeroTitulo
                "Gerado automaticamente pela Ordem de Serviço " + os.getNumeroOS() // observacoes
        ));
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
                response.observacoesCliente(), response.valorServicos(), response.valorProdutos(),
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
