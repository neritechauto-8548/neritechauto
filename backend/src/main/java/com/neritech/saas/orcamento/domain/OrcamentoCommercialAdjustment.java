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

@Entity
@Table(name = "estimate_commercial_adjustments")
public class OrcamentoCommercialAdjustment extends TenantEntity {

    public enum AdjustmentType { PACKAGE_PRICE, UNIT_PRICE_OVERRIDE, LINE_DISCOUNT, DISCOUNT_DECISION }
    public enum AuthorityStatus { APPROVED, PENDING_APPROVAL, REJECTED, CANCELLED }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico orcamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_group_id")
    private OrcamentoServiceGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_item_id")
    private OrcamentoLineItem lineItem;

    @Column(name = "estimate_revision", nullable = false)
    private long estimateRevision;

    @Enumerated(EnumType.STRING)
    @Column(name = "adjustment_type", nullable = false, length = 32)
    private AdjustmentType adjustmentType;

    @Column(name = "previous_amount", precision = 14, scale = 4)
    private BigDecimal previousAmount;

    @Column(name = "new_amount", precision = 14, scale = 4)
    private BigDecimal newAmount;

    @Column(name = "impact_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal impactAmount;

    @Column(name = "distribution_method", length = 24)
    private String distributionMethod;

    @Column(name = "price_source_type", length = 40)
    private String priceSourceType;

    @Column(name = "price_source_id")
    private Long priceSourceId;

    @Column(name = "price_source_version")
    private Integer priceSourceVersion;

    @Column(name = "reason", length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority_status", nullable = false, length = 24)
    private AuthorityStatus authorityStatus;

    @Column(name = "actor_id", nullable = false)
    private Long actorId;

    public OrdemServico getOrcamento() { return orcamento; }
    public void setOrcamento(OrdemServico orcamento) { this.orcamento = orcamento; }
    public OrcamentoServiceGroup getGroup() { return group; }
    public void setGroup(OrcamentoServiceGroup group) { this.group = group; }
    public OrcamentoLineItem getLineItem() { return lineItem; }
    public void setLineItem(OrcamentoLineItem lineItem) { this.lineItem = lineItem; }
    public long getEstimateRevision() { return estimateRevision; }
    public void setEstimateRevision(long estimateRevision) { this.estimateRevision = estimateRevision; }
    public AdjustmentType getAdjustmentType() { return adjustmentType; }
    public void setAdjustmentType(AdjustmentType adjustmentType) { this.adjustmentType = adjustmentType; }
    public BigDecimal getPreviousAmount() { return previousAmount; }
    public void setPreviousAmount(BigDecimal previousAmount) { this.previousAmount = previousAmount; }
    public BigDecimal getNewAmount() { return newAmount; }
    public void setNewAmount(BigDecimal newAmount) { this.newAmount = newAmount; }
    public BigDecimal getImpactAmount() { return impactAmount; }
    public void setImpactAmount(BigDecimal impactAmount) { this.impactAmount = impactAmount; }
    public String getDistributionMethod() { return distributionMethod; }
    public void setDistributionMethod(String distributionMethod) { this.distributionMethod = distributionMethod; }
    public String getPriceSourceType() { return priceSourceType; }
    public void setPriceSourceType(String priceSourceType) { this.priceSourceType = priceSourceType; }
    public Long getPriceSourceId() { return priceSourceId; }
    public void setPriceSourceId(Long priceSourceId) { this.priceSourceId = priceSourceId; }
    public Integer getPriceSourceVersion() { return priceSourceVersion; }
    public void setPriceSourceVersion(Integer priceSourceVersion) { this.priceSourceVersion = priceSourceVersion; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public AuthorityStatus getAuthorityStatus() { return authorityStatus; }
    public void setAuthorityStatus(AuthorityStatus authorityStatus) { this.authorityStatus = authorityStatus; }
    public Long getActorId() { return actorId; }
    public void setActorId(Long actorId) { this.actorId = actorId; }
}

