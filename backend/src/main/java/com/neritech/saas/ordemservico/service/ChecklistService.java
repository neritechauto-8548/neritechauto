package com.neritech.saas.ordemservico.service;

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

    public ChecklistService(ChecklistRepository repository, ItChecklistRepository itChecklistRepository, ChecklistMapper mapper) {
        this.repository = repository;
        this.itChecklistRepository = itChecklistRepository;
        this.mapper = mapper;
    }

    public ChecklistResponse create(ChecklistRequest request) {
        Checklist entity = mapper.toEntity(request);
        Checklist saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ChecklistResponse findById(Long id) {
        Checklist entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Checklist não encontrado com ID: " + id));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<ChecklistResponse> findAll(Long empresaId, Pageable pageable) {
        if (empresaId != null) {
            return repository.findByEmpresaId(empresaId, pageable).map(mapper::toResponse);
        }
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    public ChecklistResponse update(Long id, ChecklistRequest request) {
        Checklist entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Checklist não encontrado com ID: " + id));
        mapper.updateEntityFromRequest(request, entity);
        Checklist updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Checklist não encontrado com ID: " + id);
        }
        itChecklistRepository.deleteByChecklist_Id(id);
        repository.deleteById(id);
    }
}
