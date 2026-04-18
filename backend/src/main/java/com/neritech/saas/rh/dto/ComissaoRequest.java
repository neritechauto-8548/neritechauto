package com.neritech.saas.rh.dto;

import com.neritech.saas.financeiro.domain.enums.StatusPagamento;
import com.neritech.saas.financeiro.domain.enums.TipoComissao;
import com.neritech.saas.rh.domain.*;
import com.neritech.saas.rh.domain.enums.BaseCalculoComissao;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ComissaoRequest(
        @NotNull Long empresaId,
        @NotNull Long funcionarioId,
        TipoComissao tipoComissao,
        @NotBlank @Size(max = 7) String periodoReferencia,
        BaseCalculoComissao baseCalculo,
        @NotNull BigDecimal valorBase,
        @NotNull BigDecimal percentualComissao,
        @NotNull BigDecimal valorComissao,
        BigDecimal metaEstabelecida,
        BigDecimal metaAtingida,
        BigDecimal percentualMetaAtingido,
        BigDecimal bonusMeta,
        BigDecimal descontoRetrabalho,
        BigDecimal valorLiquido,
        @NotNull LocalDate dataCompetencia,
        LocalDate dataPagamento,
        StatusPagamento statusPagamento,
        String observacoes) {
}
