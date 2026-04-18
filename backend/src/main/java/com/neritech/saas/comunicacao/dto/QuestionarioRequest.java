package com.neritech.saas.comunicacao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QuestionarioRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 255) String dsQuestionario,
        @Size(max = 500) String urlImagem,
        Boolean snAtivo
) {}

