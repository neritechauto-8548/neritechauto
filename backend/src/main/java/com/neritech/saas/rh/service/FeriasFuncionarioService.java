package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.FeriasFuncionario;
import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.mapper.FeriasFuncionarioMapper;
import com.neritech.saas.rh.repository.FeriasFuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeriasFuncionarioService {
    private final FeriasFuncionarioRepository repository;
    private final FeriasFuncionarioMapper mapper;

    public List<FeriasFuncionarioResponse> findByFuncionario(Long funcionarioId) {
        return repository.findByFuncionarioId(funcionarioId).stream().map(mapper::toResponse).toList();
    }

    public FeriasFuncionarioResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("FÃ©rias nÃ£o encontradas"));
    }

    @Transactional
    public FeriasFuncionarioResponse create(FeriasFuncionarioRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional
    public FeriasFuncionarioResponse update(Long id, FeriasFuncionarioRequest request) {
        FeriasFuncionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FÃ©rias nÃ£o encontradas"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("FÃ©rias nÃ£o encontradas");
        repository.deleteById(id);
    }
}
