package com.neritech.saas.estoque.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LoteProdutoRequest(
        @NotNull Long produtoId,
        @NotNull String numeroLote,
        LocalDate dataFabricacao,
        LocalDate dataValidade,
        @NotNull BigDecimal quantidadeInicial) {
}
