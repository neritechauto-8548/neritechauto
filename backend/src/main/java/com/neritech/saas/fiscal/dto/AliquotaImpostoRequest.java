package com.neritech.saas.fiscal.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AliquotaImpostoRequest(
        @NotBlank(message = "O nome do imposto Ã© obrigatÃ³rio") String nomeImposto,

        @NotNull(message = "A alÃ­quota Ã© obrigatÃ³ria") @DecimalMin(value = "0.0", message = "A alÃ­quota deve ser maior ou igual a zero") BigDecimal aliquota,

        String descricao,
        String uf,
        boolean padrao) {
}
