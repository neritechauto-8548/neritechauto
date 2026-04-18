package com.neritech.saas.relatorios.service;

import com.neritech.saas.relatorios.domain.KpiOficina;
import com.neritech.saas.relatorios.dto.KpiOficinaRequest;
import com.neritech.saas.relatorios.dto.KpiOficinaResponse;
import com.neritech.saas.relatorios.mapper.KpiOficinaMapper;
import com.neritech.saas.relatorios.repository.KpiOficinaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KpiOficinaService {

    private final KpiOficinaRepository kpiRepository;
    private final KpiOficinaMapper kpiMapper;

    @Transactional
    public KpiOficinaResponse create(KpiOficinaRequest request) {
        KpiOficina entity = kpiMapper.toEntity(request);
        KpiOficina saved = kpiRepository.save(entity);
        return kpiMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<KpiOficinaResponse> findByEmpresa(Long empresaId) {
        return kpiRepository.findByEmpresaId(empresaId).stream()
                .map(kpiMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public KpiOficinaResponse update(Long id, KpiOficinaRequest request) {
        KpiOficina entity = kpiRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KPI nÃ£o encontrado: " + id));

        kpiMapper.updateEntity(entity, request);
        KpiOficina updated = kpiRepository.save(entity);
        return kpiMapper.toResponse(updated);
    }
}
