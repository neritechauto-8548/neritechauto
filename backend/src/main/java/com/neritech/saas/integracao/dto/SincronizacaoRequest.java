package com.neritech.saas.integracao.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record SincronizacaoRequest(
        Long integracaoId,

        @NotBlank(message = "A entidade Ã© obrigatÃ³ria") String entidade,

        @NotBlank(message = "A direÃ§Ã£o Ã© obrigatÃ³ria") String direcao,

        @NotBlank(message = "O status Ã© obrigatÃ³rio") String status,

        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Integer registrosProcessados,
        Integer registrosComErro,
        String logErros) {
}
