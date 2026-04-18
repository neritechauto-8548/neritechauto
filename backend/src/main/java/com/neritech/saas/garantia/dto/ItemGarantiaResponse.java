package com.neritech.saas.garantia.dto;

import com.neritech.saas.garantia.domain.TipoItemGarantia;
import java.math.BigDecimal;

/**
 * DTO de resposta para ItemGarantia
 */
public record ItemGarantiaResponse(
        Long id,
        Long garantiaId,
        TipoItemGarantia tipoItem,
        Long servicoId,
        Long produtoId,
        String descricaoItem,
        BigDecimal quantidadeOriginal,
        BigDecimal valorUnitarioOriginal,
        BigDecimal valorTotalOriginal,
        BigDecimal percentualCobertura,
        BigDecimal valorCobertura,
        String condicoesEspecificas,
        String defeitoCoberto,
        String defeitoNaoCoberto,
        Integer prazoAcionamentoDias,
        BigDecimal quantidadeUtilizada,
        BigDecimal valorUtilizado,
        BigDecimal saldoDisponivel,
        String observacoes,
        Boolean ativo) {
}
