package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.Treinamento;
import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.mapper.TreinamentoMapper;
import com.neritech.saas.rh.repository.TreinamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TreinamentoService {
    private final TreinamentoRepository repository;
    private final TreinamentoMapper mapper;

    public Page<TreinamentoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable).map(mapper::toResponse);
    }

    public TreinamentoResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Treinamento nÃƒÂ£o encontrado"));
    }

    @Transactional
    public TreinamentoResponse create(TreinamentoRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional
    public TreinamentoResponse update(Long id, TreinamentoRequest request) {
        Treinamento entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Treinamento nÃƒÂ£o encontrado"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Treinamento nÃƒÂ£o encontrado");
        repository.deleteById(id);
    }
}
