package com.neritech.saas.integracao.dto;

import java.time.LocalDateTime;

public record LogIntegracaoResponse(
        Long id,
        IntegracaoAtivaResponse integracao,
        String operacao,
        String status,
        String requisicao,
        String resposta,
        String mensagemErro,
        Integer codigoHttp,
        LocalDateTime dataExecucao,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
