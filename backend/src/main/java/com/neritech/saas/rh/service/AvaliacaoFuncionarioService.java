package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.AvaliacaoFuncionario;
import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.mapper.AvaliacaoFuncionarioMapper;
import com.neritech.saas.rh.repository.AvaliacaoFuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AvaliacaoFuncionarioService {
    private final AvaliacaoFuncionarioRepository repository;
    private final AvaliacaoFuncionarioMapper mapper;

    public List<AvaliacaoFuncionarioResponse> findByFuncionario(Long funcionarioId) {
        return repository.findByFuncionarioId(funcionarioId).stream().map(mapper::toResponse).toList();
    }

    public AvaliacaoFuncionarioResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("AvaliaÃ§Ã£o nÃ£o encontrada"));
    }

    @Transactional
    public AvaliacaoFuncionarioResponse create(AvaliacaoFuncionarioRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional
    public AvaliacaoFuncionarioResponse update(Long id, AvaliacaoFuncionarioRequest request) {
        AvaliacaoFuncionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AvaliaÃ§Ã£o nÃ£o encontrada"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("AvaliaÃ§Ã£o nÃ£o encontrada");
        repository.deleteById(id);
    }
}
