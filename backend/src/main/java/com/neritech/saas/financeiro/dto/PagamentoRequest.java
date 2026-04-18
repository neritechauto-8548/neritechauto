package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.StatusPagamento;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PagamentoRequest(
        Long faturaId,
        Long fornecedorId,
        Long clienteId,

        @NotNull(message = "Forma de pagamento Ã© obrigatÃ³ria") Long formaPagamentoId,

        Long condicaoPagamentoId,
        Long contaBancariaId,

        @NotNull(message = "Data de pagamento Ã© obrigatÃ³ria") LocalDate dataPagamento,

        @NotNull(message = "Valor original Ã© obrigatÃ³rio") BigDecimal valorOriginal,

        BigDecimal valorDesconto,
        BigDecimal valorJuros,
        BigDecimal valorMulta,

        @NotNull(message = "Valor total Ã© obrigatÃ³rio") BigDecimal valorTotal,

        StatusPagamento status,
        String comprovanteUrl,
        String observacoes,

        List<ParcelaPagamentoRequest> parcelas) {
}
