package com.neritech.saas.garantia.mapper;

import com.neritech.saas.garantia.domain.ItemGarantia;
import com.neritech.saas.garantia.dto.ItemGarantiaRequest;
import com.neritech.saas.garantia.dto.ItemGarantiaResponse;
import org.mapstruct.*;

/**
 * Mapper MapStruct para ItemGarantia
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemGarantiaMapper {

    @Mapping(target = "garantia", ignore = true)
    ItemGarantia toEntity(ItemGarantiaRequest request);

    ItemGarantiaResponse toResponse(ItemGarantia entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "garantia", ignore = true)
    void updateEntityFromDTO(ItemGarantiaRequest request, @MappingTarget ItemGarantia entity);
}
