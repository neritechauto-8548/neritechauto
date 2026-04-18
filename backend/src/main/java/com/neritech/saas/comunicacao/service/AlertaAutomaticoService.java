package com.neritech.saas.comunicacao.service;

import com.neritech.saas.comunicacao.domain.AlertaAutomatico;
import com.neritech.saas.comunicacao.dto.AlertaAutomaticoRequest;
import com.neritech.saas.comunicacao.dto.AlertaAutomaticoResponse;
import com.neritech.saas.comunicacao.mapper.AlertaAutomaticoMapper;
import com.neritech.saas.comunicacao.repository.AlertaAutomaticoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertaAutomaticoService {

    private final AlertaAutomaticoRepository repository;
    private final AlertaAutomaticoMapper mapper;

    @Transactional(readOnly = true)
    public Page<AlertaAutomaticoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public AlertaAutomaticoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Alerta automÃ¡tico nÃ£o encontrado com id: " + id));
    }

    public AlertaAutomaticoResponse create(AlertaAutomaticoRequest request) {
        AlertaAutomatico entity = mapper.toEntity(request);
        AlertaAutomatico savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    public AlertaAutomaticoResponse update(Long id, AlertaAutomaticoRequest request) {
        AlertaAutomatico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alerta automÃ¡tico nÃ£o encontrado com id: " + id));

        mapper.updateEntity(request, entity);
        AlertaAutomatico updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Alerta automÃ¡tico nÃ£o encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
