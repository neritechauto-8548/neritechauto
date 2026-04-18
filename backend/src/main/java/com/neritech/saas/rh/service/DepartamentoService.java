package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.Departamento;
import com.neritech.saas.rh.dto.DepartamentoRequest;
import com.neritech.saas.rh.dto.DepartamentoResponse;
import com.neritech.saas.rh.mapper.DepartamentoMapper;
import com.neritech.saas.rh.repository.DepartamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartamentoService {
    private final DepartamentoRepository repository;
    private final DepartamentoMapper mapper;

    public Page<DepartamentoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable).map(mapper::toResponse);
    }

    public DepartamentoResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Departamento nÃƒÂ£o encontrado"));
    }

    @Transactional
    public DepartamentoResponse create(DepartamentoRequest request) {
        Departamento entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public DepartamentoResponse update(Long id, DepartamentoRequest request) {
        Departamento entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento nÃƒÂ£o encontrado"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Departamento nÃƒÂ£o encontrado");
        repository.deleteById(id);
    }
}
