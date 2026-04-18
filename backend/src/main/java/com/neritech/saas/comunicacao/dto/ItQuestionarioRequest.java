package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.TipoItemQuestionario;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItQuestionarioRequest(
        @NotNull Long questionarioId,
        @NotBlank @Size(max = 255) String dsItQuestionario,
        @NotNull TipoItemQuestionario tpItQuestionario
) {}

