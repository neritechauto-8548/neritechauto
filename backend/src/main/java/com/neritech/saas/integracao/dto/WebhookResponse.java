package com.neritech.saas.integracao.dto;

import java.time.LocalDateTime;

public record WebhookResponse(
        Long id,
        String nome,
        String urlDestino,
        String evento,
        String metodoHttp,
        String headers,
        boolean ativo,
        Integer tentativasMaximas,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
