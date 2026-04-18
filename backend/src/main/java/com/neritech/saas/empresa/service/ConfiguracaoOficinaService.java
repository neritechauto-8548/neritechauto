package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.ConfiguracaoOficina;
import com.neritech.saas.empresa.dto.ConfiguracaoOficinaRequest;
import com.neritech.saas.empresa.dto.ConfiguracaoOficinaResponse;
import com.neritech.saas.empresa.mapper.ConfiguracaoOficinaMapper;
import com.neritech.saas.empresa.repository.ConfiguracaoOficinaRepository;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConfiguracaoOficinaService {

    private final ConfiguracaoOficinaRepository repository;
    private final EmpresaRepository empresaRepository;
    private final ConfiguracaoOficinaMapper mapper;

    public ConfiguracaoOficinaService(ConfiguracaoOficinaRepository repository,
            EmpresaRepository empresaRepository,
            ConfiguracaoOficinaMapper mapper) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.mapper = mapper;
    }

    public ConfiguracaoOficinaResponse create(ConfiguracaoOficinaRequest request) {
        var empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa nÃ£o encontrada"));

        if (repository.existsByEmpresaId(request.empresaId())) {
            throw new IllegalStateException("JÃ¡ existe configuraÃ§Ã£o para esta empresa");
        }

        ConfiguracaoOficina config = mapper.toEntity(request);
        config.setEmpresa(empresa);
        return mapper.toResponse(repository.save(config));
    }

    @Transactional(readOnly = true)
    public ConfiguracaoOficinaResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o nÃ£o encontrada"));
    }

    @Transactional(readOnly = true)
    public ConfiguracaoOficinaResponse findByEmpresaId(Long empresaId) {
        return repository.findByEmpresaId(empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o nÃ£o encontrada para esta empresa"));
    }

    @Transactional(readOnly = true)
    public Page<ConfiguracaoOficinaResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    public ConfiguracaoOficinaResponse update(Long id, ConfiguracaoOficinaRequest request) {
        ConfiguracaoOficina config = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o nÃ£o encontrada"));

        if (!config.getEmpresa().getId().equals(request.empresaId())) {
            var empresa = empresaRepository.findById(request.empresaId())
                    .orElseThrow(() -> new EntityNotFoundException("Empresa nÃ£o encontrada"));

            if (repository.existsByEmpresaId(request.empresaId())) {
                throw new IllegalStateException("JÃ¡ existe configuraÃ§Ã£o para esta empresa");
            }
            config.setEmpresa(empresa);
        }

        mapper.updateEntityFromRequest(request, config);
        return mapper.toResponse(repository.save(config));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ConfiguraÃ§Ã£o nÃ£o encontrada");
        }
        repository.deleteById(id);
    }
}
