package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.FuncionarioEspecialidade;
import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.mapper.FuncionarioEspecialidadeMapper;
import com.neritech.saas.rh.repository.FuncionarioEspecialidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FuncionarioEspecialidadeService {
    private final FuncionarioEspecialidadeRepository repository;
    private final FuncionarioEspecialidadeMapper mapper;

    public List<FuncionarioEspecialidadeResponse> findByFuncionario(Long funcionarioId) {
        return repository.findByFuncionarioId(funcionarioId).stream().map(mapper::toResponse).toList();
    }

    public FuncionarioEspecialidadeResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Especialidade do funcionÃ¡rio nÃ£o encontrada"));
    }

    @Transactional
    public FuncionarioEspecialidadeResponse create(FuncionarioEspecialidadeRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional
    public FuncionarioEspecialidadeResponse update(Long id, FuncionarioEspecialidadeRequest request) {
        FuncionarioEspecialidade entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Especialidade do funcionÃ¡rio nÃ£o encontrada"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Especialidade do funcionÃ¡rio nÃ£o encontrada");
        repository.deleteById(id);
    }
}
