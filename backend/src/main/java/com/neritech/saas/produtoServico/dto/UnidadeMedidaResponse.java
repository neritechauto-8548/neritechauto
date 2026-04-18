package com.neritech.saas.produtoServico.dto;

public record UnidadeMedidaResponse(
        Long id,
        String nome,
        String sigla,
        Boolean ativo) {
}
