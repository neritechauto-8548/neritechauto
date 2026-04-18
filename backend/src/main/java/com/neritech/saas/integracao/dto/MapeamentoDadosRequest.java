package com.neritech.saas.integracao.dto;

import jakarta.validation.constraints.NotBlank;

public record MapeamentoDadosRequest(
        Long integracaoId,

        @NotBlank(message = "A entidade Ã© obrigatÃ³ria") String entidade,

        @NotBlank(message = "O campo de origem Ã© obrigatÃ³rio") String campoOrigem,

        @NotBlank(message = "O campo de destino Ã© obrigatÃ³rio") String campoDestino,

        String transformacao,
        String regrasTransformacao,
        boolean ativo) {
}
