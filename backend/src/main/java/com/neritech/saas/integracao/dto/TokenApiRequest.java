package com.neritech.saas.integracao.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record TokenApiRequest(
        Long integracaoId,

        @NotBlank(message = "O nome Ã© obrigatÃ³rio") String nome,

        @NotBlank(message = "O token Ã© obrigatÃ³rio") String token,

        String tipo,
        LocalDateTime dataExpiracao,
        boolean ativo,
        String escopos) {
}
