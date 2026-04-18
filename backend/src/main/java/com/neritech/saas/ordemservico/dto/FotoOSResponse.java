package com.neritech.saas.ordemservico.dto;

import java.time.LocalDateTime;

public record FotoOSResponse(
        Long id,
        Long empresaId,
        Long ordemServicoId,
        String arquivoUrl,
        String contentType,
        Long tamanho,
        String descricao,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
