package com.neritech.saas.ordemservico.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class OSAdditionalModels {
    private OSAdditionalModels() {}

    public record ItemDraft(
            @NotBlank String operation,
            @NotBlank String itemType,
            Long sourceItemId,
            Long catalogItemId,
            @NotBlank @Size(max = 500) String description,
            @NotNull @Positive BigDecimal quantity,
            @Size(max = 20) String unit,
            BigDecimal amountDelta,
            Integer timeDeltaMinutes) {}

    public record CreateRequest(
            @NotBlank @Size(max = 160) String title,
            @NotBlank String reason,
            @NotEmpty List<@Valid ItemDraft> items) {}

    public record UpdateRequest(
            @NotBlank @Size(max = 160) String title,
            @NotBlank String reason,
            @NotEmpty List<@Valid ItemDraft> items) {}

    public record SubmitRequest(
            @NotBlank @Size(max = 160) String recipientName,
            @NotBlank @Size(max = 30) String channel,
            @NotBlank @Size(max = 180) String recipientMasked,
            @NotNull @Future LocalDateTime expiresAt) {}

    public record ItemDecision(
            @NotNull Long itemId,
            @NotBlank String decision,
            @Size(max = 500) String comment) {}

    public record PublicDecisionRequest(@NotEmpty List<@Valid ItemDecision> items) {}

    public record ItemResponse(
            Long id,
            String operation,
            String itemType,
            Long sourceItemId,
            Long catalogItemId,
            String description,
            BigDecimal quantity,
            String unit,
            BigDecimal amountDelta,
            Integer timeDeltaMinutes,
            String decision,
            String decisionComment) {}

    public record Response(
            Long id,
            Long ordemServicoId,
            Integer baseOsVersion,
            String title,
            String reason,
            String status,
            BigDecimal amountDelta,
            Integer timeDeltaMinutes,
            String recipientName,
            String recipientChannel,
            String recipientMasked,
            LocalDateTime tokenExpiresAt,
            LocalDateTime submittedAt,
            LocalDateTime viewedAt,
            LocalDateTime decidedAt,
            LocalDateTime revokedAt,
            Integer version,
            LocalDateTime createdAt,
            List<ItemResponse> items,
            List<String> allowedActions) {}

    /** Token bruto é efêmero: só é devolvido no comando submit. */
    public record SubmitResponse(Response request, String approvalToken) {}

    public record PublicResponse(
            Long requestId,
            String orderNumber,
            String title,
            String reason,
            String status,
            BigDecimal amountDelta,
            Integer timeDeltaMinutes,
            LocalDateTime expiresAt,
            List<ItemResponse> items) {}
}
