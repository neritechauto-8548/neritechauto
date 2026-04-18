package com.neritech.saas.ordemservico.dto;

import java.time.LocalDateTime;

public record ItChecklistResponse(
        Long id,
        Long checkListId,
        String dsItChecklist,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}

