package com.neritech.saas.rh.dto;

import com.neritech.saas.financeiro.domain.enums.StatusPagamento;
import com.neritech.saas.financeiro.domain.enums.TipoComissao;
import com.neritech.saas.rh.domain.*;
import com.neritech.saas.rh.domain.enums.BaseCalculoComissao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ComissaoResponse(
        Long id,
        Long empresaId,
        Long funcionarioId,
        TipoComissao tipoComissao,
        String periodoReferencia,
        BaseCalculoComissao baseCalculo,
        BigDecimal valorBase,
        BigDecimal percentualComissao,
        BigDecimal valorComissao,
        BigDecimal metaEstabelecida,
        BigDecimal metaAtingida,
        BigDecimal percentualMetaAtingido,
        BigDecimal bonusMeta,
        BigDecimal descontoRetrabalho,
        BigDecimal valorLiquido,
        LocalDate dataCompetencia,
        LocalDate dataPagamento,
        StatusPagamento statusPagamento,
        String observacoes,
        Long aprovadaPor,
        LocalDateTime dataAprovacao,
        Long pagaPor,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
