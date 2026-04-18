package com.neritech.saas.integracao.dto;

import java.time.LocalDateTime;

public record IntegracaoAtivaResponse(
        Long id,
        String nome,
        String tipo,
        String urlBase,
        String descricao,
        boolean ativo,
        String configuracoes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
