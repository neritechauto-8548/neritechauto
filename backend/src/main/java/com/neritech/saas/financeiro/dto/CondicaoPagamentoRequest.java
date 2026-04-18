package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.TipoCondicaoPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CondicaoPagamentoRequest(
        @NotBlank(message = "Nome Ã© obrigatÃ³rio") @Size(max = 100) String nome,

        String descricao,
        TipoCondicaoPagamento tipo,
        Integer numeroParcelas,
        Integer intervaloDias,
        BigDecimal valorEntradaPercentual,
        BigDecimal descontoAVistaPercentual,
        BigDecimal jurosParcelamentoPercentual,
        Integer vencimentoPrimeiraParcelaDias,
        String formasPagamentoAceitas, // JSON string
        Boolean padrao,
        Boolean ativo,
        String observacoes) {
}
