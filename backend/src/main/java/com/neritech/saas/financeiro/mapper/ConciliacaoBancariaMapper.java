package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.ConciliacaoBancaria;
import com.neritech.saas.financeiro.dto.ConciliacaoBancariaRequest;
import com.neritech.saas.financeiro.dto.ConciliacaoBancariaResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConciliacaoBancariaMapper {
    @Mapping(target = "contaBancaria", ignore = true)
    ConciliacaoBancaria toEntity(ConciliacaoBancariaRequest request);

    @Mapping(target = "contaBancariaNome", source = "contaBancaria.bancoNome")
    ConciliacaoBancariaResponse toResponse(ConciliacaoBancaria entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "contaBancaria", ignore = true)
    void updateEntityFromDTO(ConciliacaoBancariaRequest request, @MappingTarget ConciliacaoBancaria entity);
}
