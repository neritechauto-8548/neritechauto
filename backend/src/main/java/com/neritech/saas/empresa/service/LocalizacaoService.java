package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.Localizacao;
import com.neritech.saas.empresa.dto.LocalizacaoRequest;
import com.neritech.saas.empresa.dto.LocalizacaoResponse;
import com.neritech.saas.empresa.mapper.LocalizacaoMapper;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.empresa.repository.LocalizacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocalizacaoService {

    private final LocalizacaoRepository repository;
    private final EmpresaRepository empresaRepository;
    private final LocalizacaoMapper mapper;

    public LocalizacaoService(LocalizacaoRepository repository,
                              EmpresaRepository empresaRepository,
                              LocalizacaoMapper mapper) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.mapper = mapper;
    }

    public LocalizacaoResponse create(LocalizacaoRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.empresaId()));

        Localizacao entity = mapper.toEntity(request);
        entity.setEmpresa(empresa);

        Localizacao saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public LocalizacaoResponse findById(Long id) {
        Localizacao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Localização não encontrada com ID: " + id));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<LocalizacaoResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<LocalizacaoResponse> findByEmpresaId(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public LocalizacaoResponse update(Long id, LocalizacaoRequest request) {
        Localizacao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Localização não encontrada com ID: " + id));

        if (!entity.getEmpresa().getId().equals(request.empresaId())) {
            Empresa empresa = empresaRepository.findById(request.empresaId())
                    .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + request.empresaId()));
            entity.setEmpresa(empresa);
        }

        mapper.updateEntityFromRequest(request, entity);
        Localizacao updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Localização não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}

