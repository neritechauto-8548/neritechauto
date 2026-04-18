package com.neritech.saas.fiscal.service;

import com.neritech.saas.fiscal.domain.RegimeTributario;
import com.neritech.saas.fiscal.dto.RegimeTributarioRequest;
import com.neritech.saas.fiscal.dto.RegimeTributarioResponse;
import com.neritech.saas.fiscal.mapper.RegimeTributarioMapper;
import com.neritech.saas.fiscal.repository.RegimeTributarioRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegimeTributarioService {

    private final RegimeTributarioRepository repository;
    private final RegimeTributarioMapper mapper;

    @Transactional(readOnly = true)
    public List<RegimeTributarioResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RegimeTributarioResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Regime TributÃ¡rio nÃ£o encontrado com id: " + id));
    }

    @Transactional
    public RegimeTributarioResponse create(RegimeTributarioRequest request) {
        if (repository.findByCodigo(request.codigo()).isPresent()) {
            throw new IllegalArgumentException("JÃ¡ existe um Regime TributÃ¡rio com este cÃ³digo.");
        }
        RegimeTributario entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public RegimeTributarioResponse update(Long id, RegimeTributarioRequest request) {
        RegimeTributario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Regime TributÃ¡rio nÃ£o encontrado com id: " + id));

        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Regime TributÃ¡rio nÃ£o encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
