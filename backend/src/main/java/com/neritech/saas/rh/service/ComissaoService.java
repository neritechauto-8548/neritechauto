package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.Comissao;
import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.mapper.ComissaoMapper;
import com.neritech.saas.rh.repository.ComissaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ComissaoService {
    private final ComissaoRepository repository;
    private final ComissaoMapper mapper;

    public Page<ComissaoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable).map(mapper::toResponse);
    }

    public ComissaoResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("ComissÃƒÂ£o nÃƒÂ£o encontrada"));
    }

    @Transactional
    public ComissaoResponse create(ComissaoRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional
    public ComissaoResponse update(Long id, ComissaoRequest request) {
        Comissao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ComissÃƒÂ£o nÃƒÂ£o encontrada"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("ComissÃƒÂ£o nÃƒÂ£o encontrada");
        repository.deleteById(id);
    }
}
