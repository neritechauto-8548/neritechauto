package com.neritech.saas.ordemservico.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrdemServicoExecutionResponse(
        Long ordemServicoId,
        String numero,
        LocalDateTime serverTime,
        Summary summary,
        WorkSessionResponse activeSession,
        List<ServiceExecution> services) {

    public record Summary(
            Integer totalServices,
            Integer inProgressServices,
            Long elapsedSeconds,
            Integer estimatedMinutes,
            Integer soldMinutes,
            Integer blockers) {
    }

    public record ServiceExecution(
            Long id,
            Long catalogServiceId,
            String description,
            Long technicianId,
            String status,
            Boolean authorized,
            Integer estimatedMinutes,
            Integer realMinutes,
            Integer soldMinutes,
            LocalDateTime startedAt,
            LocalDateTime completedAt,
            List<String> blockers,
            List<String> allowedActions) {
    }
}
