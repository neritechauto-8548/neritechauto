package com.neritech.saas.empresa.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SetorRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 255) String nome,
        Boolean ativo
) {}

