package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.NaturezaSaldo;
import com.neritech.saas.financeiro.domain.enums.TipoConta;
import com.neritech.saas.financeiro.domain.enums.TipoContaContabil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlanoContaRequest(
        @NotBlank(message = "CÃ³digo Ã© obrigatÃ³rio") @Size(max = 20) String codigo,

        @NotBlank(message = "Nome Ã© obrigatÃ³rio") String nome,

        Long contaPaiId,

        @NotNull(message = "NÃ­vel Ã© obrigatÃ³rio") Integer nivel,

        TipoContaContabil tipoConta,
        NaturezaSaldo naturezaSaldo,
        Boolean aceitaLancamento,
        Boolean ativo) {
}
