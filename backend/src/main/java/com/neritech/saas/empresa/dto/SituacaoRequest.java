package com.neritech.saas.empresa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SituacaoRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 255) String nmSituacao,
        @Size(max = 1000) String dsSituacao,
        @Size(max = 7) String corSituacao
) {}

