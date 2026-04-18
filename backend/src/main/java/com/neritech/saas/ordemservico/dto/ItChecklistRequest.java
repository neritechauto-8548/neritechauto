package com.neritech.saas.ordemservico.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItChecklistRequest(
        @NotNull Long checkListId,
        @NotBlank @Size(max = 255) String dsItChecklist
) {}

