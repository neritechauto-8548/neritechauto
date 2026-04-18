package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.CentroCusto;
import com.neritech.saas.financeiro.dto.CentroCustoRequest;
import com.neritech.saas.financeiro.dto.CentroCustoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CentroCustoMapper {
    @Mapping(target = "centroCustoPai", ignore = true)
    CentroCusto toEntity(CentroCustoRequest request);

    @Mapping(target = "centroCustoPaiNome", source = "centroCustoPai.nome")
    CentroCustoResponse toResponse(CentroCusto entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "centroCustoPai", ignore = true)
    void updateEntityFromDTO(CentroCustoRequest request, @MappingTarget CentroCusto entity);
}
