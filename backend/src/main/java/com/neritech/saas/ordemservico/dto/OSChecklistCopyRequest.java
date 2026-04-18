package com.neritech.saas.ordemservico.dto;

import jakarta.validation.constraints.NotNull;

public record OSChecklistCopyRequest(
        @NotNull Long ordemServicoId,
        @NotNull Long checklistId
) {}
