package com.neritech.saas.fiscal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegimeTributarioRequest(
        @NotBlank(message = "O cÃ³digo Ã© obrigatÃ³rio") @Size(max = 50, message = "O cÃ³digo deve ter no mÃ¡ximo 50 caracteres") String codigo,

        @NotBlank(message = "O nome Ã© obrigatÃ³rio") @Size(max = 100, message = "O nome deve ter no mÃ¡ximo 100 caracteres") String nome,

        String descricao,

        boolean ativo) {
}
