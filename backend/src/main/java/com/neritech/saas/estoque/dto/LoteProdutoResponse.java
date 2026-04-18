package com.neritech.saas.estoque.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoteProdutoResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        String numeroLote,
        LocalDate dataFabricacao,
        LocalDate dataValidade,
        BigDecimal quantidadeInicial) {
}
