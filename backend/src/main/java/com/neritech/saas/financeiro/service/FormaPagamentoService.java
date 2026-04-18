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
        FormaPagamento entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context

        if (request.contaBancariaId() != null) {
            ContaBancaria conta = contaBancariaRepository.findByIdAndEmpresaId(request.contaBancariaId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Conta bancÃ¡ria nÃ£o encontrada"));
            entity.setContaBancaria(conta);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public FormaPagamentoResponse update(Long id, Long empresaId, FormaPagamentoRequest request) {
        FormaPagamento entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento nÃ£o encontrada"));

        mapper.updateEntityFromDTO(request, entity);

        if (request.contaBancariaId() != null) {
            ContaBancaria conta = contaBancariaRepository.findByIdAndEmpresaId(request.contaBancariaId(), empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Conta bancÃ¡ria nÃ£o encontrada"));
            entity.setContaBancaria(conta);
        } else {
            entity.setContaBancaria(null);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        FormaPagamento entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Forma de pagamento nÃ£o encontrada"));
        repository.delete(entity);
    }
}
