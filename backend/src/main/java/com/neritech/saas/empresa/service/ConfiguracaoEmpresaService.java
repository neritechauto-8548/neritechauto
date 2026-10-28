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
import java.util.LinkedHashMap;
import java.util.Map;
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
    private final EmpresaAlteracaoService auditoria;

    public ConfiguracaoEmpresaService(ConfiguracaoEmpresaRepository configuracaoEmpresaRepository,
            EmpresaRepository empresaRepository,
            ConfiguracaoEmpresaMapper configuracaoEmpresaMapper, EmpresaAlteracaoService auditoria) {
        this.configuracaoEmpresaRepository = configuracaoEmpresaRepository;
        this.empresaRepository = empresaRepository;
        this.configuracaoEmpresaMapper = configuracaoEmpresaMapper;
        this.auditoria = auditoria;
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

        ConfiguracaoEmpresa saved = configuracaoEmpresaRepository.saveAndFlush(configuracao);
        auditoria.registrar(request.empresaId(), "configuracoes_empresa", saved.getId(),
                saved.getVersao(), Map.of(), camposAuditaveis(saved));
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

        Map<String, Object> antes = camposAuditaveis(configuracao);
        configuracaoEmpresaMapper.updateEntityFromRequest(request, configuracao);
        ConfiguracaoEmpresa updated = configuracaoEmpresaRepository.saveAndFlush(configuracao);
        auditoria.registrar(request.empresaId(), "configuracoes_empresa", updated.getId(),
                updated.getVersao(), antes, camposAuditaveis(updated));
        return configuracaoEmpresaMapper.toResponse(updated);
    }

    private static Map<String, Object> camposAuditaveis(ConfiguracaoEmpresa config) {
        Map<String, Object> campos = new LinkedHashMap<>();
        campos.put("regimeTributario", config.getRegimeTributario());
        campos.put("codigoCnaePrincipal", config.getCodigoCnaePrincipal());
        campos.put("codigoCnaeSecundario", config.getCodigoCnaeSecundario());
        campos.put("capitalSocial", config.getCapitalSocial());
        campos.put("porteEmpresa", config.getPorteEmpresa());
        campos.put("situacaoCadastral", config.getSituacaoCadastral());
        return campos;
    }

    public void delete(Long id) {
        ConfiguracaoEmpresa config = configuracaoEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Configuração da empresa não encontrada"));
        TenantAccess.require(config.getEmpresa().getId());
        configuracaoEmpresaRepository.delete(config);
    }
}
