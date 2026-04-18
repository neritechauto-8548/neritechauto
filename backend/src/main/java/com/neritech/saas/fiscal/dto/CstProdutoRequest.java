package com.neritech.saas.fiscal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CstProdutoRequest(
        @NotBlank(message = "O cÃ³digo Ã© obrigatÃ³rio") @Size(max = 10, message = "O cÃ³digo deve ter no mÃ¡ximo 10 caracteres") String codigo,

        @NotBlank(message = "A descriÃ§Ã£o Ã© obrigatÃ³ria") String descricao,

        String tipo,
        boolean ativo) {
}
