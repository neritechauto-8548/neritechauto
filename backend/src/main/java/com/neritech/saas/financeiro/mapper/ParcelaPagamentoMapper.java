package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.ParcelaPagamento;
import com.neritech.saas.financeiro.dto.ParcelaPagamentoRequest;
import com.neritech.saas.financeiro.dto.ParcelaPagamentoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParcelaPagamentoMapper {
    @Mapping(target = "pagamento", ignore = true)
    ParcelaPagamento toEntity(ParcelaPagamentoRequest request);

    ParcelaPagamentoResponse toResponse(ParcelaPagamento entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "pagamento", ignore = true)
    void updateEntityFromDTO(ParcelaPagamentoRequest request, @MappingTarget ParcelaPagamento entity);
}
