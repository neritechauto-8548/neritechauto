package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.Pagamento;
import com.neritech.saas.financeiro.dto.PagamentoRequest;
import com.neritech.saas.financeiro.dto.PagamentoResponse;
import com.neritech.saas.empresa.domain.enums.FormaPagamento;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {
        ParcelaPagamentoMapper.class })
public interface PagamentoMapper {
    @Mapping(target = "formaPagamento", ignore = true)
    @Mapping(target = "condicaoPagamento", ignore = true)
    @Mapping(target = "contaBancaria", ignore = true)
    @Mapping(target = "fatura", ignore = true)
    @Mapping(target = "parcelas", ignore = true)
    Pagamento toEntity(PagamentoRequest request);

    @Mapping(target = "faturaNumero", source = "fatura.numeroFatura")
    PagamentoResponse toResponse(Pagamento entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "formaPagamento", ignore = true)
    @Mapping(target = "condicaoPagamento", ignore = true)
    @Mapping(target = "contaBancaria", ignore = true)
    @Mapping(target = "fatura", ignore = true)
    @Mapping(target = "parcelas", ignore = true)
    void updateEntityFromDTO(PagamentoRequest request, @MappingTarget Pagamento entity);
}
