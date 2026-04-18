package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.Fatura;
import com.neritech.saas.financeiro.dto.FaturaRequest;
import com.neritech.saas.financeiro.dto.FaturaResponse;
import com.neritech.saas.empresa.domain.enums.FormaPagamento;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { ItemFaturaMapper.class })
public interface FaturaMapper {
    @Mapping(target = "formaPagamento", ignore = true)
    @Mapping(target = "condicaoPagamento", ignore = true)
    Fatura toEntity(FaturaRequest request);

    FaturaResponse toResponse(Fatura entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "formaPagamento", ignore = true)
    @Mapping(target = "condicaoPagamento", ignore = true)
    void updateEntityFromDTO(FaturaRequest request, @MappingTarget Fatura entity);
}
