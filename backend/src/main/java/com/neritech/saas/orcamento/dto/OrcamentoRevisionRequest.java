package com.neritech.saas.orcamento.dto;

import jakarta.validation.constraints.NotNull;

public record OrcamentoRevisionRequest(@NotNull Long expectedRevision) {
}

