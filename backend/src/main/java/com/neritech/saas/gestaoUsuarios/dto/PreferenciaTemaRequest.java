package com.neritech.saas.gestaoUsuarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PreferenciaTemaRequest(
        @NotBlank
        @Pattern(regexp = "light|dark|auto", message = "Tema inválido")
        String tema) {
}
