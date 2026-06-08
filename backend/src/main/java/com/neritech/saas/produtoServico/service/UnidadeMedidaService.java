package com.neritech.saas.produtoServico.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.produtoServico.domain.UnidadeMedida;
import com.neritech.saas.produtoServico.dto.UnidadeMedidaRequest;
import com.neritech.saas.produtoServico.dto.UnidadeMedidaResponse;
import com.neritech.saas.produtoServico.mapper.UnidadeMedidaMapper;
import com.neritech.saas.produtoServico.repository.UnidadeMedidaRepository;

import java.util.List;

@Service
public class UnidadeMedidaService {

    private final UnidadeMedidaRepository repository;
    private final UnidadeMedidaMapper mapper;

    public UnidadeMedidaService(UnidadeMedidaRepository repository, UnidadeMedidaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    private void validarUnidadeMedida(UnidadeMedidaRequest request, Long id) {
        if (request.nome() == null || request.nome().trim().isBlank()) {
            throw new BusinessException("O nome da unidade de medida é obrigatório.");
        }
        if (request.nome().trim().length() < 2) {
            throw new BusinessException("O nome da unidade de medida deve ter pelo menos 2 caracteres.");
        }
        if (request.nome().trim().length() > 50) {
            throw new BusinessException("O nome da unidade de medida não pode exceder 50 caracteres.");
        }

        if (request.sigla() == null || request.sigla().trim().isBlank()) {
            throw new BusinessException("A sigla da unidade de medida é obrigatória.");
        }
        if (request.sigla().trim().length() < 1) {
            throw new BusinessException("A sigla deve ter pelo menos 1 caractere.");
        }
        if (request.sigla().trim().length() > 10) {
            throw new BusinessException("A sigla não pode exceder 10 caracteres.");
        }

        Long tenantId = com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();

        boolean nomeExists;
        boolean siglaExists;

        if (id == null) {
            nomeExists = repository.existsByEmpresaIdAndNomeIgnoreCase(tenantId, request.nome().trim());
            siglaExists = repository.existsByEmpresaIdAndSiglaIgnoreCase(tenantId, request.sigla().trim());
        } else {
            nomeExists = repository.existsByEmpresaIdAndNomeIgnoreCaseAndIdNot(tenantId, request.nome().trim(), id);
            siglaExists = repository.existsByEmpresaIdAndSiglaIgnoreCaseAndIdNot(tenantId, request.sigla().trim(), id);
        }

        if (nomeExists) {
            throw new BusinessException("Já existe uma unidade de medida cadastrada com este nome.");
        }
        if (siglaExists) {
            throw new BusinessException("Já existe uma unidade de medida cadastrada com esta sigla.");
        }
    }

    @Transactional
    public UnidadeMedidaResponse create(UnidadeMedidaRequest request) {
        validarUnidadeMedida(request, null);

        Long tenantId = com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();
        UnidadeMedida entity = mapper.toEntity(request);
        entity.setEmpresaId(tenantId);
        UnidadeMedida saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public UnidadeMedidaResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de medida não encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<UnidadeMedidaResponse> findAll(Pageable pageable) {
        Long tenantId = com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();
        return repository.findByEmpresaId(tenantId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<UnidadeMedidaResponse> findActive() {
        Long tenantId = com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();
        return repository.findByEmpresaIdAndAtivoTrue(tenantId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public UnidadeMedidaResponse update(Long id, UnidadeMedidaRequest request) {
        UnidadeMedida entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de medida não encontrada com ID: " + id));

        validarUnidadeMedida(request, id);

        mapper.updateEntityFromRequest(request, entity);
        UnidadeMedida saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Unidade de medida não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}
