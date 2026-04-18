package com.neritech.saas.financeiro.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record LancamentoContabilResponse(
        Long id,
        Long empresaId,
        LocalDate dataLancamento,
        String numeroLancamento,
        String descricao,
        Long contaDebitoId,
        String contaDebitoNome,
        Long contaCreditoId,
        String contaCreditoNome,
        BigDecimal valor,
        Long centroCustoId,
        String centroCustoNome,
        String historico,
        String documentoReferencia,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
