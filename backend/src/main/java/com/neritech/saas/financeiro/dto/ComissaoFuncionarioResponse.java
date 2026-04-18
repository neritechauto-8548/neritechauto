package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.TipoComissao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ComissaoFuncionarioResponse(
        Long id,
        Long empresaId,
        Long funcionarioId,
        String funcionarioNome, // Assuming we can fetch this
        Long faturaId,
        String faturaNumero,
        Long itemFaturaId,
        String itemFaturaDescricao,
        LocalDate dataReferencia,
        BigDecimal valorBase,
        BigDecimal percentualComissao,
        BigDecimal valorComissao,
        TipoComissao tipoComissao,
        Boolean pago,
        LocalDate dataPagamento,
        String observacoes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
