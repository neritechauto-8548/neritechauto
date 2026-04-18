package com.neritech.saas.produtoServico.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public UnidadeMedidaResponse create(UnidadeMedidaRequest request) {
        Long tenantId = com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();
        if (repository.findByEmpresaIdAndSigla(tenantId, request.sigla()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma unidade de medida com esta sigla");
        }

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

        Long tenantId = com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();
        if (!entity.getSigla().equals(request.sigla()) && repository.findByEmpresaIdAndSigla(tenantId, request.sigla()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma unidade de medida com esta sigla");
        }

        mapper.updateEntityFromRequest(request, entity);
        UnidadeMedida saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Unidade de medida nÃ£o encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}
