package com.neritech.saas.orcamento.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrcamentoReorderRequest(
        @NotNull Long expectedRevision,
        @NotEmpty List<@Valid @NotNull @Positive Long> orderedIds) {
}

