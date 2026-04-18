package com.neritech.saas.integracao.service;

import com.neritech.saas.integracao.domain.IntegracaoAtiva;
import com.neritech.saas.integracao.dto.IntegracaoAtivaRequest;
import com.neritech.saas.integracao.dto.IntegracaoAtivaResponse;
import com.neritech.saas.integracao.mapper.IntegracaoAtivaMapper;
import com.neritech.saas.integracao.repository.IntegracaoAtivaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IntegracaoAtivaService {

    private final IntegracaoAtivaRepository repository;
    private final IntegracaoAtivaMapper mapper;

    @Transactional(readOnly = true)
    public List<IntegracaoAtivaResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public IntegracaoAtivaResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("IntegraÃ§Ã£o nÃ£o encontrada com id: " + id));
    }

    @Transactional
    public IntegracaoAtivaResponse create(IntegracaoAtivaRequest request) {
        IntegracaoAtiva entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public IntegracaoAtivaResponse update(Long id, IntegracaoAtivaRequest request) {
        IntegracaoAtiva entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("IntegraÃ§Ã£o nÃ£o encontrada com id: " + id));

        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("IntegraÃ§Ã£o nÃ£o encontrada com id: " + id);
        }
        repository.deleteById(id);
    }
}
