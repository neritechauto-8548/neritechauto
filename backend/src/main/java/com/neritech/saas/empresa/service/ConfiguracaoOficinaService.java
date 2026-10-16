package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.ConfiguracaoOficina;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.ZoneId;
import java.time.DateTimeException;
import java.util.Map;
import java.util.LinkedHashMap;
import com.neritech.saas.common.tenancy.TenantAccess;
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
    private final EmpresaAlteracaoService auditoria;

    public ConfiguracaoOficinaService(ConfiguracaoOficinaRepository repository,
            EmpresaRepository empresaRepository,
            ConfiguracaoOficinaMapper mapper, EmpresaAlteracaoService auditoria) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.mapper = mapper;
        this.auditoria = auditoria;
    }

    public ConfiguracaoOficinaResponse create(ConfiguracaoOficinaRequest request) {
        TenantAccess.require(request.empresaId());
        validarPreferencias(request);
        var empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa nÃ£o encontrada"));

        if (repository.existsByEmpresaId(request.empresaId())) {
            throw new IllegalStateException("JÃ¡ existe configuraÃ§Ã£o para esta empresa");
        }

        ConfiguracaoOficina config = mapper.toEntity(request);
        config.setEmpresa(empresa);
        if (config.getCorTema() == null) config.setCorTema("light");
        if (config.getMoeda() == null) config.setMoeda("BRL");
        ConfiguracaoOficina saved = repository.saveAndFlush(config);
        auditoria.registrar(request.empresaId(), "configuracoes_oficina", saved.getId(),
                saved.getVersao(), Map.of(), camposAuditaveis(saved));
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ConfiguracaoOficinaResponse findById(Long id) {
        return repository.findById(id)
                .map(config -> { TenantAccess.require(config.getEmpresa().getId()); return mapper.toResponse(config); })
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o nÃ£o encontrada"));
    }

    @Transactional(readOnly = true)
    public ConfiguracaoOficinaResponse findByEmpresaId(Long empresaId) {
        TenantAccess.require(empresaId);
        return repository.findByEmpresaId(empresaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o nÃ£o encontrada para esta empresa"));
    }

    @Transactional(readOnly = true)
    public Page<ConfiguracaoOficinaResponse> findAll(Pageable pageable) {
        Long empresaId = TenantAccess.requireCurrent();
        var own = repository.findByEmpresaId(empresaId).map(mapper::toResponse);
        var content = pageable.getOffset() == 0 ? own.stream().toList() : java.util.List.<ConfiguracaoOficinaResponse>of();
        return new org.springframework.data.domain.PageImpl<>(content, pageable, own.isPresent() ? 1 : 0);
    }

    public ConfiguracaoOficinaResponse update(Long id, ConfiguracaoOficinaRequest request) {
        TenantAccess.require(request.empresaId());
        validarPreferencias(request);
        ConfiguracaoOficina config = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o nÃ£o encontrada"));

        TenantAccess.require(config.getEmpresa().getId());
        if (!config.getEmpresa().getId().equals(request.empresaId())) {
            var empresa = empresaRepository.findById(request.empresaId())
                    .orElseThrow(() -> new EntityNotFoundException("Empresa nÃ£o encontrada"));

            if (repository.existsByEmpresaId(request.empresaId())) {
                throw new IllegalStateException("JÃ¡ existe configuraÃ§Ã£o para esta empresa");
            }
            config.setEmpresa(empresa);
        }

        Map<String, Object> antes = camposAuditaveis(config);
        mapper.updateEntityFromRequest(request, config);
        ConfiguracaoOficina saved = repository.saveAndFlush(config);
        auditoria.registrar(request.empresaId(), "configuracoes_oficina", saved.getId(),
                saved.getVersao(), antes, camposAuditaveis(saved));
        return mapper.toResponse(saved);
    }

    private static void validarPreferencias(ConfiguracaoOficinaRequest request) {
        if (request.moeda() != null && !"BRL".equals(request.moeda())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Moeda deve ser BRL");
        }
        if (request.timezone() != null) {
            try { ZoneId.of(request.timezone()); }
            catch (DateTimeException ex) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Fuso horário inválido");
            }
        }
    }

    private static Map<String, Object> camposAuditaveis(ConfiguracaoOficina c) {
        Map<String, Object> campos = new LinkedHashMap<>();
        campos.put("corTema", c.getCorTema());
        campos.put("timezone", c.getTimezone());
        campos.put("moeda", c.getMoeda());
        campos.put("horarioAberturaSegSex", c.getHorarioAberturaSegSex());
        campos.put("horarioFechamentoSegSex", c.getHorarioFechamentoSegSex());
        campos.put("horarioAberturaSabado", c.getHorarioAberturaSabado());
        campos.put("horarioFechamentoSabado", c.getHorarioFechamentoSabado());
        campos.put("horarioAberturaDomingo", c.getHorarioAberturaDomingo());
        campos.put("horarioFechamentoDomingo", c.getHorarioFechamentoDomingo());
        return campos;
    }

    public void delete(Long id) {
        ConfiguracaoOficina config = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Configuração da oficina não encontrada"));
        TenantAccess.require(config.getEmpresa().getId());
        repository.delete(config);
    }
}
