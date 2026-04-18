package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.StatusPagamento;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ParcelaPagamentoRequest(
        @NotNull(message = "NÃºmero da parcela Ã© obrigatÃ³rio") Integer numeroParcela,

        @NotNull(message = "Data de vencimento Ã© obrigatÃ³ria") LocalDate dataVencimento,

        @NotNull(message = "Valor da parcela Ã© obrigatÃ³rio") BigDecimal valorParcela,

        BigDecimal valorJuros,
        BigDecimal valorMulta,
        BigDecimal valorDesconto,
        BigDecimal valorPago,
        LocalDate dataPagamento,
        StatusPagamento status,
        String observacoes) {
}
