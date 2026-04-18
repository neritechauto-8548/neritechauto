package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.TipoComissao;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ComissaoFuncionarioRequest(
        @NotNull(message = "FuncionÃ¡rio Ã© obrigatÃ³rio") Long funcionarioId,

        Long faturaId,
        Long itemFaturaId,

        @NotNull(message = "Data de referÃªncia Ã© obrigatÃ³ria") LocalDate dataReferencia,

        @NotNull(message = "Valor base Ã© obrigatÃ³rio") BigDecimal valorBase,

        @NotNull(message = "Percentual Ã© obrigatÃ³rio") BigDecimal percentualComissao,

        @NotNull(message = "Valor da comissÃ£o Ã© obrigatÃ³rio") BigDecimal valorComissao,

        TipoComissao tipoComissao,
        Boolean pago,
        LocalDate dataPagamento,
        String observacoes) {
}
