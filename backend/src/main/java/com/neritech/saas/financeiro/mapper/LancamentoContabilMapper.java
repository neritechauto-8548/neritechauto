package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.LancamentoContabil;
import com.neritech.saas.financeiro.dto.LancamentoContabilRequest;
import com.neritech.saas.financeiro.dto.LancamentoContabilResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LancamentoContabilMapper {
    @Mapping(target = "contaDebito", ignore = true)
    @Mapping(target = "contaCredito", ignore = true)
    @Mapping(target = "centroCusto", ignore = true)
    LancamentoContabil toEntity(LancamentoContabilRequest request);

    @Mapping(target = "contaDebitoNome", source = "contaDebito.nome")
    @Mapping(target = "contaCreditoNome", source = "contaCredito.nome")
    @Mapping(target = "centroCustoNome", source = "centroCusto.nome")
    LancamentoContabilResponse toResponse(LancamentoContabil entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "contaDebito", ignore = true)
    @Mapping(target = "contaCredito", ignore = true)
    @Mapping(target = "centroCusto", ignore = true)
    void updateEntityFromDTO(LancamentoContabilRequest request, @MappingTarget LancamentoContabil entity);
}
