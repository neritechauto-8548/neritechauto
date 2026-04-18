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
    private final PagamentoMapper mapper;
    private final ParcelaPagamentoMapper parcelaMapper;

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
