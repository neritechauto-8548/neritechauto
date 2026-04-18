package com.neritech.saas.fiscal.dto;

import java.time.LocalDateTime;

public record CstProdutoResponse(
        Long id,
        String codigo,
        String descricao,
        String tipo,
        boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
