package com.neritech.saas.orcamento.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record OrcamentoInstantiateKitRequest(
        @NotNull Long expectedRevision,
        @NotNull @DecimalMin("0.001") @Digits(integer = 9, fraction = 3) BigDecimal quantity,
        @PositiveOrZero Integer targetPosition) {
}
