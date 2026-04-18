package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.StatusTitulo;
import com.neritech.saas.financeiro.domain.enums.TipoTitulo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ContasReceberResponse(
        Long id,
        Long empresaId,
        String descricao,
        Long clienteId,
        Long faturaId,
        String faturaNumero,
        LocalDate dataEmissao,
        LocalDate dataVencimento,
        LocalDate dataRecebimento,
        BigDecimal valorOriginal,
        BigDecimal valorRecebido,
        BigDecimal valorJuros,
        BigDecimal valorMulta,
        BigDecimal valorDesconto,
        BigDecimal saldoDevedor,
        StatusTitulo status,
        TipoTitulo tipoTitulo,
        Long formaPagamentoId,
        String formaPagamentoNome,
        Long contaBancariaId,
        String contaBancariaNome,
        Long centroCustoId,
        String centroCustoNome,
        Long planoContasId,
        String planoContasNome,
        String numeroTitulo,
        String observacoes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
