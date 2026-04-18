package com.neritech.saas.fiscal.service;

import com.neritech.saas.fiscal.domain.SpedFiscal;
import com.neritech.saas.fiscal.dto.SpedFiscalRequest;
import com.neritech.saas.fiscal.dto.SpedFiscalResponse;
import com.neritech.saas.fiscal.mapper.SpedFiscalMapper;
import com.neritech.saas.fiscal.repository.SpedFiscalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpedFiscalService {

    private final SpedFiscalRepository repository;
    private final SpedFiscalMapper mapper;

    @Transactional(readOnly = true)
    public List<SpedFiscalResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SpedFiscalResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("SPED Fiscal nÃ£o encontrado com id: " + id));
    }

    @Transactional
    public SpedFiscalResponse create(SpedFiscalRequest request) {
        SpedFiscal entity = mapper.toEntity(request);
        entity.setDataGeracao(LocalDateTime.now());
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public SpedFiscalResponse update(Long id, SpedFiscalRequest request) {
        SpedFiscal entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SPED Fiscal nÃ£o encontrado com id: " + id));

        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("SPED Fiscal nÃ£o encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
