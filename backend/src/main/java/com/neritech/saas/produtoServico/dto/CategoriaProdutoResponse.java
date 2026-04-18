package com.neritech.saas.produtoServico.dto;

public record CategoriaProdutoResponse(
        Long id,
        Long empresaId,
        String nome,
        Boolean ativo) {
}
