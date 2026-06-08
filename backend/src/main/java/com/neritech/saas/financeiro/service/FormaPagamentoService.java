package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.ContaBancaria;
import com.neritech.saas.financeiro.domain.FormaPagamento;
import com.neritech.saas.financeiro.dto.FormaPagamentoRequest;
import com.neritech.saas.financeiro.dto.FormaPagamentoResponse;
import com.neritech.saas.financeiro.mapper.FormaPagamentoMapper;
import com.neritech.saas.financeiro.repository.ContaBancariaRepository;
import com.neritech.saas.financeiro.repository.FormaPagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FormaPagamentoService {

    private final FormaPagamentoRepository repository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final FormaPagamentoMapper mapper;

    @Transactional(readOnly = true)
    public Page<FormaPagamentoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public FormaPagamentoResponse findById(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento nÃ£o encontrada"));
    }

    @Transactional
    public FormaPagamentoResponse create(Long empresaId, FormaPagamentoRequest request) {
        validarFormaPagamento(empresaId, null, request);
        
        if (Boolean.TRUE.equals(request.padrao())) {
            desmarcarOutrosPadrao(empresaId, null);
        }

        FormaPagamento entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context

        if (!Boolean.TRUE.equals(entity.getAceitaParcelamento())) {
            entity.setParcelasMaximas(1);
        }

        if (request.contaBancariaId() != null) {
            ContaBancaria conta = contaBancariaRepository.findByIdAndEmpresaId(request.contaBancariaId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Conta bancária não encontrada"));
            entity.setContaBancaria(conta);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public FormaPagamentoResponse update(Long id, Long empresaId, FormaPagamentoRequest request) {
        validarFormaPagamento(empresaId, id, request);

        if (Boolean.TRUE.equals(request.padrao())) {
            desmarcarOutrosPadrao(empresaId, id);
        }

        FormaPagamento entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento não encontrada"));

        mapper.updateEntityFromDTO(request, entity);

        if (!Boolean.TRUE.equals(entity.getAceitaParcelamento())) {
            entity.setParcelasMaximas(1);
        }

        if (request.contaBancariaId() != null) {
            ContaBancaria conta = contaBancariaRepository.findByIdAndEmpresaId(request.contaBancariaId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Conta bancária não encontrada"));
            entity.setContaBancaria(conta);
        } else {
            entity.setContaBancaria(null);
        }

        return mapper.toResponse(repository.save(entity));
    }

    private void desmarcarOutrosPadrao(Long empresaId, Long currentId) {
        java.util.List<FormaPagamento> padroes = repository.findByEmpresaIdAndPadraoTrue(empresaId);
        for (FormaPagamento fp : padroes) {
            if (currentId == null || !fp.getId().equals(currentId)) {
                fp.setPadrao(false);
                repository.save(fp);
            }
        }
    }

    private void validarFormaPagamento(Long empresaId, Long id, FormaPagamentoRequest request) {
        if (request.nome() == null || request.nome().trim().isEmpty()) {
            throw new com.neritech.saas.common.exception.BusinessException("O nome da forma de pagamento é obrigatório.");
        }
        if (request.nome().trim().length() < 2) {
            throw new com.neritech.saas.common.exception.BusinessException("O nome da forma de pagamento deve ter pelo menos 2 caracteres.");
        }
        if (request.tipo() == null) {
            throw new com.neritech.saas.common.exception.BusinessException("O tipo da forma de pagamento é obrigatório.");
        }

        boolean duplicado = id == null
                ? repository.existsByEmpresaIdAndNomeIgnoreCase(empresaId, request.nome().trim())
                : repository.existsByEmpresaIdAndNomeIgnoreCaseAndIdNot(empresaId, request.nome().trim(), id);
        if (duplicado) {
            throw new com.neritech.saas.common.exception.BusinessException("Já existe uma forma de pagamento cadastrada com este nome.");
        }

        if (Boolean.TRUE.equals(request.aceitaParcelamento())) {
            if (request.parcelasMaximas() == null || request.parcelasMaximas() < 2) {
                throw new com.neritech.saas.common.exception.BusinessException("Para aceitar parcelamento, a quantidade máxima de parcelas deve ser maior ou igual a 2.");
            }
        }

        if (request.taxaAdministracao() != null) {
            if (request.taxaAdministracao().compareTo(java.math.BigDecimal.ZERO) < 0 ||
                request.taxaAdministracao().compareTo(new java.math.BigDecimal("100")) > 0) {
                throw new com.neritech.saas.common.exception.BusinessException("A taxa de administração deve estar entre 0% e 100%.");
            }
        }

        if (request.prazoRecebimentoDias() != null && request.prazoRecebimentoDias() < 0) {
            throw new com.neritech.saas.common.exception.BusinessException("O prazo de recebimento em dias não pode ser negativo.");
        }

        if (request.limiteDiario() != null && request.limiteDiario().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new com.neritech.saas.common.exception.BusinessException("O limite diário não pode ser negativo.");
        }
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        FormaPagamento entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento não encontrada"));
        repository.delete(entity);
    }
}
