package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.ConfiguracaoFiscal;
import com.neritech.saas.common.tenancy.TenantAccess;
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
        TenantAccess.require(request.empresaId());
        rejectPlaintextSecrets(request);
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
                .map(config -> { TenantAccess.require(config.getEmpresa().getId()); return mapper.toResponse(config); })
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o fiscal nÃ£o encontrada"));
    }

    @Transactional(readOnly = true)
    public ConfiguracaoFiscalResponse findByEmpresaId(Long empresaId) {
        TenantAccess.require(empresaId);
        return repository.findByEmpresaId(empresaId)
                .map(mapper::toResponse)
                .orElseGet(() -> ConfiguracaoFiscalResponse.builder().empresaId(empresaId).build());
    }

    @Transactional(readOnly = true)
    public Page<ConfiguracaoFiscalResponse> findAll(Pageable pageable) {
        Long empresaId = TenantAccess.requireCurrent();
        var own = repository.findByEmpresaId(empresaId).map(mapper::toResponse);
        var content = pageable.getOffset() == 0 ? own.stream().toList() : java.util.List.<ConfiguracaoFiscalResponse>of();
        return new org.springframework.data.domain.PageImpl<>(content, pageable, own.isPresent() ? 1 : 0);
    }

    public ConfiguracaoFiscalResponse update(Long id, ConfiguracaoFiscalRequest request) {
        TenantAccess.require(request.empresaId());
        rejectPlaintextSecrets(request);
        ConfiguracaoFiscal config = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o fiscal nÃ£o encontrada"));

        TenantAccess.require(config.getEmpresa().getId());
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
        ConfiguracaoFiscal config = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguraÃ§Ã£o fiscal nÃ£o encontrada"));
        TenantAccess.require(config.getEmpresa().getId());
        repository.delete(config);
    }
    private static void rejectPlaintextSecrets(ConfiguracaoFiscalRequest request) {
        if (hasContent(request.certificadoDigitalA1()) || hasContent(request.senhaCertificado())
                || hasContent(request.tokenAcessoProvedorHomologacao())
                || hasContent(request.tokenAcessoProvedorProducao())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY,
                    "Credenciais fiscais não podem ser gravadas no cadastro comum da empresa");
        }
    }

    private static boolean hasContent(String value) {
        return value != null && !value.isBlank();
    }

}
