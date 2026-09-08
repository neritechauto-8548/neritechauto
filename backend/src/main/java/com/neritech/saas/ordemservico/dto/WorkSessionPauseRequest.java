package com.neritech.saas.ordemservico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WorkSessionPauseRequest(
        @NotBlank(message = "Motivo da pausa é obrigatório") String reason,
        @Size(max = 500, message = "Observação da pausa deve possuir no máximo 500 caracteres") String note) {
}
