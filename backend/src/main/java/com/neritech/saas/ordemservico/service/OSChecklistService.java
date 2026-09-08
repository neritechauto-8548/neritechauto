package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.ordemservico.domain.Checklist;
import com.neritech.saas.ordemservico.domain.ItChecklist;
import com.neritech.saas.ordemservico.domain.OSChecklistItem;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.dto.OSChecklistCopyRequest;
import com.neritech.saas.ordemservico.dto.OSChecklistItemRequest;
import com.neritech.saas.ordemservico.dto.OSChecklistItemResponse;
import com.neritech.saas.ordemservico.mapper.OSChecklistItemMapper;
import com.neritech.saas.ordemservico.repository.ChecklistRepository;
import com.neritech.saas.ordemservico.repository.ItChecklistRepository;
import com.neritech.saas.ordemservico.repository.OSChecklistItemRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OSChecklistService {

    private final OSChecklistItemRepository repository;
    private final OrdemServicoRepository osRepository;
    private final ChecklistRepository checklistRepository;
    private final ItChecklistRepository itChecklistRepository;
    private final OSChecklistItemMapper mapper;

    public OSChecklistService(
            OSChecklistItemRepository repository,
            OrdemServicoRepository osRepository,
            ChecklistRepository checklistRepository,
            ItChecklistRepository itChecklistRepository,
            OSChecklistItemMapper mapper) {
        this.repository = repository;
        this.osRepository = osRepository;
        this.checklistRepository = checklistRepository;
        this.itChecklistRepository = itChecklistRepository;
        this.mapper = mapper;
    }

    public List<OSChecklistItemResponse> copyFromChecklist(OSChecklistCopyRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        OrdemServico os = requireOwnedOrder(request.ordemServicoId(), tenantId);
        Checklist checklist = checklistRepository.findByIdAndEmpresaId(request.checklistId(), tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Checklist modelo não encontrado para a empresa autenticada"));

        if (repository.existsByOrdemServico_IdAndOrdemServico_EmpresaIdAndChecklistModelo_Id(
                os.getId(), tenantId, checklist.getId())) {
            throw new BusinessException("Este checklist já está aplicado à Ordem de Serviço.");
        }

        List<ItChecklist> itensModelo = itChecklistRepository.findByChecklist_Id(checklist.getId());
        if (itensModelo.isEmpty()) {
            throw new BusinessException("O checklist selecionado não possui itens configurados.");
        }

        int ordem = repository.findByOrdemServico_IdAndOrdemServico_EmpresaId(os.getId(), tenantId).size() + 1;
        for (ItChecklist it : itensModelo) {
            OSChecklistItem item = new OSChecklistItem();
            item.setOrdemServico(os);
            item.setChecklistModelo(checklist);
            item.setItemModelo(it);
            item.setDescricao(it.getDsItChecklist());
            item.setFeito(false);
            item.setOrdem(ordem++);
            repository.save(item);
        }

        return listByOS(os.getId());
    }

    @Transactional(readOnly = true)
    public List<OSChecklistItemResponse> listByOS(Long ordemServicoId) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        requireOwnedOrder(ordemServicoId, tenantId);
        return repository.findByOrdemServico_IdAndOrdemServico_EmpresaId(ordemServicoId, tenantId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public OSChecklistItemResponse update(Long id, OSChecklistItemRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        OSChecklistItem entity = repository.findByIdAndOrdemServico_EmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item de checklist da OS não encontrado para a empresa autenticada"));
        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        OSChecklistItem entity = repository.findByIdAndOrdemServico_EmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item de checklist da OS não encontrado para a empresa autenticada"));
        repository.delete(entity);
    }

    private OrdemServico requireOwnedOrder(Long ordemServicoId, Long tenantId) {
        return osRepository.findByIdAndEmpresaId(ordemServicoId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ordem de serviço não encontrada para a empresa autenticada"));
    }
}
