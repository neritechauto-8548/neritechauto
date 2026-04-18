package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.ConfiguracaoFiscal;
import com.neritech.saas.empresa.dto.ConfiguracaoFiscalRequest;
import com.neritech.saas.empresa.dto.ConfiguracaoFiscalResponse;
import com.neritech.saas.empresa.mapper.ConfiguracaoFiscalMapper;
import com.neritech.saas.empresa.repository.ConfiguracaoFiscalRepository;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConfiguracaoFiscalService {

    private final ConfiguracaoFiscalRepository repository;
    private final EmpresaRepository empresaRepository;
    private final ConfiguracaoFiscalMapper mapper;

    public ConfiguracaoFiscalService(ConfiguracaoFiscalRepository repository,
            EmpresaRepository empresaRepository,
            ConfiguracaoFiscalMapper mapper) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.mapper = mapper;
    }

    public ConfiguracaoFiscalResponse create(ConfiguracaoFiscalRequest request) {
        var empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa nÃ£o encontrada"));

        if (repository.existsByEmpresaId(request.empresaId())) {
            throw new IllegalStateException("JÃ¡ existe configuraÃ§Ã£o fiscal para esta empresa");
        }

        ConfiguracaoFiscal config = mapper.toEntity(request);
        config.setEmpresa(empresa);
        return mapper.toResponse(repository.save(config));
    }

    @Transactional(readOnly = true)
    public ConfiguracaoFiscalResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o fiscal nÃ£o encontrada"));
    }

    @Transactional(readOnly = true)
    public ConfiguracaoFiscalResponse findByEmpresaId(Long empresaId) {
        return repository.findByEmpresaId(empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o fiscal nÃ£o encontrada para esta empresa"));
    }

    @Transactional(readOnly = true)
    public Page<ConfiguracaoFiscalResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    public ConfiguracaoFiscalResponse update(Long id, ConfiguracaoFiscalRequest request) {
        ConfiguracaoFiscal config = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o fiscal nÃ£o encontrada"));

        if (!config.getEmpresa().getId().equals(request.empresaId())) {
            var empresa = empresaRepository.findById(request.empresaId())
                    .orElseThrow(() -> new EntityNotFoundException("Empresa nÃ£o encontrada"));

            if (repository.existsByEmpresaId(request.empresaId())) {
                throw new IllegalStateException("JÃ¡ existe configuraÃ§Ã£o fiscal para esta empresa");
            }
            config.setEmpresa(empresa);
        }

        mapper.updateEntityFromRequest(request, config);
        return mapper.toResponse(repository.save(config));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ConfiguraÃ§Ã£o fiscal nÃ£o encontrada");
        }
        repository.deleteById(id);
    }
}
