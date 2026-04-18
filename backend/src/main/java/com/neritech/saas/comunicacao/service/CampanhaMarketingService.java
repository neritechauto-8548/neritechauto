package com.neritech.saas.comunicacao.service;

import com.neritech.saas.comunicacao.domain.CampanhaMarketing;
import com.neritech.saas.comunicacao.dto.CampanhaMarketingRequest;
import com.neritech.saas.comunicacao.dto.CampanhaMarketingResponse;
import com.neritech.saas.comunicacao.mapper.CampanhaMarketingMapper;
import com.neritech.saas.comunicacao.repository.CampanhaMarketingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CampanhaMarketingService {

    private final CampanhaMarketingRepository repository;
    private final CampanhaMarketingMapper mapper;

    @Transactional(readOnly = true)
    public Page<CampanhaMarketingResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public CampanhaMarketingResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Campanha nÃ£o encontrada com id: " + id));
    }

    public CampanhaMarketingResponse create(CampanhaMarketingRequest request) {
        CampanhaMarketing entity = mapper.toEntity(request);
        CampanhaMarketing savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

    public CampanhaMarketingResponse update(Long id, CampanhaMarketingRequest request) {
        CampanhaMarketing entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campanha nÃ£o encontrada com id: " + id));

        mapper.updateEntity(request, entity);
        CampanhaMarketing updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Campanha nÃ£o encontrada com id: " + id);
        }
        repository.deleteById(id);
    }
}
