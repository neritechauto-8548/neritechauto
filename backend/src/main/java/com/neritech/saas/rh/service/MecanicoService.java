package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.Mecanico;
import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.mapper.MecanicoMapper;
import com.neritech.saas.rh.repository.MecanicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MecanicoService {
    private final MecanicoRepository repository;
    private final MecanicoMapper mapper;

    public Page<MecanicoResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    public MecanicoResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("MecÃ¢nico nÃ£o encontrado"));
    }

    @Transactional
    public MecanicoResponse create(MecanicoRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional
    public MecanicoResponse update(Long id, MecanicoRequest request) {
        Mecanico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MecÃ¢nico nÃ£o encontrado"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("MecÃ¢nico nÃ£o encontrado");
        repository.deleteById(id);
    }
}
