package com.neritech.saas.integracao.dto;

import java.time.LocalDateTime;

public record SincronizacaoResponse(
        Long id,
        IntegracaoAtivaResponse integracao,
        String entidade,
        String direcao,
        String status,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Integer registrosProcessados,
        Integer registrosComErro,
        String logErros,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
