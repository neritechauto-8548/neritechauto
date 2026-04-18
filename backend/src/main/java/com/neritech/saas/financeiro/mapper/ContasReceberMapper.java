package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.ContasReceber;
import com.neritech.saas.financeiro.dto.ContasReceberRequest;
import com.neritech.saas.financeiro.dto.ContasReceberResponse;
import com.neritech.saas.financeiro.domain.FormaPagamento;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContasReceberMapper {
    @Mapping(target = "formaPagamento", ignore = true)
    @Mapping(target = "contaBancaria", ignore = true)
    @Mapping(target = "centroCusto", ignore = true)
    @Mapping(target = "planoContas", ignore = true)
    @Mapping(target = "valorNominal", source = "valorOriginal")
    ContasReceber toEntity(ContasReceberRequest request);

    @Mapping(target = "faturaNumero", ignore = true) // Need to fetch if faturaId is present
    @Mapping(target = "formaPagamentoNome", source = "formaPagamento.nome")
    @Mapping(target = "contaBancariaNome", source = "contaBancaria.bancoNome")
    @Mapping(target = "centroCustoNome", source = "centroCusto.nome")
    @Mapping(target = "planoContasNome", source = "planoContas.nome")
    ContasReceberResponse toResponse(ContasReceber entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "formaPagamento", ignore = true)
    @Mapping(target = "contaBancaria", ignore = true)
    @Mapping(target = "centroCusto", ignore = true)
    @Mapping(target = "planoContas", ignore = true)
    @Mapping(target = "valorNominal", source = "valorOriginal")
    void updateEntityFromDTO(ContasReceberRequest request, @MappingTarget ContasReceber entity);
}
