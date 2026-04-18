package com.neritech.saas.integracao.dto;

import java.time.LocalDateTime;

public record MapeamentoDadosResponse(
        Long id,
        IntegracaoAtivaResponse integracao,
        String entidade,
        String campoOrigem,
        String campoDestino,
        String transformacao,
        String regrasTransformacao,
        boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
