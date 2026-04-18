package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.TipoCentroCusto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CentroCustoResponse(
        Long id,
        Long empresaId,
        String codigo,
        String nome,
        String descricao,
        Long centroCustoPaiId,
        String centroCustoPaiNome,
        TipoCentroCusto tipo,
        Long responsavelId,
        BigDecimal orcamentoMensal,
        BigDecimal orcamentoAnual,
        Boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
