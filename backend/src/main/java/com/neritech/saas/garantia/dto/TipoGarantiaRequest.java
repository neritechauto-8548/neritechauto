package com.neritech.saas.garantia.dto;

import com.neritech.saas.garantia.domain.TipoCobertura;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO de requisiÃ§Ã£o para TipoGarantia
 */
public record TipoGarantiaRequest(
        @NotBlank(message = "Nome Ã© obrigatÃ³rio") @Size(max = 100, message = "Nome deve ter no mÃ¡ximo 100 caracteres") String nome,

        String descricao,

        @NotNull(message = "Prazo em dias Ã© obrigatÃ³rio") @Positive(message = "Prazo deve ser positivo") Integer prazoDias,

        @NotNull(message = "Tipo de cobertura Ã© obrigatÃ³rio") TipoCobertura tipoCobertura,

        @DecimalMin(value = "0.0", message = "Percentual deve ser maior ou igual a 0") @DecimalMax(value = "100.0", message = "Percentual deve ser menor ou igual a 100") BigDecimal percentualCobertura,

        @PositiveOrZero(message = "Valor mÃ¡ximo deve ser positivo ou zero") BigDecimal valorMaximoCobertura,

        String condicoesAplicacao,
        String restricoes,
        String documentacaoNecessaria,
        String processoAcionamento,

        @Positive(message = "SLA deve ser positivo") Integer slaAtendimentoHoras,

        Boolean transferivel,
        Boolean renovavel,
        String custosAdicionais,
        String exclusoes,
        Boolean ativo,
        Boolean padraoServicos,
        Boolean padraoProdutos) {
}
