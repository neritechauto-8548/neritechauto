package com.neritech.saas.ordemservico.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "os_additional_requests")
public class OSAdditionalRequest extends BaseEntity {

    public enum Status {
        RASCUNHO, PRONTA_PARA_ENVIO, PENDENTE, VISUALIZADA, APROVADA, PARCIAL,
        RECUSADA, EXPIRADA, REVOGADA, SUBSTITUIDA, CANCELADA
    }

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "ordem_servico_id", nullable = false)
    private Long ordemServicoId;

    @Column(name = "base_os_version")
    private Integer baseOsVersion;

    @Column(name = "title", nullable = false, length = 160)
    private String title;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private Status status = Status.RASCUNHO;

    @Column(name = "amount_delta", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountDelta = BigDecimal.ZERO;

    @Column(name = "time_delta_minutes", nullable = false)
    private Integer timeDeltaMinutes = 0;

    @Column(name = "recipient_name", length = 160)
    private String recipientName;

    @Column(name = "recipient_channel", length = 30)
    private String recipientChannel;

    @Column(name = "recipient_masked", length = 180)
    private String recipientMasked;

    @Column(name = "token_hash", length = 64)
    private String tokenHash;

    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpiresAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "viewed_at")
    private LocalDateTime viewedAt;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;
}
