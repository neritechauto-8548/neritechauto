package com.neritech.saas.integracao.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record LogIntegracaoRequest(
        Long integracaoId,

        @NotBlank(message = "A operaÃ§Ã£o Ã© obrigatÃ³ria") String operacao,

        @NotBlank(message = "O status Ã© obrigatÃ³rio") String status,

        String requisicao,
        String resposta,
        String mensagemErro,
        Integer codigoHttp,
        LocalDateTime dataExecucao) {
}
