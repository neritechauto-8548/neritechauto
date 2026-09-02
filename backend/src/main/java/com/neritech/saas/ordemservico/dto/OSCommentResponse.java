package com.neritech.saas.ordemservico.dto;

import java.time.LocalDateTime;

public record OSCommentResponse(
        Long id,
        Long ordemServicoId,
        Long authorUserId,
        String authorName,
        String content,
        String visibility,
        LocalDateTime createdAt) {
}
