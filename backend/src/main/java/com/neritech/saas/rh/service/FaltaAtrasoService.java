package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.FaltaAtraso;
import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.mapper.FaltaAtrasoMapper;
import com.neritech.saas.rh.repository.FaltaAtrasoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaltaAtrasoService {
    private final FaltaAtrasoRepository repository;
    private final FaltaAtrasoMapper mapper;

    public List<FaltaAtrasoResponse> findByFuncionario(Long funcionarioId) {
        return repository.findByFuncionarioId(funcionarioId).stream().map(mapper::toResponse).toList();
    }

    public FaltaAtrasoResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Falta/Atraso nÃ£o encontrado"));
    }

    @Transactional
    public FaltaAtrasoResponse create(FaltaAtrasoRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional
    public FaltaAtrasoResponse update(Long id, FaltaAtrasoRequest request) {
        FaltaAtraso entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Falta/Atraso nÃ£o encontrado"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Falta/Atraso nÃ£o encontrado");
        repository.deleteById(id);
    }
}
