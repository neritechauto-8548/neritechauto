package com.neritech.saas.fiscal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NcmProdutoRequest(
        @NotBlank(message = "O cÃ³digo Ã© obrigatÃ³rio") @Size(max = 20, message = "O cÃ³digo deve ter no mÃ¡ximo 20 caracteres") String codigo,

        @NotBlank(message = "A descriÃ§Ã£o Ã© obrigatÃ³ria") String descricao,

        Double aliquotaIpi,
        boolean ativo) {
}
