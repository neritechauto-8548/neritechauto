package com.neritech.saas.relatorios.service;

import com.neritech.saas.relatorios.domain.MetricaPerformance;
import com.neritech.saas.relatorios.dto.MetricaPerformanceRequest;
import com.neritech.saas.relatorios.dto.MetricaPerformanceResponse;
import com.neritech.saas.relatorios.mapper.MetricaPerformanceMapper;
import com.neritech.saas.relatorios.repository.MetricaPerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetricaPerformanceService {

    private final MetricaPerformanceRepository metricaRepository;
    private final MetricaPerformanceMapper metricaMapper;

    @Transactional
    public MetricaPerformanceResponse create(MetricaPerformanceRequest request) {
        MetricaPerformance entity = metricaMapper.toEntity(request);
        MetricaPerformance saved = metricaRepository.save(entity);
        return metricaMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MetricaPerformanceResponse> findByEmpresa(Long empresaId) {
        return metricaRepository.findByEmpresaId(empresaId).stream()
                .map(metricaMapper::toResponse)
                .collect(Collectors.toList());
    }
}
