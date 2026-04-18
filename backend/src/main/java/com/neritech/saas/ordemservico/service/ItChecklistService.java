package com.neritech.saas.ordemservico.service;

import com.neritech.saas.ordemservico.domain.Checklist;
import com.neritech.saas.ordemservico.domain.ItChecklist;
import com.neritech.saas.ordemservico.dto.ItChecklistRequest;
import com.neritech.saas.ordemservico.dto.ItChecklistResponse;
import com.neritech.saas.ordemservico.mapper.ItChecklistMapper;
import com.neritech.saas.ordemservico.repository.ChecklistRepository;
import com.neritech.saas.ordemservico.repository.ItChecklistRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("ordemServicoItChecklistService")
@Transactional
public class ItChecklistService {

    private final ItChecklistRepository repository;
    private final ChecklistRepository checklistRepository;
    private final ItChecklistMapper mapper;

    public ItChecklistService(ItChecklistRepository repository,
                              ChecklistRepository checklistRepository,
                              ItChecklistMapper mapper) {
        this.repository = repository;
        this.checklistRepository = checklistRepository;
        this.mapper = mapper;
    }

    public ItChecklistResponse create(ItChecklistRequest request) {
        Checklist checklist = checklistRepository.findById(request.checkListId())
                .orElseThrow(() -> new EntityNotFoundException("Checklist não encontrado com ID: " + request.checkListId()));
        ItChecklist entity = mapper.toEntity(request);
        entity.setChecklist(checklist);
        ItChecklist saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ItChecklistResponse findById(Long id) {
        ItChecklist entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de checklist não encontrado com ID: " + id));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<ItChecklistResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ItChecklistResponse> findByChecklistId(Long checklistId) {
        return repository.findByChecklist_Id(checklistId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public ItChecklistResponse update(Long id, ItChecklistRequest request) {
        ItChecklist entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de checklist não encontrado com ID: " + id));
        if (!entity.getChecklist().getId().equals(request.checkListId())) {
            Checklist checklist = checklistRepository.findById(request.checkListId())
                    .orElseThrow(() -> new EntityNotFoundException("Checklist não encontrado com ID: " + request.checkListId()));
            entity.setChecklist(checklist);
        }
        mapper.updateEntityFromRequest(request, entity);
        ItChecklist updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Item de checklist não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
