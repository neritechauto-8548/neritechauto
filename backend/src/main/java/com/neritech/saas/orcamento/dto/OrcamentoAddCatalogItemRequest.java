package com.neritech.saas.orcamento.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record OrcamentoAddCatalogItemRequest(
        @NotNull Long expectedRevision,
        @NotNull @Pattern(regexp = "PART|LABOR") String lineType,
        @NotNull Long catalogItemId,
        @NotNull @DecimalMin("0.001") @DecimalMax("999999.999") @Digits(integer = 6, fraction = 3)
        BigDecimal quantity) {
}
