package com.neritech.saas.produtoServico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoriaProdutoRequest(
        @NotNull(message = "O ID da empresa é obrigatório") Long empresaId,
        @NotBlank(message = "O nome é obrigatório") @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres") String nome,
        Boolean ativo) {
}
