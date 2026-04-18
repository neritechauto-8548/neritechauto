package com.neritech.saas.ordemservico.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChecklistRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 255) String dsChecklist,
        @Size(max = 500) String urlImagem
) {}

