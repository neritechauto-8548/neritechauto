package com.neritech.saas.ordemservico.dto;

import jakarta.validation.constraints.Size;

public record OSChecklistItemRequest(
        @Size(max = 255) String descricao,
        Boolean feito,
        Integer ordem
) {}
