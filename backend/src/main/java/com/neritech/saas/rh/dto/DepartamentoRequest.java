package com.neritech.saas.rh.dto;

import jakarta.validation.constraints.*;

public record DepartamentoRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 100) String nome,
        String descricao,
        @Size(max = 20) String codigo,
        Long departamentoPaiId,
        Long gerenteId,
        @Size(max = 20) String centroCusto,
        Boolean ativo) {
}
