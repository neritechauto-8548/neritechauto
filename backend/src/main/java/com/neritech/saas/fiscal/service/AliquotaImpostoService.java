package com.neritech.saas.fiscal.service;

import com.neritech.saas.fiscal.domain.AliquotaImposto;
import com.neritech.saas.fiscal.dto.AliquotaImpostoRequest;
import com.neritech.saas.fiscal.dto.AliquotaImpostoResponse;
import com.neritech.saas.fiscal.mapper.AliquotaImpostoMapper;
import com.neritech.saas.fiscal.repository.AliquotaImpostoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AliquotaImpostoService {

    private final AliquotaImpostoRepository repository;
    private final AliquotaImpostoMapper mapper;

    @Transactional(readOnly = true)
    public List<AliquotaImpostoResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AliquotaImpostoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("AlÃ­quota de Imposto nÃ£o encontrada com id: " + id));
    }

    @Transactional
    public AliquotaImpostoResponse create(AliquotaImpostoRequest request) {
        AliquotaImposto entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public AliquotaImpostoResponse update(Long id, AliquotaImpostoRequest request) {
        AliquotaImposto entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AlÃ­quota de Imposto nÃ£o encontrada com id: " + id));

        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("AlÃ­quota de Imposto nÃ£o encontrada com id: " + id);
        }
        repository.deleteById(id);
    }
}
