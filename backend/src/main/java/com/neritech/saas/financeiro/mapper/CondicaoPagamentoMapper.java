package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.CondicaoPagamento;
import com.neritech.saas.financeiro.dto.CondicaoPagamentoRequest;
import com.neritech.saas.financeiro.dto.CondicaoPagamentoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CondicaoPagamentoMapper {
    CondicaoPagamento toEntity(CondicaoPagamentoRequest request);

    CondicaoPagamentoResponse toResponse(CondicaoPagamento entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(CondicaoPagamentoRequest request, @MappingTarget CondicaoPagamento entity);
}
