package com.neritech.saas.orcamento.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrcamentoUpdateLineRequest(
        @NotNull Long expectedRevision,
        @NotNull @DecimalMin("0.001") @Digits(integer = 9, fraction = 3) BigDecimal quantity) {
}

