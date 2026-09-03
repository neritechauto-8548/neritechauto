package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.ordemservico.domain.Checklist;
import com.neritech.saas.ordemservico.dto.ChecklistRequest;
import com.neritech.saas.ordemservico.dto.ChecklistResponse;
import com.neritech.saas.ordemservico.mapper.ChecklistMapper;
import com.neritech.saas.ordemservico.repository.ChecklistRepository;
import com.neritech.saas.ordemservico.repository.ItChecklistRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChecklistService {

    private final ChecklistRepository repository;
    private final ItChecklistRepository itChecklistRepository;
    private final ChecklistMapper mapper;

    public ChecklistService(
            ChecklistRepository repository,
            ItChecklistRepository itChecklistRepository,
            ChecklistMapper mapper) {
        this.repository = repository;
        this.itChecklistRepository = itChecklistRepository;
        this.mapper = mapper;
    }

    public ChecklistResponse create(ChecklistRequest request) {
        Long tenantId = requireRequestTenant(request.empresaId());
        validarChecklist(tenantId, null, request);

        Checklist entity = mapper.toEntity(request);
        entity.setEmpresaId(tenantId);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public ChecklistResponse findById(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        Checklist entity = repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Checklist não encontrado para a empresa autenticada"));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<ChecklistResponse> findAll(Long requestedEmpresaId, Pageable pageable) {
        Long tenantId = requestedEmpresaId != null
                ? TenantAccess.requireCurrentTenant(requestedEmpresaId)
                : TenantAccess.requireCurrentTenant();
        return repository.findByEmpresaId(tenantId, pageable).map(mapper::toResponse);
    }

    public ChecklistResponse update(Long id, ChecklistRequest request) {
        Long tenantId = requireRequestTenant(request.empresaId());
        Checklist entity = repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Checklist não encontrado para a empresa autenticada"));

        validarChecklist(tenantId, id, request);
        mapper.updateEntityFromRequest(request, entity);
        entity.setEmpresaId(tenantId);
        return mapper.toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        Checklist entity = repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Checklist não encontrado para a empresa autenticada"));
        itChecklistRepository.deleteByChecklist_Id(entity.getId());
        repository.delete(entity);
    }

    private Long requireRequestTenant(Long requestedEmpresaId) {
        if (requestedEmpresaId == null) {
            return TenantAccess.requireCurrentTenant();
        }
        return TenantAccess.requireCurrentTenant(requestedEmpresaId);
    }

    private void validarChecklist(Long empresaId, Long id, ChecklistRequest request) {
        if (request.dsChecklist() == null || request.dsChecklist().trim().isEmpty()) {
            throw new BusinessException("O título do checklist é obrigatório.");
        }
        if (request.dsChecklist().trim().length() < 2) {
            throw new BusinessException("O título do checklist deve ter pelo menos 2 caracteres.");
        }

        boolean duplicado = id == null
                ? repository.existsByEmpresaIdAndDsChecklistIgnoreCase(empresaId, request.dsChecklist().trim())
                : repository.existsByEmpresaIdAndDsChecklistIgnoreCaseAndIdNot(
                        empresaId,
                        request.dsChecklist().trim(),
                        id);
        if (duplicado) {
            throw new BusinessException("Já existe um checklist cadastrado com esta descrição.");
        }
    }
}
