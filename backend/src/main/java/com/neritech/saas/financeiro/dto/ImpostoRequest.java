package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.BaseCalculoImposto;
import com.neritech.saas.financeiro.domain.enums.TipoImposto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ImpostoRequest(
        @NotNull(message = "Tipo de imposto Ã© obrigatÃ³rio") TipoImposto tipoImposto,

        @NotBlank(message = "Nome do imposto Ã© obrigatÃ³rio") @Size(max = 100) String nomeImposto,

        @NotNull(message = "AlÃ­quota Ã© obrigatÃ³ria") BigDecimal aliquotaPercentual,

        BaseCalculoImposto baseCalculo,

        @Size(max = 10) String codigoReceita,

        String aplicavelRegime, // JSON
        Boolean ativo,

        @NotNull(message = "Data de inÃ­cio de vigÃªncia Ã© obrigatÃ³ria") LocalDate dataInicioVigencia,

        LocalDate dataFimVigencia,
        String observacoes) {
}
