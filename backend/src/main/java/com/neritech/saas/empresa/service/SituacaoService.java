package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.Situacao;
import com.neritech.saas.empresa.dto.SituacaoRequest;
import com.neritech.saas.empresa.dto.SituacaoResponse;
import com.neritech.saas.empresa.mapper.SituacaoMapper;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.empresa.repository.SituacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SituacaoService {

    private final SituacaoRepository repository;
    private final EmpresaRepository empresaRepository;
    private final SituacaoMapper mapper;

    public SituacaoService(SituacaoRepository repository,
                           EmpresaRepository empresaRepository,
                           SituacaoMapper mapper) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.mapper = mapper;
    }

    public SituacaoResponse create(SituacaoRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.empresaId()));

        Situacao entity = mapper.toEntity(request);
        entity.setEmpresa(empresa);
        Situacao saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public SituacaoResponse findById(Long id) {
        Situacao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Situação não encontrada com ID: " + id));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<SituacaoResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<SituacaoResponse> findByEmpresaId(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public SituacaoResponse update(Long id, SituacaoRequest request) {
        Situacao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Situação não encontrada com ID: " + id));

        if (!entity.getEmpresa().getId().equals(request.empresaId())) {
            Empresa empresa = empresaRepository.findById(request.empresaId())
                    .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.empresaId()));
            entity.setEmpresa(empresa);
        }

        mapper.updateEntityFromRequest(request, entity);
        Situacao updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Situação não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}

