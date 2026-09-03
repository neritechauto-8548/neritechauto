package com.neritech.saas.ordemservico.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Read model composto da Visão 360 da Ordem de Serviço.
 *
 * O frontend deve renderizar stage/nextAction/allowedActions exatamente como
 * retornados por este contrato. Regras de próxima ação pertencem ao backend.
 */
public record OrdemServicoCockpitResponse(
        Long id,
        String numero,
        Long tenantId,
        Long unitId,
        Integer version,
        Stage stage,
        NextAction nextAction,
        List<AllowedAction> allowedActions,
        Customer customer,
        Vehicle vehicle,
        Execution execution,
        Parts parts,
        Approvals approvals,
        List<Block> blocks,
        RelatedCounts relatedCounts,
        Financial financial,
        Fiscal fiscal,
        Audit audit,
        List<String> partialSources) {

    public record Stage(String code, String label, String severity, String color) {}

    public record NextAction(
            String code,
            String label,
            String reason,
            String route,
            String event) {}

    public record AllowedAction(String code, String label) {}

    public record Customer(Long id, String name) {}

    public record Vehicle(Long id, String plate, String description) {}

    public record Execution(
            String status,
            Long responsibleId,
            LocalDateTime plannedStart,
            LocalDateTime plannedEnd,
            LocalDateTime startedAt,
            LocalDateTime completedAt,
            Integer progress) {}

    public record Parts(Integer totalItems, Integer reservedItems, Integer missingItems) {}

    public record Approvals(Integer pending, Integer approved, Integer rejected) {}

    public record Block(String code, String label, String severity) {}

    public record RelatedCounts(Integer checklists, Integer evidences, Integer additionalRequests) {}

    public record Financial(
            String status,
            BigDecimal totalReceivable,
            BigDecimal paidAmount,
            BigDecimal remainingAmount) {}

    public record Fiscal(String status, List<String> documents) {}

    public record Audit(LocalDateTime createdAt, LocalDateTime updatedAt, String traceId) {}
}
