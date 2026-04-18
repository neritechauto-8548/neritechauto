package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.TipoItemFatura;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ItemFaturaResponse(
        Long id,
        TipoItemFatura tipoItem,
        Long servicoId,
        Long produtoId,
        String descricao,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal,
        BigDecimal descontoPercentual,
        BigDecimal descontoValor,
        BigDecimal valorLiquido,
        String ncm,
        String cfop,
        String cstIcms,
        String cstPis,
        String cstCofins,
        BigDecimal aliquotaIcms,
        BigDecimal aliquotaPis,
        BigDecimal aliquotaCofins,
        BigDecimal valorIcms,
        BigDecimal valorPis,
        BigDecimal valorCofins,
        String observacoes,
        Integer ordemItem,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
