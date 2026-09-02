package com.neritech.saas.ordemservico.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contratos canônicos da revisão e conclusão operacional da Ordem de Serviço.
 *
 * A conclusão operacional não gera contas a receber, fatura ou documento fiscal.
 * Esses efeitos pertencem ao comando separado de liberação para faturamento.
 */
public final class OSClosureModels {

    private OSClosureModels() {
    }

    public record Guard(
            String code,
            String label,
            String status,
            String message,
            String owner,
            String route,
            boolean overrideAllowed) {
    }

    public record Review(
            Long ordemServicoId,
            String numeroOS,
            Integer aggregateVersion,
            String operationalState,
            boolean readyToComplete,
            boolean alreadyCompleted,
            List<Guard> guards,
            List<String> partialSources,
            Long snapshotId,
            LocalDateTime operationallyCompletedAt) {
    }

    public record CommandResult(
            Long ordemServicoId,
            String numeroOS,
            String operationalState,
            Integer aggregateVersion,
            Long snapshotId,
            LocalDateTime operationallyCompletedAt,
            String downstreamRequestId) {
    }
}
