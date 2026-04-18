package com.neritech.saas.financeiro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoContabilRequest(
                @NotNull(message = "Data de lanÃ§amento Ã© obrigatÃ³ria") LocalDate dataLancamento,

                @NotBlank(message = "NÃºmero do lanÃ§amento Ã© obrigatÃ³rio") String numeroLancamento,

                @NotBlank(message = "DescriÃ§Ã£o Ã© obrigatÃ³ria") String descricao,

                @NotNull(message = "Conta dÃ©bito Ã© obrigatÃ³ria") Long contaDebitoId,

                @NotNull(message = "Conta crÃ©dito Ã© obrigatÃ³ria") Long contaCreditoId,

                @NotNull(message = "Valor Ã© obrigatÃ³rio") BigDecimal valor,

                Long centroCustoId,
                String historico,
                String documentoReferencia) {
}
