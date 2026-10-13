package com.neritech.saas.comunicacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QuestionarioDuplicacaoRequest(
    @NotBlank @Size(min = 2, max = 255) String titulo
) {}
