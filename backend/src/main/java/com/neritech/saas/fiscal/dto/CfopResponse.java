package com.neritech.saas.fiscal.dto;

import java.time.LocalDateTime;

public record CfopResponse(
        Long id,
        String codigo,
        String descricao,
        String aplicacao,
        boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
