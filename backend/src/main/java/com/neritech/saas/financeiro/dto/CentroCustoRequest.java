package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.TipoCentroCusto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CentroCustoRequest(
        @NotBlank(message = "CÃ³digo Ã© obrigatÃ³rio") @Size(max = 20) String codigo,

        @NotBlank(message = "Nome Ã© obrigatÃ³rio") @Size(max = 100) String nome,

        String descricao,
        Long centroCustoPaiId,
        TipoCentroCusto tipo,
        Long responsavelId,
        BigDecimal orcamentoMensal,
        BigDecimal orcamentoAnual,
        Boolean ativo) {
}
