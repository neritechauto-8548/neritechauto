package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.dto.PlanoAssinaturaRequest;
import com.neritech.saas.empresa.dto.PlanoAssinaturaResponse;
import com.neritech.saas.empresa.mapper.PlanoAssinaturaMapper;
import com.neritech.saas.empresa.repository.PlanoAssinaturaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlanoAssinaturaService {

    private final PlanoAssinaturaRepository repository;
    private final PlanoAssinaturaMapper mapper;

    public PlanoAssinaturaService(PlanoAssinaturaRepository repository, PlanoAssinaturaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public PlanoAssinaturaResponse create(PlanoAssinaturaRequest request) {
        PlanoAssinatura plano = mapper.toEntity(request);
        PlanoAssinatura saved = repository.save(plano);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PlanoAssinaturaResponse findById(Long id) {
        PlanoAssinatura plano = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plano nÃ£o encontrado com ID: " + id));
        return mapper.toResponse(plano);
    }

    @Transactional(readOnly = true)
    public Page<PlanoAssinaturaResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<PlanoAssinaturaResponse> findAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public PlanoAssinaturaResponse update(Long id, PlanoAssinaturaRequest request) {
        PlanoAssinatura plano = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plano nÃ£o encontrado com ID: " + id));

        mapper.updateEntityFromRequest(request, plano);
        PlanoAssinatura updated = repository.save(plano);
        return mapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Plano nÃ£o encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
