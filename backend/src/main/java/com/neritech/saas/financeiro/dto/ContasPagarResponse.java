package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.StatusTitulo;
import com.neritech.saas.financeiro.domain.enums.TipoTitulo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ContasPagarResponse(
        Long id,
        Long empresaId,
        String descricao,
        Long fornecedorId,
        String numeroDocumento,
        LocalDate dataEmissao,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        LocalDate dataAgendamento,
        BigDecimal valorOriginal,
        BigDecimal valorPago,
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
        String codigoBarras,
        String observacoes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
