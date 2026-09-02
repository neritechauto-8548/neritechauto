package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.ordemservico.domain.StatusOS;
import com.neritech.saas.ordemservico.dto.StatusOSRequest;
import com.neritech.saas.ordemservico.dto.StatusOSResponse;
import com.neritech.saas.ordemservico.mapper.StatusOSMapper;
import com.neritech.saas.ordemservico.repository.StatusOSRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StatusOSService {

    private final StatusOSRepository repository;
    private final StatusOSMapper mapper;

    public StatusOSService(StatusOSRepository repository, StatusOSMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public StatusOSResponse create(StatusOSRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant(request.empresaId());
        if (repository.existsByEmpresaIdAndCodigo(tenantId, request.codigo())) {
            throw new IllegalArgumentException("Código de status já existe para esta empresa");
        }
        StatusOS entity = mapper.toEntity(request);
        StatusOS saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public StatusOSResponse findById(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        StatusOS entity = repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Status não encontrado para a empresa autenticada"));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<StatusOSResponse> findByEmpresaId(Long empresaId, Pageable pageable) {
        Long tenantId = TenantAccess.requireCurrentTenant(empresaId);
        return repository.findByEmpresaId(tenantId, pageable)
                .map(mapper::toResponse);
    }

    public StatusOSResponse update(Long id, StatusOSRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant(request.empresaId());
        StatusOS entity = repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Status não encontrado para a empresa autenticada"));
        mapper.updateEntityFromRequest(request, entity);
        StatusOS updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        StatusOS entity = repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Status não encontrado para a empresa autenticada"));
        repository.delete(entity);
    }
}
