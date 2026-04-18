package com.neritech.saas.relatorios.dto;

import java.math.BigDecimal;

public record RelatorioItemDTO(
    String descricao,
    BigDecimal quantidade,
    BigDecimal valorUnitario,
    BigDecimal valorTotal,
    String tipo // "PRODUTO" ou "SERVICO"
) {}
