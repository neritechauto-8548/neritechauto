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
        validarItChecklist(null, request);
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
        validarItChecklist(id, request);
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

    private void validarItChecklist(Long id, ItChecklistRequest request) {
        if (request.dsItChecklist() == null || request.dsItChecklist().trim().isEmpty()) {
            throw new com.neritech.saas.common.exception.BusinessException("A descrição do item de checklist é obrigatória.");
        }
        if (request.dsItChecklist().trim().length() < 2) {
            throw new com.neritech.saas.common.exception.BusinessException("A descrição do item de checklist deve ter pelo menos 2 caracteres.");
        }
        if (request.checkListId() == null) {
            throw new com.neritech.saas.common.exception.BusinessException("O ID do checklist é obrigatório.");
        }

        boolean duplicado = id == null
                ? repository.existsByChecklist_IdAndDsItChecklistIgnoreCase(request.checkListId(), request.dsItChecklist().trim())
                : repository.existsByChecklist_IdAndDsItChecklistIgnoreCaseAndIdNot(request.checkListId(), request.dsItChecklist().trim(), id);
        if (duplicado) {
            throw new com.neritech.saas.common.exception.BusinessException("Já existe um item cadastrado com esta descrição neste checklist.");
        }
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Item de checklist não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
