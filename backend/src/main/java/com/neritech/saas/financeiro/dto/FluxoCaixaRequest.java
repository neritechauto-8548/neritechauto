package com.neritech.saas.financeiro.dto;


import com.neritech.saas.financeiro.domain.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FluxoCaixaRequest(
        @NotNull(message = "Data de movimento Ã© obrigatÃ³ria") LocalDate dataMovimento,

        @NotBlank(message = "DescriÃ§Ã£o Ã© obrigatÃ³ria") String descricao,

        @NotNull(message = "Tipo de movimentaÃ§Ã£o Ã© obrigatÃ³rio") TipoMovimentacao tipoMovimentacao,

        @NotNull(message = "Valor Ã© obrigatÃ³rio") BigDecimal valor,

        BigDecimal saldoAcumulado,
        Long contaBancariaId,
        Long centroCustoId,
        Long planoContasId,
        Long pagamentoId,
        Long recebimentoId, // Assuming ContasReceber ID
        String observacoes) {
}
