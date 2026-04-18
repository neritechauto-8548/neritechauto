package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.ContaBancaria;
import com.neritech.saas.financeiro.dto.ContaBancariaRequest;
import com.neritech.saas.financeiro.dto.ContaBancariaResponse;
import com.neritech.saas.financeiro.mapper.ContaBancariaMapper;
import com.neritech.saas.financeiro.repository.ContaBancariaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContaBancariaService {

    private final ContaBancariaRepository repository;
    private final ContaBancariaMapper mapper;

    @Transactional(readOnly = true)
    public Page<ContaBancariaResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ContaBancariaResponse findById(Long id, Long empresaId) {
        return repository.findByIdAndEmpresaId(id, empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Conta bancÃ¡ria nÃ£o encontrada"));
    }

    @Transactional
    public ContaBancariaResponse create(Long empresaId, ContaBancariaRequest request) {
        ContaBancaria entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        entity.setCriadoPor(1L); // TODO: Get from security context
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public ContaBancariaResponse update(Long id, Long empresaId, ContaBancariaRequest request) {
        ContaBancaria entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta bancÃ¡ria nÃ£o encontrada"));

        mapper.updateEntityFromDTO(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        ContaBancaria entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta bancÃ¡ria nÃ£o encontrada"));
        repository.delete(entity);
    }
}
