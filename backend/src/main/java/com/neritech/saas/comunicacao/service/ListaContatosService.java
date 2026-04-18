package com.neritech.saas.comunicacao.service;

import com.neritech.saas.comunicacao.domain.ListaContatos;
import com.neritech.saas.comunicacao.dto.ListaContatosRequest;
import com.neritech.saas.comunicacao.dto.ListaContatosResponse;
import com.neritech.saas.comunicacao.mapper.ListaContatosMapper;
import com.neritech.saas.comunicacao.repository.ListaContatosRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ListaContatosService {

    private final ListaContatosRepository repository;
    private final ListaContatosMapper mapper;

    @Transactional(readOnly = true)
    public Page<ListaContatosResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ListaContatosResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Lista de contatos nÃ£o encontrada com id: " + id));
    }

    public ListaContatosResponse create(ListaContatosRequest request) {
        if (repository.existsByEmpresaIdAndNome(request.empresaId(), request.nome())) {
            throw new IllegalArgumentException("JÃ¡ existe uma lista com este nome para esta empresa.");
        }
        ListaContatos entity = mapper.toEntity(request);
        ListaContatos savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    public ListaContatosResponse update(Long id, ListaContatosRequest request) {
        ListaContatos entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lista de contatos nÃ£o encontrada com id: " + id));

        mapper.updateEntity(request, entity);
        ListaContatos updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lista de contatos nÃ£o encontrada com id: " + id);
        }
        repository.deleteById(id);
    }
}
