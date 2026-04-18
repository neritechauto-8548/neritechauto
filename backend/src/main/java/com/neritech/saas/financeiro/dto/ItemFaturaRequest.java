package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.TipoItemFatura;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ItemFaturaRequest(
        TipoItemFatura tipoItem,
        Long servicoId,
        Long produtoId,

        @NotBlank(message = "DescriÃ§Ã£o Ã© obrigatÃ³ria") String descricao,

        @NotNull(message = "Quantidade Ã© obrigatÃ³ria") BigDecimal quantidade,

        @NotNull(message = "Valor unitÃ¡rio Ã© obrigatÃ³rio") BigDecimal valorUnitario,

        BigDecimal descontoPercentual,
        BigDecimal descontoValor,

        String ncm,
        String cfop,
        String cstIcms,
        String cstPis,
        String cstCofins,
        BigDecimal aliquotaIcms,
        BigDecimal aliquotaPis,
        BigDecimal aliquotaCofins,
        String observacoes,
        Integer ordemItem) {
}
