package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.FormaPagamento;
import com.neritech.saas.financeiro.dto.FormaPagamentoRequest;
import com.neritech.saas.financeiro.dto.FormaPagamentoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FormaPagamentoMapper {
    @Mapping(target = "contaBancaria", ignore = true)
    FormaPagamento toEntity(FormaPagamentoRequest request);

    @Mapping(target = "contaBancariaNome", source = "contaBancaria.bancoNome")
    FormaPagamentoResponse toResponse(FormaPagamento entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "contaBancaria", ignore = true)
    void updateEntityFromDTO(FormaPagamentoRequest request, @MappingTarget FormaPagamento entity);
}
