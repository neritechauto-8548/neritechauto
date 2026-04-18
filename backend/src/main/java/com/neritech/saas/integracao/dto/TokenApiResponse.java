package com.neritech.saas.integracao.dto;

import java.time.LocalDateTime;

public record TokenApiResponse(
        Long id,
        IntegracaoAtivaResponse integracao,
        String nome,
        String tipo,
        LocalDateTime dataExpiracao,
        boolean ativo,
        String escopos,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
