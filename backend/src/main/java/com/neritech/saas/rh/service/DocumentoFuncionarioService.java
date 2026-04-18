package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.DocumentoFuncionario;
import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.mapper.DocumentoFuncionarioMapper;
import com.neritech.saas.rh.repository.DocumentoFuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentoFuncionarioService {
    private final DocumentoFuncionarioRepository repository;
    private final DocumentoFuncionarioMapper mapper;

    public List<DocumentoFuncionarioResponse> findByFuncionario(Long funcionarioId) {
        return repository.findByFuncionarioId(funcionarioId).stream().map(mapper::toResponse).toList();
    }

    public DocumentoFuncionarioResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Documento nÃ£o encontrado"));
    }

    @Transactional
    public DocumentoFuncionarioResponse create(DocumentoFuncionarioRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional
    public DocumentoFuncionarioResponse update(Long id, DocumentoFuncionarioRequest request) {
        DocumentoFuncionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento nÃ£o encontrado"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Documento nÃ£o encontrado");
        repository.deleteById(id);
    }
}
