package com.neritech.saas.gestaoUsuarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record FuncaoRequest(
        @NotBlank @Size(max = 50) String nome,
        @Size(max = 255) String descricao,
        Boolean ativo,
        Set<Long> permissaoIds,
        Integer versao) {}
