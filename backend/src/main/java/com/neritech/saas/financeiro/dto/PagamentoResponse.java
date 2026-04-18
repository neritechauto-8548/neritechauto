package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.StatusPagamento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PagamentoResponse(
        Long id,
        Long empresaId,
        Long faturaId,
        String faturaNumero,
        Long fornecedorId,
        Long clienteId,
        Long formaPagamentoId,
        String formaPagamentoNome,
        Long condicaoPagamentoId,
        String condicaoPagamentoNome,
        Long contaBancariaId,
        String contaBancariaNome,
        LocalDate dataPagamento,
        BigDecimal valorOriginal,
        BigDecimal valorDesconto,
        BigDecimal valorJuros,
        BigDecimal valorMulta,
        BigDecimal valorTotal,
        StatusPagamento status,
        String comprovanteUrl,
        String observacoes,
        List<ParcelaPagamentoResponse> parcelas,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
