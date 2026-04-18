package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.TipoFormaPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FormaPagamentoResponse(
        Long id,
        Long empresaId,
        String nome,
        TipoFormaPagamento tipo,
        Boolean aceitaParcelamento,
        Integer parcelasMaximas,
        BigDecimal taxaAdministracao,
        Integer prazoRecebimentoDias,
        Long contaBancariaId,
        String contaBancariaNome,
        Boolean ativo,
        Boolean padrao,
        Boolean exigeAutorizacao,
        BigDecimal limiteDiario,
        String observacoes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
