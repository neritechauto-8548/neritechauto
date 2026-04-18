package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.FluxoCaixa;
import com.neritech.saas.financeiro.dto.FluxoCaixaRequest;
import com.neritech.saas.financeiro.dto.FluxoCaixaResponse;
import com.neritech.saas.empresa.domain.enums.FormaPagamento;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FluxoCaixaMapper {
    @Mapping(target = "contaBancaria", ignore = true)
    @Mapping(target = "centroCusto", ignore = true)
    @Mapping(target = "formaPagamento", ignore = true)
    @Mapping(target = "contaDestino", ignore = true)
    @Mapping(target = "dataMovimentacao", source = "dataMovimento")
    FluxoCaixa toEntity(FluxoCaixaRequest request);

    @Mapping(target = "contaBancariaNome", source = "contaBancaria.bancoNome")
    @Mapping(target = "centroCustoNome", source = "centroCusto.nome")
    @Mapping(target = "planoContasId", ignore = true)
    @Mapping(target = "planoContasNome", ignore = true)
    @Mapping(target = "pagamentoId", expression = "java(getPagamentoId(entity))")
    @Mapping(target = "recebimentoId", expression = "java(getRecebimentoId(entity))")
    @Mapping(target = "dataMovimento", source = "dataMovimentacao")
    FluxoCaixaResponse toResponse(FluxoCaixa entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "contaBancaria", ignore = true)
    @Mapping(target = "centroCusto", ignore = true)
    @Mapping(target = "formaPagamento", ignore = true)
    @Mapping(target = "contaDestino", ignore = true)
    @Mapping(target = "dataMovimentacao", source = "dataMovimento")
    void updateEntityFromDTO(FluxoCaixaRequest request, @MappingTarget FluxoCaixa entity);

    default Long getPagamentoId(FluxoCaixa entity) {
        if (entity == null) return null;
        return entity.getTipoMovimentacao() == com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.SAIDA
                ? entity.getDocumentoId() : null;
    }

    default Long getRecebimentoId(FluxoCaixa entity) {
        if (entity == null) return null;
        return entity.getTipoMovimentacao() == com.neritech.saas.financeiro.domain.enums.TipoMovimentacao.ENTRADA
                ? entity.getDocumentoId() : null;
    }
}
