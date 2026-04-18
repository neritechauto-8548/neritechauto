package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.ContaBancaria;
import com.neritech.saas.financeiro.dto.ContaBancariaRequest;
import com.neritech.saas.financeiro.dto.ContaBancariaResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContaBancariaMapper {
    ContaBancaria toEntity(ContaBancariaRequest request);

    ContaBancariaResponse toResponse(ContaBancaria entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ContaBancariaRequest request, @MappingTarget ContaBancaria entity);
}
