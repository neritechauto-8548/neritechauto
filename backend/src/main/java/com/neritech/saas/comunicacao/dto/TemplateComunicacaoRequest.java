package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.CategoriaTemplate;
import com.neritech.saas.comunicacao.domain.enums.TipoTemplate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TemplateComunicacaoRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 100) String nome,
        @NotNull TipoTemplate tipoTemplate,
        @NotNull CategoriaTemplate categoria,
        @Size(max = 255) String assunto,
        @NotBlank String conteudo,
        String variaveisDisponiveis,
        String anexosPadrao,
        Boolean ativo,
        Boolean padraoCategoria,
        @Size(max = 5) String idioma,
        Boolean personalizavel,
        Boolean requerAprovacao,
        @Size(max = 255) String tags) {
}
