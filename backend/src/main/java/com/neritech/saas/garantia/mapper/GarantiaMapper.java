package com.neritech.saas.garantia.mapper;

import com.neritech.saas.garantia.domain.Garantia;
import com.neritech.saas.garantia.dto.GarantiaRequest;
import com.neritech.saas.garantia.dto.GarantiaResponse;
import org.mapstruct.*;

/**
 * Mapper MapStruct para Garantia
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GarantiaMapper {

    @Mapping(target = "tipoGarantia", ignore = true)
    @Mapping(target = "ordemServico", ignore = true)
    @Mapping(target = "renovadaDeGarantia", ignore = true)
    Garantia toEntity(GarantiaRequest request);

    @Mapping(target = "tipoGarantiaNome", source = "tipoGarantia.nome")
    GarantiaResponse toResponse(Garantia entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tipoGarantia", ignore = true)
    @Mapping(target = "ordemServico", ignore = true)
    @Mapping(target = "renovadaDeGarantia", ignore = true)
    void updateEntityFromDTO(GarantiaRequest request, @MappingTarget Garantia entity);
}
