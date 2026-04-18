package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.TipoMovimentacao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FluxoCaixaResponse(
        Long id,
        Long empresaId,
        LocalDate dataMovimento,
        String descricao,
        TipoMovimentacao tipoMovimentacao,
        BigDecimal valor,
        BigDecimal saldoAcumulado,
        Long contaBancariaId,
        String contaBancariaNome,
        Long centroCustoId,
        String centroCustoNome,
        Long planoContasId,
        String planoContasNome,
        Long pagamentoId,
        Long recebimentoId,
        String observacoes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
