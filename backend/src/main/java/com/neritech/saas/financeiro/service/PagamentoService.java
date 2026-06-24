package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.*;
import com.neritech.saas.financeiro.dto.PagamentoRequest;
import com.neritech.saas.financeiro.dto.PagamentoResponse;
import com.neritech.saas.financeiro.mapper.PagamentoMapper;
import com.neritech.saas.financeiro.mapper.ParcelaPagamentoMapper;
import com.neritech.saas.financeiro.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository repository;
    private final ParcelaPagamentoRepository parcelaRepository;
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final CondicaoPagamentoRepository condicaoPagamentoRepository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final FaturaRepository faturaRepository;
    private final ContasReceberRepository contasReceberRepository;
    private final com.neritech.saas.ordemservico.repository.OrdemServicoRepository ordemServicoRepository;
    private final PagamentoMapper mapper;
    private final ParcelaPagamentoMapper parcelaMapper;
    private final ContasReceberService contasReceberService;

    @Transactional(readOnly = true)
    public Page<PagamentoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(this::toResponseWithParcelas);
    }

    @Transactional(readOnly = true)
    public Page<PagamentoResponse> findByFatura(Long empresaId, Long faturaId, Pageable pageable) {
        return repository.findByEmpresaIdAndFatura_Id(empresaId, faturaId, pageable)
                .map(this::toResponseWithParcelas);
    }

    @Transactional(readOnly = true)
    public PagamentoResponse findById(Long id, Long empresaId) {
        Pagamento pagamento = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento nÃ£o encontrado"));
        return toResponseWithParcelas(pagamento);
    }

    @Transactional(readOnly = true)
    public Page<PagamentoResponse> findByOsId(Long empresaId, Long osId, Pageable pageable) {
        return repository.findByEmpresaIdAndOsId(empresaId, osId, pageable)
                .map(this::toResponseWithParcelas);
    }

    @Transactional
    public PagamentoResponse create(Long empresaId, PagamentoRequest request) {
        Pagamento entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context
        entity.setProcessadoPor(1L);
        entity.setNumeroPagamento("PG-" + System.currentTimeMillis());
        if (request.valorTotal() != null) {
            entity.setValorPago(request.valorTotal());
        }

        // Set relationships
        if (request.faturaId() != null) {
            Fatura fatura = faturaRepository.findByIdAndEmpresaId(request.faturaId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Fatura não encontrada"));
            entity.setFatura(fatura);
            if (entity.getStatus() == com.neritech.saas.financeiro.domain.enums.StatusPagamento.CONFIRMADO) {
                fatura.setStatus(com.neritech.saas.financeiro.domain.enums.StatusFatura.PAGA);
                faturaRepository.save(fatura);
            }
        }

        if (request.formaPagamentoId() != null) {
            FormaPagamento forma = formaPagamentoRepository.findByIdAndEmpresaId(request.formaPagamentoId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento nÃ£o encontrada"));
            entity.setFormaPagamento(forma);
        }

        if (request.condicaoPagamentoId() != null) {
            CondicaoPagamento condicao = condicaoPagamentoRepository
                    .findByIdAndEmpresaId(request.condicaoPagamentoId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("CondiÃ§Ã£o de pagamento nÃ£o encontrada"));
            entity.setCondicaoPagamento(condicao);
        }

        if (request.contaBancariaId() != null) {
            ContaBancaria conta = contaBancariaRepository.findByIdAndEmpresaId(request.contaBancariaId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Conta bancÃ¡ria nÃ£o encontrada"));
            entity.setContaBancaria(conta);
        }

        if (request.osId() != null) {
            com.neritech.saas.ordemservico.domain.OrdemServico os = ordemServicoRepository.findById(request.osId())
                    .orElseThrow(() -> new EntityNotFoundException("Ordem de Serviço não encontrada"));

            if (os.getStatus() == null || (
                !Boolean.TRUE.equals(os.getStatus().getFinalizaOS()) &&
                !os.getStatus().getNome().equalsIgnoreCase("FINALIZADA") &&
                !os.getStatus().getNome().equalsIgnoreCase("ENTREGUE") &&
                !os.getStatus().getNome().equalsIgnoreCase("CONCLUIDA") &&
                !os.getStatus().getCodigo().equalsIgnoreCase("FINALIZADA") &&
                !os.getStatus().getCodigo().equalsIgnoreCase("ENTREGUE") &&
                !os.getStatus().getCodigo().equalsIgnoreCase("CONCLUIDA")
            )) {
                throw new IllegalArgumentException("Financeiro só pode ser gerado para OS com status FINALIZADA ou ENTREGUE.");
            }

            java.math.BigDecimal valorNominal = request.valorOriginal() != null ? request.valorOriginal() : 
                                                (request.valorTotal() != null ? request.valorTotal() : 
                                                (os.getValorTotal() != null ? os.getValorTotal() : java.math.BigDecimal.ZERO));

            java.util.Optional<com.neritech.saas.financeiro.domain.ContasReceber> optCr = contasReceberRepository.findByEmpresaIdAndNumeroTitulo(empresaId, os.getNumeroOS());
            com.neritech.saas.financeiro.domain.ContasReceber cr;
            if (optCr.isPresent()) {
                cr = optCr.get();
                cr.setValorNominal(valorNominal);
            } else {
                cr = new com.neritech.saas.financeiro.domain.ContasReceber();
                cr.setEmpresaId(empresaId);
                cr.setNumeroTitulo(os.getNumeroOS());
                cr.setDescricao("Recebimento OS " + os.getNumeroOS());
                cr.setClienteId(request.clienteId());
                cr.setDataEmissao(java.time.LocalDate.now());
                cr.setDataVencimento(java.time.LocalDate.now());
                cr.setValorNominal(valorNominal);
                cr.setValorPago(java.math.BigDecimal.ZERO);
                cr.setCriadoPor(1L);
                cr.setTipoTitulo(com.neritech.saas.financeiro.domain.enums.TipoTitulo.OS);
            }

            cr.setValorDesconto(request.valorDesconto() != null ? request.valorDesconto() : java.math.BigDecimal.ZERO);
            cr.setValorJuros(request.valorJuros() != null ? request.valorJuros() : java.math.BigDecimal.ZERO);
            cr.setValorMulta(request.valorMulta() != null ? request.valorMulta() : java.math.BigDecimal.ZERO);

            if (entity.getStatus() == com.neritech.saas.financeiro.domain.enums.StatusPagamento.CONFIRMADO || "CONFIRMADO".equalsIgnoreCase(String.valueOf(entity.getStatus()))) {
                cr.setStatus(com.neritech.saas.financeiro.domain.enums.StatusTitulo.PAGO);
                cr.setValorPago(request.valorTotal() != null ? request.valorTotal() : cr.getValorNominal());
                cr.setValorPendente(java.math.BigDecimal.ZERO);

                // Criar RecebimentoTitulo correspondente para manter integridade e o lançamento do caixa
                if (cr.getRecebimentos() == null) {
                    cr.setRecebimentos(new java.util.ArrayList<>());
                }
                cr.getRecebimentos().clear();
                com.neritech.saas.financeiro.domain.RecebimentoTitulo rec = new com.neritech.saas.financeiro.domain.RecebimentoTitulo();
                rec.setContaReceber(cr);
                rec.setDataRecebimento(request.dataPagamento() != null ? request.dataPagamento() : java.time.LocalDate.now());
                rec.setValorRecebido(cr.getValorPago());
                rec.setValorJuros(cr.getValorJuros());
                rec.setValorMulta(cr.getValorMulta());
                rec.setValorDesconto(cr.getValorDesconto());
                rec.setEmpresaId(empresaId);
                rec.setFormaPagamento(cr.getFormaPagamento());
                rec.setContaBancaria(cr.getContaBancaria());
                cr.getRecebimentos().add(rec);
            } else {
                cr.setStatus(com.neritech.saas.financeiro.domain.enums.StatusTitulo.ABERTO);
                cr.setValorPago(java.math.BigDecimal.ZERO);
                cr.setValorPendente(cr.getValorNominal());
                if (cr.getRecebimentos() != null) {
                    cr.getRecebimentos().clear();
                }
            }
            
            if (entity.getFormaPagamento() != null) {
                cr.setFormaPagamento(entity.getFormaPagamento());
            }
            if (entity.getContaBancaria() != null) {
                cr.setContaBancaria(entity.getContaBancaria());
            }

            com.neritech.saas.financeiro.domain.ContasReceber savedCr = contasReceberRepository.save(cr);
            contasReceberService.syncFluxoCaixa(savedCr);
        }

        Pagamento savedPagamento = repository.save(entity);

        if (request.parcelas() != null && !request.parcelas().isEmpty()) {
            List<ParcelaPagamento> parcelas = request.parcelas().stream()
                    .map(parcelaRequest -> {
                        ParcelaPagamento parcela = parcelaMapper.toEntity(parcelaRequest);
                        parcela.setPagamento(savedPagamento);
                        return parcela;
                    })
                    .collect(Collectors.toList());
            parcelaRepository.saveAll(parcelas);
        }

        return toResponseWithParcelas(savedPagamento);
    }

    @Transactional
    public PagamentoResponse update(Long id, Long empresaId, PagamentoRequest request) {
        Pagamento entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento nÃ£o encontrado"));

        mapper.updateEntityFromDTO(request, entity);

        // Update relationships
        if (request.faturaId() != null) {
            Fatura fatura = faturaRepository.findByIdAndEmpresaId(request.faturaId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Fatura não encontrada"));
            entity.setFatura(fatura);
            if (entity.getStatus() == com.neritech.saas.financeiro.domain.enums.StatusPagamento.CONFIRMADO) {
                fatura.setStatus(com.neritech.saas.financeiro.domain.enums.StatusFatura.PAGA);
                faturaRepository.save(fatura);
            }
        } else {
            entity.setFatura(null);
        }

        if (request.formaPagamentoId() != null) {
            FormaPagamento forma = formaPagamentoRepository.findByIdAndEmpresaId(request.formaPagamentoId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento nÃ£o encontrada"));
            entity.setFormaPagamento(forma);
        } else {
            entity.setFormaPagamento(null);
        }

        if (request.condicaoPagamentoId() != null) {
            CondicaoPagamento condicao = condicaoPagamentoRepository
                    .findByIdAndEmpresaId(request.condicaoPagamentoId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("CondiÃ§Ã£o de pagamento nÃ£o encontrada"));
            entity.setCondicaoPagamento(condicao);
        } else {
            entity.setCondicaoPagamento(null);
        }

        if (request.contaBancariaId() != null) {
            ContaBancaria conta = contaBancariaRepository.findByIdAndEmpresaId(request.contaBancariaId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Conta bancÃ¡ria nÃ£o encontrada"));
            entity.setContaBancaria(conta);
        } else {
            entity.setContaBancaria(null);
        }

        Pagamento savedPagamento = repository.save(entity);

        // Update parcelas logic (simplified: delete all and recreate)
        if (request.parcelas() != null) {
            List<ParcelaPagamento> existingParcelas = parcelaRepository.findByPagamentoId(id);
            parcelaRepository.deleteAll(existingParcelas);

            List<ParcelaPagamento> newParcelas = request.parcelas().stream()
                    .map(parcelaRequest -> {
                        ParcelaPagamento parcela = parcelaMapper.toEntity(parcelaRequest);
                        parcela.setPagamento(savedPagamento);
                        return parcela;
                    })
                    .collect(Collectors.toList());
            parcelaRepository.saveAll(newParcelas);
        }

        return toResponseWithParcelas(savedPagamento);
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        Pagamento entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento nÃ£o encontrado"));

        List<ParcelaPagamento> parcelas = parcelaRepository.findByPagamentoId(id);
        parcelaRepository.deleteAll(parcelas);

        repository.delete(entity);
    }

    private PagamentoResponse toResponseWithParcelas(Pagamento pagamento) {
        List<ParcelaPagamento> parcelas = parcelaRepository.findByPagamentoId(pagamento.getId());

        Long formaId = pagamento.getFormaPagamento() != null ? pagamento.getFormaPagamento().getId() : null;
        String formaNome = pagamento.getFormaPagamento() != null ? pagamento.getFormaPagamento().getNome() : null;
        Long condId = pagamento.getCondicaoPagamento() != null ? pagamento.getCondicaoPagamento().getId() : null;
        String condNome = pagamento.getCondicaoPagamento() != null ? pagamento.getCondicaoPagamento().getNome() : null;
        Long contaId = pagamento.getContaBancaria() != null ? pagamento.getContaBancaria().getId() : null;
        String contaNome = pagamento.getContaBancaria() != null ? pagamento.getContaBancaria().getTitularConta() : null;

        java.time.LocalDate dataPag = pagamento.getDataPagamento() != null ? pagamento.getDataPagamento().toLocalDate() : null;

        java.math.BigDecimal valorOriginal = pagamento.getValorPago();
        java.math.BigDecimal valorTotal = pagamento.getValorPago();
        java.math.BigDecimal valorDesconto = java.math.BigDecimal.ZERO;
        java.math.BigDecimal valorJuros = java.math.BigDecimal.ZERO;
        java.math.BigDecimal valorMulta = java.math.BigDecimal.ZERO;

        return new PagamentoResponse(
                pagamento.getId(),
                pagamento.getEmpresaId(),
                pagamento.getFatura() != null ? pagamento.getFatura().getId() : null,
                pagamento.getOsId(),
                pagamento.getFatura() != null ? pagamento.getFatura().getNumeroFatura() : null,
                null,
                pagamento.getClienteId(),
                formaId,
                formaNome,
                condId,
                condNome,
                contaId,
                contaNome,
                dataPag,
                valorOriginal,
                valorDesconto,
                valorJuros,
                valorMulta,
                valorTotal,
                pagamento.getStatus(),
                pagamento.getComprovanteUrl(),
                pagamento.getObservacoes(),
                parcelas.stream().map(parcelaMapper::toResponse).collect(Collectors.toList()),
                null,
                null);
    }
}
