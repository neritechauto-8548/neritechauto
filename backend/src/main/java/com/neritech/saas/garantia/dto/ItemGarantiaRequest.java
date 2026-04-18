package com.neritech.saas.garantia.dto;

import com.neritech.saas.garantia.domain.TipoItemGarantia;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO de requisiÃ§Ã£o para ItemGarantia
 */
public record ItemGarantiaRequest(
        @NotNull(message = "Garantia Ã© obrigatÃ³ria") Long garantiaId,

        @NotNull(message = "Tipo de item Ã© obrigatÃ³rio") TipoItemGarantia tipoItem,

        Long servicoId,
        Long produtoId,

        @NotBlank(message = "DescriÃ§Ã£o do item Ã© obrigatÃ³ria") String descricaoItem,

        @NotNull(message = "Quantidade original Ã© obrigatÃ³ria") @Positive(message = "Quantidade deve ser positiva") BigDecimal quantidadeOriginal,

        @NotNull(message = "Valor unitÃ¡rio Ã© obrigatÃ³rio") @PositiveOrZero(message = "Valor deve ser positivo ou zero") BigDecimal valorUnitarioOriginal,

        @NotNull(message = "Valor total Ã© obrigatÃ³rio") @PositiveOrZero(message = "Valor deve ser positivo ou zero") BigDecimal valorTotalOriginal,

        @DecimalMin(value = "0.0", message = "Percentual deve ser maior ou igual a 0") @DecimalMax(value = "100.0", message = "Percentual deve ser menor ou igual a 100") BigDecimal percentualCobertura,

        @NotNull(message = "Valor de cobertura Ã© obrigatÃ³rio") @PositiveOrZero(message = "Valor deve ser positivo ou zero") BigDecimal valorCobertura,

        String condicoesEspecificas,
        String defeitoCoberto,
        String defeitoNaoCoberto,

        @Positive(message = "Prazo deve ser positivo") Integer prazoAcionamentoDias,

        Boolean ativo) {
}
