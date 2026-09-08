package com.neritech.saas.orcamento.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "estimate_discount_approval_requests")
public class OrcamentoDiscountApprovalRequest extends TenantEntity {

    public enum Status { PENDING, APPROVED, REJECTED, CANCELLED }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico orcamento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_group_id", nullable = false)
    private OrcamentoServiceGroup group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "line_item_id", nullable = false)
    private OrcamentoLineItem lineItem;

    @Column(name = "requested_revision", nullable = false)
    private long requestedRevision;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private OrcamentoLineItem.DiscountType discountType;

    @Column(name = "discount_value", nullable = false, precision = 14, scale = 4)
    private BigDecimal discountValue;

    @Column(name = "calculated_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal calculatedAmount;

    @Column(name = "equivalent_percentage", nullable = false, precision = 7, scale = 4)
    private BigDecimal equivalentPercentage;

    @Column(name = "authority_limit_percentage", nullable = false, precision = 7, scale = 4)
    private BigDecimal authorityLimitPercentage;

    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 24)
    private Status status = Status.PENDING;

    @Column(name = "requested_by", nullable = false)
    private Long requestedBy;

    @Column(name = "decided_by")
    private Long decidedBy;

    @Column(name = "decision_reason", length = 500)
    private String decisionReason;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;

    public OrdemServico getOrcamento() { return orcamento; }
    public void setOrcamento(OrdemServico orcamento) { this.orcamento = orcamento; }
    public OrcamentoServiceGroup getGroup() { return group; }
    public void setGroup(OrcamentoServiceGroup group) { this.group = group; }
    public OrcamentoLineItem getLineItem() { return lineItem; }
    public void setLineItem(OrcamentoLineItem lineItem) { this.lineItem = lineItem; }
    public long getRequestedRevision() { return requestedRevision; }
    public void setRequestedRevision(long requestedRevision) { this.requestedRevision = requestedRevision; }
    public OrcamentoLineItem.DiscountType getDiscountType() { return discountType; }
    public void setDiscountType(OrcamentoLineItem.DiscountType discountType) { this.discountType = discountType; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    public BigDecimal getCalculatedAmount() { return calculatedAmount; }
    public void setCalculatedAmount(BigDecimal calculatedAmount) { this.calculatedAmount = calculatedAmount; }
    public BigDecimal getEquivalentPercentage() { return equivalentPercentage; }
    public void setEquivalentPercentage(BigDecimal equivalentPercentage) { this.equivalentPercentage = equivalentPercentage; }
    public BigDecimal getAuthorityLimitPercentage() { return authorityLimitPercentage; }
    public void setAuthorityLimitPercentage(BigDecimal authorityLimitPercentage) { this.authorityLimitPercentage = authorityLimitPercentage; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Long getRequestedBy() { return requestedBy; }
    public void setRequestedBy(Long requestedBy) { this.requestedBy = requestedBy; }
    public Long getDecidedBy() { return decidedBy; }
    public void setDecidedBy(Long decidedBy) { this.decidedBy = decidedBy; }
    public String getDecisionReason() { return decisionReason; }
    public void setDecisionReason(String decisionReason) { this.decisionReason = decisionReason; }
    public LocalDateTime getDecidedAt() { return decidedAt; }
    public void setDecidedAt(LocalDateTime decidedAt) { this.decidedAt = decidedAt; }
}

