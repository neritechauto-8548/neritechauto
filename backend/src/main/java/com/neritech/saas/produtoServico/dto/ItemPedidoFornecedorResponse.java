package com.neritech.saas.produtoServico.dto;

import java.math.BigDecimal;

public record ItemPedidoFornecedorResponse(
        Long id,
        Long produtoId,
        String nomeProduto,
        String codigoInternoProduto,
        BigDecimal quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {}
