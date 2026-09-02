package com.neritech.saas.ordemservico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OSCommentCreateRequest(
        @NotBlank @Size(max = 2000) String content) {
}
