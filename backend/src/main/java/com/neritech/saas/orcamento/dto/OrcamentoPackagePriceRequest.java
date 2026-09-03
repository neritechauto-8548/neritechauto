package com.neritech.saas.orcamento.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OrcamentoPackagePriceRequest(
        @NotNull Long expectedRevision,
        @DecimalMin("0.00") @Digits(integer = 12, fraction = 2) BigDecimal packagePrice,
        @Pattern(regexp = "WEIGHTED|LABOR_FIRST|POLICY") String distributionMethod,
        Long priceSourceId,
        Integer priceSourceVersion,
        @Size(max = 500) String overrideReason) {
}

