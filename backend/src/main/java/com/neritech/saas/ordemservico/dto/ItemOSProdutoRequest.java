package com.neritech.saas.ordemservico.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ItemOSProdutoRequest(
        @NotNull Long ordemServicoId,
        Long produtoId,
        String descricao,
        @NotNull BigDecimal quantidade,
        @NotNull BigDecimal valorUnitario,
        @NotNull BigDecimal valorTotal,
        BigDecimal descontoPercentual,
        BigDecimal descontoValor,
        @NotNull BigDecimal valorFinal,
        String loteNumero,
        Long localizacaoEstoqueId,
        Long fornecedorId,
        BigDecimal precoCusto,
        Integer garantiaMeses,
        String numeroSerie,
        String observacoes,
        Boolean aprovadoCliente) {
}
