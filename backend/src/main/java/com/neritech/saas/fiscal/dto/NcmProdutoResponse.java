package com.neritech.saas.fiscal.dto;

import java.time.LocalDateTime;

public record NcmProdutoResponse(
        Long id,
        String codigo,
        String descricao,
        Double aliquotaIpi,
        boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
