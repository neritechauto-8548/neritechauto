package com.neritech.saas.comunicacao.service;

import com.neritech.saas.comunicacao.domain.TemplateComunicacao;
import com.neritech.saas.comunicacao.dto.TemplateComunicacaoRequest;
import com.neritech.saas.comunicacao.dto.TemplateComunicacaoResponse;
import com.neritech.saas.comunicacao.mapper.TemplateComunicacaoMapper;
import com.neritech.saas.comunicacao.repository.TemplateComunicacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TemplateComunicacaoService {

    private final TemplateComunicacaoRepository repository;
    private final TemplateComunicacaoMapper mapper;

    @Transactional(readOnly = true)
    public Page<TemplateComunicacaoResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public TemplateComunicacaoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Template nÃ£o encontrado com id: " + id));
    }

    public TemplateComunicacaoResponse create(TemplateComunicacaoRequest request) {
        TemplateComunicacao entity = mapper.toEntity(request);
        TemplateComunicacao savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    public TemplateComunicacaoResponse update(Long id, TemplateComunicacaoRequest request) {
        TemplateComunicacao entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Template nÃ£o encontrado com id: " + id));

        mapper.updateEntity(request, entity);
        TemplateComunicacao updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Template nÃ£o encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
