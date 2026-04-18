package com.neritech.saas.produtoServico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UnidadeMedidaRequest(
        @NotBlank(message = "O nome é obrigatório") @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres") String nome,
        @NotBlank(message = "A sigla é obrigatória") @Size(max = 10, message = "A sigla deve ter no máximo 10 caracteres") String sigla,
        Boolean ativo) {
}
