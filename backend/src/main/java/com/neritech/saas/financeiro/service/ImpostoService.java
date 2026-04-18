package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.Imposto;
import com.neritech.saas.financeiro.dto.ImpostoRequest;
import com.neritech.saas.financeiro.dto.ImpostoResponse;
import com.neritech.saas.financeiro.mapper.ImpostoMapper;
import com.neritech.saas.financeiro.repository.ImpostoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImpostoService {

    private final ImpostoRepository repository;
    private final ImpostoMapper mapper;

    @Transactional(readOnly = true)
    public Page<ImpostoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ImpostoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Imposto nÃ£o encontrado"));
    }

    @Transactional
    public ImpostoResponse create(ImpostoRequest request) {
        Imposto entity = mapper.toEntity(request);
        entity.setCriadoPor(1L); // TODO: Get from security context
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public ImpostoResponse update(Long id, ImpostoRequest request) {
        Imposto entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Imposto nÃ£o encontrado"));

        mapper.updateEntityFromDTO(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        Imposto entity = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Imposto nÃ£o encontrado"));
        repository.delete(entity);
    }
}
