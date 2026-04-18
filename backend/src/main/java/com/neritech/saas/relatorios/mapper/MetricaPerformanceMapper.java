package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.MetricaPerformance;
import com.neritech.saas.relatorios.dto.MetricaPerformanceRequest;
import com.neritech.saas.relatorios.dto.MetricaPerformanceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MetricaPerformanceMapper {
    @Mapping(target = "id", ignore = true)
    MetricaPerformance toEntity(MetricaPerformanceRequest request);

    MetricaPerformanceResponse toResponse(MetricaPerformance entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    void updateEntity(@MappingTarget MetricaPerformance entity, MetricaPerformanceRequest request);
}
