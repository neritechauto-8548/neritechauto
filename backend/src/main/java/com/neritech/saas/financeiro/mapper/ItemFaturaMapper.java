package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.ItemFatura;
import com.neritech.saas.financeiro.dto.ItemFaturaRequest;
import com.neritech.saas.financeiro.dto.ItemFaturaResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemFaturaMapper {
    @Mapping(target = "fatura", ignore = true)
    ItemFatura toEntity(ItemFaturaRequest request);

    ItemFaturaResponse toResponse(ItemFatura entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "fatura", ignore = true)
    void updateEntityFromDTO(ItemFaturaRequest request, @MappingTarget ItemFatura entity);
}
