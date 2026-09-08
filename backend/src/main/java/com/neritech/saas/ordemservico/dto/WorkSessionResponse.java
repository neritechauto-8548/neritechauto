package com.neritech.saas.ordemservico.dto;

import java.time.LocalDateTime;
import java.util.List;

public record WorkSessionResponse(
        Long sessionId,
        Long ordemServicoId,
        Long serviceId,
        Long technicianId,
        String technicianName,
        String status,
        String source,
        LocalDateTime startedAt,
        LocalDateTime pausedAt,
        LocalDateTime endedAt,
        String pauseReason,
        String pauseNote,
        Long elapsedSeconds,
        Integer sessionVersion,
        LocalDateTime serverTime,
        String serviceStatus,
        List<String> blockers,
        List<String> allowedActions) {
}
