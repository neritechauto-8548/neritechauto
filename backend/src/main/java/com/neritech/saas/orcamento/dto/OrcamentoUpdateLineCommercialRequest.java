package com.neritech.saas.orcamento.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OrcamentoUpdateLineCommercialRequest(
        @NotNull Long expectedRevision,
        @NotNull @DecimalMin("0.001") @Digits(integer = 9, fraction = 3) BigDecimal quantity,
        @DecimalMin("0.00") @Digits(integer = 10, fraction = 4) BigDecimal unitPrice,
        @Size(max = 500) String priceOverrideReason,
        @NotNull @Pattern(regexp = "NONE|FIXED|PERCENT") String discountType,
        @DecimalMin("0.00") @Digits(integer = 10, fraction = 4) BigDecimal discountValue,
        @Size(max = 500) String discountReason) {
}

