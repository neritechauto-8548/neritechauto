package com.neritech.saas.ordemservico.service;

import com.neritech.saas.ordemservico.domain.*;
import com.neritech.saas.ordemservico.dto.OSChecklistCopyRequest;
import com.neritech.saas.ordemservico.dto.OSChecklistItemRequest;
import com.neritech.saas.ordemservico.dto.OSChecklistItemResponse;
import com.neritech.saas.ordemservico.mapper.OSChecklistItemMapper;
import com.neritech.saas.ordemservico.repository.ItChecklistRepository;
import com.neritech.saas.ordemservico.repository.OSChecklistItemRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OSChecklistService {

    private final OSChecklistItemRepository repository;
    private final OrdemServicoRepository osRepository;
    private final ItChecklistRepository itChecklistRepository;
    private final OSChecklistItemMapper mapper;

    public OSChecklistService(OSChecklistItemRepository repository,
                              OrdemServicoRepository osRepository,
                              ItChecklistRepository itChecklistRepository,
                              OSChecklistItemMapper mapper) {
        this.repository = repository;
        this.osRepository = osRepository;
        this.itChecklistRepository = itChecklistRepository;
        this.mapper = mapper;
    }

    public List<OSChecklistItemResponse> copyFromChecklist(OSChecklistCopyRequest request) {
        OrdemServico os = osRepository.findById(request.ordemServicoId())
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Serviço não encontrada"));

        List<ItChecklist> itensModelo = itChecklistRepository.findByChecklist_Id(request.checklistId());
        if (itensModelo.isEmpty()) {
            return List.of();
        }

        int ordem = 1;
        for (ItChecklist it : itensModelo) {
            OSChecklistItem item = new OSChecklistItem();
            item.setOrdemServico(os);
            item.setChecklistModelo(it.getChecklist());
            item.setItemModelo(it);
            item.setDescricao(it.getDsItChecklist());
            item.setFeito(false);
            item.setOrdem(ordem++);
            repository.save(item);
        }

        return repository.findByOrdemServico_Id(os.getId()).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OSChecklistItemResponse> listByOS(Long ordemServicoId) {
        return repository.findByOrdemServico_Id(ordemServicoId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public OSChecklistItemResponse update(Long id, OSChecklistItemRequest request) {
        OSChecklistItem entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de checklist da OS não encontrado"));
        mapper.updateEntityFromRequest(request, entity);
        OSChecklistItem saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Item de checklist da OS não encontrado");
        }
        repository.deleteById(id);
    }
}
