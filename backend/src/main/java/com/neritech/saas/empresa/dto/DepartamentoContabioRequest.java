package com.neritech.saas.empresa.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

public record DepartamentoContabioRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 255) String descricao,
        @NotBlank @Size(max = 30) String codigo,
        @Size(max = 2000) String detalhes,
        @Size(max = 50) String tipoOperacao,
        Long supervisorId,
        Boolean ativo,
        @Min(0) Integer ordem,
        @Min(1) Integer tempoPadraoMinutos,
        Integer versao
) {}

