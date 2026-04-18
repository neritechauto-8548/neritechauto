package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.PlanoConta;
import com.neritech.saas.financeiro.dto.PlanoContaRequest;
import com.neritech.saas.financeiro.dto.PlanoContaResponse;
import com.neritech.saas.financeiro.mapper.PlanoContaMapper;
import com.neritech.saas.financeiro.repository.PlanoContaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlanoContaService {

    private final PlanoContaRepository repository;
    private final PlanoContaMapper mapper;

    @Transactional(readOnly = true)
    public Page<PlanoContaResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public PlanoContaResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Plano de contas nÃ£o encontrado"));
    }

    @Transactional
    public PlanoContaResponse create(PlanoContaRequest request) {
        PlanoConta entity = mapper.toEntity(request);
        entity.setCriadoPor(1L); // TODO: Get from security context

        if (request.contaPaiId() != null) {
            PlanoConta pai = repository.findById(request.contaPaiId())
                    .orElseThrow(() -> new EntityNotFoundException("Conta pai nÃ£o encontrada"));
            entity.setContaPai(pai);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public PlanoContaResponse update(Long id, PlanoContaRequest request) {
        PlanoConta entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plano de contas nÃ£o encontrado"));

        mapper.updateEntityFromDTO(request, entity);

        if (request.contaPaiId() != null) {
            PlanoConta pai = repository.findById(request.contaPaiId())
                    .orElseThrow(() -> new EntityNotFoundException("Conta pai nÃ£o encontrada"));
            entity.setContaPai(pai);
        } else {
            entity.setContaPai(null);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        PlanoConta entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plano de contas nÃ£o encontrado"));
        repository.delete(entity);
    }
}
