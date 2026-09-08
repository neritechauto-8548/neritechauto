package com.neritech.saas.orcamento.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OrcamentoDiscountDecisionRequest(
        @NotNull Long expectedRevision,
        @NotNull @Pattern(regexp = "APPROVE|REJECT") String decision,
        @NotNull @Size(min = 8, max = 500) String reason) {
}

