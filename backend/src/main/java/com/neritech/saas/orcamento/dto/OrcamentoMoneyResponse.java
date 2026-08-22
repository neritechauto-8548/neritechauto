package com.neritech.saas.orcamento.dto;

import java.math.BigDecimal;

public record OrcamentoMoneyResponse(
        String currency,
        BigDecimal amount) {

    public static OrcamentoMoneyResponse brl(BigDecimal amount) {
        return new OrcamentoMoneyResponse("BRL", amount != null ? amount : BigDecimal.ZERO);
    }
}
