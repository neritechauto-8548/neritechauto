package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.ordemservico.domain.Diagnostico;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.dto.DiagnosticoRequest;
import com.neritech.saas.ordemservico.dto.DiagnosticoResponse;
import com.neritech.saas.ordemservico.mapper.DiagnosticoMapper;
import com.neritech.saas.ordemservico.repository.DiagnosticoRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DiagnosticoService {

    private final DiagnosticoRepository repository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final DiagnosticoMapper mapper;

    public DiagnosticoService(
            DiagnosticoRepository repository,
            OrdemServicoRepository ordemServicoRepository,
            DiagnosticoMapper mapper) {
        this.repository = repository;
        this.ordemServicoRepository = ordemServicoRepository;
        this.mapper = mapper;
    }

    public DiagnosticoResponse create(DiagnosticoRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        OrdemServico ordemServico = requireOwnedOrder(request.ordemServicoId(), tenantId);

        Diagnostico entity = mapper.toEntity(request);
        entity.setOrdemServico(ordemServico);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public DiagnosticoResponse findById(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        return mapper.toResponse(requireOwnedDiagnostico(id, tenantId));
    }

    @Transactional(readOnly = true)
    public List<DiagnosticoResponse> findByOrdemServicoId(Long ordemServicoId) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        requireOwnedOrder(ordemServicoId, tenantId);
        return repository.findByOrdemServico_IdAndOrdemServico_EmpresaId(ordemServicoId, tenantId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public DiagnosticoResponse update(Long id, DiagnosticoRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        Diagnostico entity = requireOwnedDiagnostico(id, tenantId);
        OrdemServico ordemServico = requireOwnedOrder(request.ordemServicoId(), tenantId);

        mapper.updateEntityFromRequest(request, entity);
        entity.setOrdemServico(ordemServico);
        return mapper.toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        repository.delete(requireOwnedDiagnostico(id, tenantId));
    }

    private OrdemServico requireOwnedOrder(Long ordemServicoId, Long tenantId) {
        return ordemServicoRepository.findByIdAndEmpresaId(ordemServicoId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ordem de serviço não encontrada para a empresa autenticada"));
    }

    private Diagnostico requireOwnedDiagnostico(Long id, Long tenantId) {
        return repository.findByIdAndOrdemServico_EmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Diagnóstico não encontrado para a empresa autenticada"));
    }
}
