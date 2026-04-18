package com.neritech.saas.integracao.dto;

import jakarta.validation.constraints.NotBlank;

public record WebhookRequest(
        @NotBlank(message = "O nome Ã© obrigatÃ³rio") String nome,

        @NotBlank(message = "A URL de destino Ã© obrigatÃ³ria") String urlDestino,

        @NotBlank(message = "O evento Ã© obrigatÃ³rio") String evento,

        String metodoHttp,
        String headers,
        String secretKey,
        boolean ativo,
        Integer tentativasMaximas) {
}
