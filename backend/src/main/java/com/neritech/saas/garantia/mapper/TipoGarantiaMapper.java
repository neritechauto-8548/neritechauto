package com.neritech.saas.garantia.mapper;

import com.neritech.saas.garantia.domain.TipoGarantia;
import com.neritech.saas.garantia.dto.TipoGarantiaRequest;
import com.neritech.saas.garantia.dto.TipoGarantiaResponse;
import org.mapstruct.*;

/**
 * Mapper MapStruct para TipoGarantia
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TipoGarantiaMapper {

    TipoGarantia toEntity(TipoGarantiaRequest request);

    TipoGarantiaResponse toResponse(TipoGarantia entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(TipoGarantiaRequest request, @MappingTarget TipoGarantia entity);
}
