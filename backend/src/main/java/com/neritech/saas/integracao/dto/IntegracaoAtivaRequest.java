package com.neritech.saas.integracao.dto;

import jakarta.validation.constraints.NotBlank;

public record IntegracaoAtivaRequest(
        @NotBlank(message = "O nome Ã© obrigatÃ³rio") String nome,

        @NotBlank(message = "O tipo Ã© obrigatÃ³rio") String tipo,

        @NotBlank(message = "A URL base Ã© obrigatÃ³ria") String urlBase,

        String descricao,
        boolean ativo,
        String configuracoes) {
}
