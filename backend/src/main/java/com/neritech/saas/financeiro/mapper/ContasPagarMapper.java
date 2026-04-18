package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.ContasPagar;
import com.neritech.saas.financeiro.dto.ContasPagarRequest;
import com.neritech.saas.financeiro.dto.ContasPagarResponse;
import com.neritech.saas.empresa.domain.enums.FormaPagamento;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContasPagarMapper {
    @Mapping(target = "formaPagamento", ignore = true)
    @Mapping(target = "contaBancaria", ignore = true)
    @Mapping(target = "centroCusto", ignore = true)
    @Mapping(target = "planoContas", ignore = true)
    ContasPagar toEntity(ContasPagarRequest request);

    ContasPagarResponse toResponse(ContasPagar entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "formaPagamento", ignore = true)
    @Mapping(target = "contaBancaria", ignore = true)
    @Mapping(target = "centroCusto", ignore = true)
    @Mapping(target = "planoContas", ignore = true)
    void updateEntityFromDTO(ContasPagarRequest request, @MappingTarget ContasPagar entity);
}
