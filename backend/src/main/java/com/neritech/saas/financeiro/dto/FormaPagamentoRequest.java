package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.TipoFormaPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record FormaPagamentoRequest(
        @NotBlank(message = "Nome Ã© obrigatÃ³rio") @Size(max = 100) String nome,

        TipoFormaPagamento tipo,
        Boolean aceitaParcelamento,
        Integer parcelasMaximas,
        BigDecimal taxaAdministracao,
        Integer prazoRecebimentoDias,
        Long contaBancariaId,
        Boolean ativo,
        Boolean padrao,
        Boolean exigeAutorizacao,
        BigDecimal limiteDiario,
        String observacoes) {
}
