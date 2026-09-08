package com.neritech.saas.empresa.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.empresa.domain.ConfiguracaoEmpresa;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.dto.ConfiguracaoEmpresaRequest;
import com.neritech.saas.empresa.dto.ConfiguracaoEmpresaResponse;
import com.neritech.saas.empresa.mapper.ConfiguracaoEmpresaMapper;
import com.neritech.saas.empresa.repository.ConfiguracaoEmpresaRepository;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ConfiguracaoEmpresaService {

    private final ConfiguracaoEmpresaRepository configuracaoEmpresaRepository;
    private final EmpresaRepository empresaRepository;
    private final ConfiguracaoEmpresaMapper configuracaoEmpresaMapper;

    public ConfiguracaoEmpresaService(
            ConfiguracaoEmpresaRepository configuracaoEmpresaRepository,
            EmpresaRepository empresaRepository,
            ConfiguracaoEmpresaMapper configuracaoEmpresaMapper) {
        this.configuracaoEmpresaRepository = configuracaoEmpresaRepository;
        this.empresaRepository = empresaRepository;
        this.configuracaoEmpresaMapper = configuracaoEmpresaMapper;
    }

    public ConfiguracaoEmpresaResponse create(ConfiguracaoEmpresaRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant(request.empresaId());
        Empresa empresa = empresaRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa autenticada não encontrada"));

        if (configuracaoEmpresaRepository.existsByEmpresaId(tenantId)) {
            throw new IllegalStateException("Já existe uma configuração para esta empresa");
        }

        ConfiguracaoEmpresa configuracao = configuracaoEmpresaMapper.toEntity(request);
        configuracao.setEmpresa(empresa);
        return configuracaoEmpresaMapper.toResponse(configuracaoEmpresaRepository.save(configuracao));
    }

    @Transactional(readOnly = true)
    public ConfiguracaoEmpresaResponse findById(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        return configuracaoEmpresaMapper.toResponse(findOwned(id, tenantId));
    }

    @Transactional(readOnly = true)
    public ConfiguracaoEmpresaResponse findByEmpresaId(Long empresaId) {
        Long tenantId = TenantAccess.requireCurrentTenant(empresaId);
        return configuracaoEmpresaRepository.findByEmpresaId(tenantId)
                .map(configuracaoEmpresaMapper::toResponse)
                .orElseGet(() -> emptyResponse(tenantId));
    }

    @Transactional(readOnly = true)
    public Page<ConfiguracaoEmpresaResponse> findAll(Pageable pageable) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        List<ConfiguracaoEmpresaResponse> content = configuracaoEmpresaRepository.findByEmpresaId(tenantId)
                .map(configuracaoEmpresaMapper::toResponse)
                .map(List::of)
                .orElseGet(List::of);
        return new PageImpl<>(content, pageable, content.size());
    }

    public ConfiguracaoEmpresaResponse update(Long id, ConfiguracaoEmpresaRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant(request.empresaId());
        ConfiguracaoEmpresa configuracao = findOwned(id, tenantId);

        // A empresa da configuração é imutável: input do cliente nunca transfere
        // configuração entre tenants.
        configuracaoEmpresaMapper.updateEntityFromRequest(request, configuracao);
        return configuracaoEmpresaMapper.toResponse(configuracaoEmpresaRepository.save(configuracao));
    }

    public void delete(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        configuracaoEmpresaRepository.delete(findOwned(id, tenantId));
    }

    private ConfiguracaoEmpresa findOwned(Long id, Long tenantId) {
        return configuracaoEmpresaRepository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Configuração não encontrada para a empresa autenticada"));
    }

    private ConfiguracaoEmpresaResponse emptyResponse(Long tenantId) {
        return new ConfiguracaoEmpresaResponse(
                null, tenantId, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null);
    }
}
