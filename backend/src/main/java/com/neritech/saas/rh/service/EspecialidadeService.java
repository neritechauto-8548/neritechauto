package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.Especialidade;
import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.mapper.EspecialidadeMapper;
import com.neritech.saas.rh.repository.EspecialidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EspecialidadeService {
    private final EspecialidadeRepository repository;
    private final EspecialidadeMapper mapper;

    public Page<EspecialidadeResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable).map(mapper::toResponse);
    }

    public EspecialidadeResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Especialidade nÃƒÂ£o encontrada"));
    }

    @Transactional
    public EspecialidadeResponse create(EspecialidadeRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional
    public EspecialidadeResponse update(Long id, EspecialidadeRequest request) {
        Especialidade entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Especialidade nÃƒÂ£o encontrada"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Especialidade nÃƒÂ£o encontrada");
        repository.deleteById(id);
    }
}
