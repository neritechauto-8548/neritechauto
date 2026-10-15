package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.ConfiguracaoEmpresa;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.empresa.dto.ConfiguracaoEmpresaRequest;
import com.neritech.saas.empresa.dto.ConfiguracaoEmpresaResponse;
import com.neritech.saas.empresa.mapper.ConfiguracaoEmpresaMapper;
import com.neritech.saas.empresa.repository.ConfiguracaoEmpresaRepository;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConfiguracaoEmpresaService {

    private final ConfiguracaoEmpresaRepository configuracaoEmpresaRepository;
    private final EmpresaRepository empresaRepository;
    private final ConfiguracaoEmpresaMapper configuracaoEmpresaMapper;

    public ConfiguracaoEmpresaService(ConfiguracaoEmpresaRepository configuracaoEmpresaRepository,
            EmpresaRepository empresaRepository,
            ConfiguracaoEmpresaMapper configuracaoEmpresaMapper) {
        this.configuracaoEmpresaRepository = configuracaoEmpresaRepository;
        this.empresaRepository = empresaRepository;
        this.configuracaoEmpresaMapper = configuracaoEmpresaMapper;
    }

    public ConfiguracaoEmpresaResponse create(ConfiguracaoEmpresaRequest request) {
        TenantAccess.require(request.empresaId());
        // Validate empresa exists
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Empresa nÃ£o encontrada com id: " + request.empresaId()));

        // Check if configuration already exists for this empresa
        if (configuracaoEmpresaRepository.existsByEmpresaId(request.empresaId())) {
            throw new IllegalStateException("JÃ¡ existe uma configuraÃ§Ã£o para esta empresa");
        }

        ConfiguracaoEmpresa configuracao = configuracaoEmpresaMapper.toEntity(request);
        configuracao.setEmpresa(empresa);

        ConfiguracaoEmpresa saved = configuracaoEmpresaRepository.save(configuracao);
        return configuracaoEmpresaMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ConfiguracaoEmpresaResponse findById(Long id) {
        ConfiguracaoEmpresa configuracao = configuracaoEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o nÃ£o encontrada com id: " + id));
        TenantAccess.require(configuracao.getEmpresa().getId());
        return configuracaoEmpresaMapper.toResponse(configuracao);
    }

    @Transactional(readOnly = true)
    public ConfiguracaoEmpresaResponse findByEmpresaId(Long empresaId) {
        TenantAccess.require(empresaId);
        return configuracaoEmpresaRepository.findByEmpresaId(empresaId)
                .map(configuracaoEmpresaMapper::toResponse)
                .orElseGet(() -> new ConfiguracaoEmpresaResponse(
                        null, empresaId, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
    }

    @Transactional(readOnly = true)
    public Page<ConfiguracaoEmpresaResponse> findAll(Pageable pageable) {
        Long empresaId = TenantAccess.requireCurrent();
        var own = configuracaoEmpresaRepository.findByEmpresaId(empresaId)
                .map(configuracaoEmpresaMapper::toResponse);
        var content = pageable.getOffset() == 0 ? own.stream().toList() : java.util.List.<ConfiguracaoEmpresaResponse>of();
        return new org.springframework.data.domain.PageImpl<>(content, pageable, own.isPresent() ? 1 : 0);
    }

    public ConfiguracaoEmpresaResponse update(Long id, ConfiguracaoEmpresaRequest request) {
        TenantAccess.require(request.empresaId());
        ConfiguracaoEmpresa configuracao = configuracaoEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o nÃ£o encontrada com id: " + id));

        TenantAccess.require(configuracao.getEmpresa().getId());
        // Validate empresa exists if changing
        if (!configuracao.getEmpresa().getId().equals(request.empresaId())) {
            Empresa empresa = empresaRepository.findById(request.empresaId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Empresa nÃ£o encontrada com id: " + request.empresaId()));

            // Check if configuration already exists for the new empresa
            if (configuracaoEmpresaRepository.existsByEmpresaId(request.empresaId())) {
                throw new IllegalStateException("JÃ¡ existe uma configuraÃ§Ã£o para esta empresa");
            }

            configuracao.setEmpresa(empresa);
        }

        configuracaoEmpresaMapper.updateEntityFromRequest(request, configuracao);
        ConfiguracaoEmpresa updated = configuracaoEmpresaRepository.save(configuracao);
        return configuracaoEmpresaMapper.toResponse(updated);
    }

    public void delete(Long id) {
        ConfiguracaoEmpresa config = configuracaoEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Configuração da empresa não encontrada"));
        TenantAccess.require(config.getEmpresa().getId());
        configuracaoEmpresaRepository.delete(config);
    }
}
