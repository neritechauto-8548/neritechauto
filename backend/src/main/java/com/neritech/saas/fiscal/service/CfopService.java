package com.neritech.saas.fiscal.service;

import com.neritech.saas.fiscal.domain.Cfop;
import com.neritech.saas.fiscal.dto.CfopRequest;
import com.neritech.saas.fiscal.dto.CfopResponse;
import com.neritech.saas.fiscal.mapper.CfopMapper;
import com.neritech.saas.fiscal.repository.CfopRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CfopService {

    private final CfopRepository repository;
    private final CfopMapper mapper;

    @Transactional(readOnly = true)
    public List<CfopResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CfopResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("CFOP nÃ£o encontrado com id: " + id));
    }

    @Transactional
    public CfopResponse create(CfopRequest request) {
        if (repository.findByCodigo(request.codigo()).isPresent()) {
            throw new IllegalArgumentException("JÃ¡ existe um CFOP com este cÃ³digo.");
        }
        Cfop entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public CfopResponse update(Long id, CfopRequest request) {
        Cfop entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CFOP nÃ£o encontrado com id: " + id));

        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("CFOP nÃ£o encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
