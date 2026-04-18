package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.StatusPagamento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ParcelaPagamentoResponse(
        Long id,
        Integer numeroParcela,
        LocalDate dataVencimento,
        BigDecimal valorParcela,
        BigDecimal valorJuros,
        BigDecimal valorMulta,
        BigDecimal valorDesconto,
        BigDecimal valorPago,
        LocalDate dataPagamento,
        StatusPagamento status,
        String observacoes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
