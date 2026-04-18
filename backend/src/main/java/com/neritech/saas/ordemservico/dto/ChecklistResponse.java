package com.neritech.saas.ordemservico.dto;

import java.time.LocalDateTime;

public record ChecklistResponse(
        Long id,
        Long empresaId,
        String dsChecklist,
        String urlImagem,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}

