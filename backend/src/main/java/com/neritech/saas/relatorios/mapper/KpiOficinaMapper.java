package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.KpiOficina;
import com.neritech.saas.relatorios.dto.KpiOficinaRequest;
import com.neritech.saas.relatorios.dto.KpiOficinaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface KpiOficinaMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataProximaAtualizacao", ignore = true)
    KpiOficina toEntity(KpiOficinaRequest request);

    KpiOficinaResponse toResponse(KpiOficina entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    void updateEntity(@MappingTarget KpiOficina entity, KpiOficinaRequest request);
}
