package com.neritech.saas.ordemservico.dto;

import java.time.LocalDateTime;

public record OSChecklistItemResponse(
        Long id,
        Long ordemServicoId,
        Long checklistModeloId,
        Long itemModeloId,
        String descricao,
        Boolean feito,
        Integer ordem,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}
