package com.neritech.saas.fiscal.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AliquotaImpostoResponse(
        Long id,
        String nomeImposto,
        BigDecimal aliquota,
        String descricao,
        String uf,
        boolean padrao,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
