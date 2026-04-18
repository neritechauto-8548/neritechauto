package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.ParametroRelatorio;
import com.neritech.saas.relatorios.dto.ParametroRelatorioRequest;
import com.neritech.saas.relatorios.dto.ParametroRelatorioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ParametroRelatorioMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "relatorio", ignore = true) // Set manually in service
    @Mapping(target = "dependenteDe", ignore = true) // Set manually in service
    ParametroRelatorio toEntity(ParametroRelatorioRequest request);

    @Mapping(target = "relatorioId", source = "relatorio.id")
    @Mapping(target = "dependenteDeId", source = "dependenteDe.id")
    ParametroRelatorioResponse toResponse(ParametroRelatorio entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "relatorio", ignore = true)
    @Mapping(target = "dependenteDe", ignore = true)
    void updateEntity(@MappingTarget ParametroRelatorio entity, ParametroRelatorioRequest request);
}
