package com.neritech.saas.fiscal.dto;

import java.time.LocalDateTime;

public record RegimeTributarioResponse(
        Long id,
        String codigo,
        String nome,
        String descricao,
        boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
