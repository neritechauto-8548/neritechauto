package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.EscalaTrabalho;
import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.mapper.EscalaTrabalhoMapper;
import com.neritech.saas.rh.repository.EscalaTrabalhoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EscalaTrabalhoService {
    private final EscalaTrabalhoRepository repository;
    private final EscalaTrabalhoMapper mapper;

    public List<EscalaTrabalhoResponse> findByEmpresa(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream().map(mapper::toResponse).toList();
    }

    public EscalaTrabalhoResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Escala de trabalho nÃƒÂ£o encontrada"));
    }

    @Transactional
    public EscalaTrabalhoResponse create(EscalaTrabalhoRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional
    public EscalaTrabalhoResponse update(Long id, EscalaTrabalhoRequest request) {
        EscalaTrabalho entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escala de trabalho nÃƒÂ£o encontrada"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Escala de trabalho nÃƒÂ£o encontrada");
        repository.deleteById(id);
    }
}
