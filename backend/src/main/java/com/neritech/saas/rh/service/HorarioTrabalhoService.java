package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.HorarioTrabalho;
import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.mapper.HorarioTrabalhoMapper;
import com.neritech.saas.rh.repository.HorarioTrabalhoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HorarioTrabalhoService {
    private final HorarioTrabalhoRepository repository;
    private final HorarioTrabalhoMapper mapper;

    public List<HorarioTrabalhoResponse> findByEmpresa(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream().map(mapper::toResponse).toList();
    }

    public HorarioTrabalhoResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("HorÃƒÂ¡rio de trabalho nÃƒÂ£o encontrado"));
    }

    @Transactional
    public HorarioTrabalhoResponse create(HorarioTrabalhoRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional
    public HorarioTrabalhoResponse update(Long id, HorarioTrabalhoRequest request) {
        HorarioTrabalho entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("HorÃƒÂ¡rio de trabalho nÃƒÂ£o encontrado"));
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("HorÃƒÂ¡rio de trabalho nÃƒÂ£o encontrado");
        repository.deleteById(id);
    }
}
