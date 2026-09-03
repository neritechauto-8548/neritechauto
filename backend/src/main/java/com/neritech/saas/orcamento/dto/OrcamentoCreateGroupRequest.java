package com.neritech.saas.orcamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OrcamentoCreateGroupRequest(
        @NotNull Long expectedRevision,
        @NotBlank @Size(min = 3, max = 120) String title,
        @Size(max = 2000) String customerDescription,
        boolean recommended,
        @Pattern(regexp = "CUSTOMER_VISIBLE|INTERNAL_ONLY") String visibility) {
}
