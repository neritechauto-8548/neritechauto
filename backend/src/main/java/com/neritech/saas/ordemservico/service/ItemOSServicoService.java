package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.dto.ItemOSServicoRequest;
import com.neritech.saas.ordemservico.dto.ItemOSServicoResponse;
import com.neritech.saas.ordemservico.mapper.ItemOSServicoMapper;
import com.neritech.saas.ordemservico.repository.ItemOSServicoRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemOSServicoService {

    private final ItemOSServicoRepository repository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final ItemOSServicoMapper mapper;

    public ItemOSServicoService(
            ItemOSServicoRepository repository,
            OrdemServicoRepository ordemServicoRepository,
            ItemOSServicoMapper mapper) {
        this.repository = repository;
        this.ordemServicoRepository = ordemServicoRepository;
        this.mapper = mapper;
    }

    public ItemOSServicoResponse create(ItemOSServicoRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        OrdemServico ordemServico = requireOwnedOrder(request.ordemServicoId(), tenantId);

        ItemOSServico entity = mapper.toEntity(request);
        entity.setOrdemServico(ordemServico);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public ItemOSServicoResponse findById(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        return mapper.toResponse(requireOwnedItem(id, tenantId));
    }

    @Transactional(readOnly = true)
    public List<ItemOSServicoResponse> findByOrdemServicoId(Long ordemServicoId) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        requireOwnedOrder(ordemServicoId, tenantId);
        return repository.findByOrdemServico_IdAndOrdemServico_EmpresaId(ordemServicoId, tenantId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public ItemOSServicoResponse update(Long id, ItemOSServicoRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        ItemOSServico entity = requireOwnedItem(id, tenantId);
        OrdemServico ordemServico = requireOwnedOrder(request.ordemServicoId(), tenantId);

        mapper.updateEntityFromRequest(request, entity);
        entity.setOrdemServico(ordemServico);
        return mapper.toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        repository.delete(requireOwnedItem(id, tenantId));
    }

    private OrdemServico requireOwnedOrder(Long ordemServicoId, Long tenantId) {
        return ordemServicoRepository.findByIdAndEmpresaId(ordemServicoId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ordem de serviço não encontrada para a empresa autenticada"));
    }

    private ItemOSServico requireOwnedItem(Long id, Long tenantId) {
        return repository.findByIdAndOrdemServico_EmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item de serviço não encontrado para a empresa autenticada"));
    }
}
