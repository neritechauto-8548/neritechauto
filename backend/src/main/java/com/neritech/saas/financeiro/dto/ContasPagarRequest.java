package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.StatusTitulo;
import com.neritech.saas.financeiro.domain.enums.TipoTitulo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ContasPagarRequest(
        @NotBlank(message = "DescriÃ§Ã£o Ã© obrigatÃ³ria") String descricao,

        @NotNull(message = "Fornecedor Ã© obrigatÃ³rio") Long fornecedorId,

        String numeroDocumento,

        @NotNull(message = "Data de emissÃ£o Ã© obrigatÃ³ria") LocalDate dataEmissao,

        @NotNull(message = "Data de vencimento Ã© obrigatÃ³ria") LocalDate dataVencimento,

        LocalDate dataPagamento,
        LocalDate dataAgendamento,

        @NotNull(message = "Valor original Ã© obrigatÃ³rio") BigDecimal valorOriginal,

        BigDecimal valorPago,
        BigDecimal valorJuros,
        BigDecimal valorMulta,
        BigDecimal valorDesconto,
        BigDecimal saldoDevedor,

        StatusTitulo status,
        TipoTitulo tipoTitulo,
        Long formaPagamentoId,
        Long contaBancariaId,
        Long centroCustoId,
        Long planoContasId,
        String numeroTitulo,
        String codigoBarras,
        String observacoes) {
}
