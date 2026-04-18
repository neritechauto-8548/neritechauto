package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.TipoCondicaoPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CondicaoPagamentoResponse(
        Long id,
        Long empresaId,
        String nome,
        String descricao,
        TipoCondicaoPagamento tipo,
        Integer numeroParcelas,
        Integer intervaloDias,
        BigDecimal valorEntradaPercentual,
        BigDecimal descontoAVistaPercentual,
        BigDecimal jurosParcelamentoPercentual,
        Integer vencimentoPrimeiraParcelaDias,
        String formasPagamentoAceitas,
        Boolean padrao,
        Boolean ativo,
        String observacoes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
