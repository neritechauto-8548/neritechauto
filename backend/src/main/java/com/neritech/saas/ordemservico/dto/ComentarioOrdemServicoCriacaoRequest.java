package com.neritech.saas.ordemservico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ComentarioOrdemServicoCriacaoRequest(
        @NotBlank @Size(max = 2000) String conteudo) {
}
