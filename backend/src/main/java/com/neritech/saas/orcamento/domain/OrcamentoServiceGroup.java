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
@Table(name = "estimate_service_groups")
public class OrcamentoServiceGroup extends TenantEntity {

    public enum Visibility { CUSTOMER_VISIBLE, INTERNAL_ONLY }
    public enum PackageDistributionMethod { WEIGHTED, LABOR_FIRST, POLICY }
    public enum CommercialAuthorityStatus { APPROVED, PENDING_APPROVAL }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico orcamento;

    @Column(name = "title", nullable = false, length = 120)
    private String title;

    @Column(name = "customer_description", columnDefinition = "TEXT")
    private String customerDescription;

    @Column(name = "internal_note", columnDefinition = "TEXT")
    private String internalNote;

    @Column(name = "recommended", nullable = false)
    private boolean recommended;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false, length = 24)
    private Visibility visibility = Visibility.CUSTOMER_VISIBLE;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "kit_origin_id")
    private Long kitOriginId;

    @Column(name = "kit_origin_version")
    private Integer kitOriginVersion;

    @Column(name = "package_price", precision = 14, scale = 2)
    private BigDecimal packagePrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "package_distribution_method", length = 24)
    private PackageDistributionMethod packageDistributionMethod;

    @Column(name = "package_original_subtotal", precision = 14, scale = 2)
    private BigDecimal packageOriginalSubtotal;

    @Column(name = "package_adjustment_amount", precision = 14, scale = 2)
    private BigDecimal packageAdjustmentAmount;

    @Column(name = "package_price_source_type", length = 40)
    private String packagePriceSourceType;

    @Column(name = "package_price_source_id")
    private Long packagePriceSourceId;

    @Column(name = "package_price_source_version")
    private Integer packagePriceSourceVersion;

    @Column(name = "package_applied_at")
    private LocalDateTime packageAppliedAt;

    @Column(name = "package_override_reason", length = 500)
    private String packageOverrideReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "package_authority_status", length = 24)
    private CommercialAuthorityStatus packageAuthorityStatus;

    public OrdemServico getOrcamento() { return orcamento; }
    public void setOrcamento(OrdemServico orcamento) { this.orcamento = orcamento; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCustomerDescription() { return customerDescription; }
    public void setCustomerDescription(String customerDescription) { this.customerDescription = customerDescription; }
    public String getInternalNote() { return internalNote; }
    public void setInternalNote(String internalNote) { this.internalNote = internalNote; }
    public boolean isRecommended() { return recommended; }
    public void setRecommended(boolean recommended) { this.recommended = recommended; }
    public Visibility getVisibility() { return visibility; }
    public void setVisibility(Visibility visibility) { this.visibility = visibility; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public Long getKitOriginId() { return kitOriginId; }
    public void setKitOriginId(Long kitOriginId) { this.kitOriginId = kitOriginId; }
    public Integer getKitOriginVersion() { return kitOriginVersion; }
    public void setKitOriginVersion(Integer kitOriginVersion) { this.kitOriginVersion = kitOriginVersion; }
    public BigDecimal getPackagePrice() { return packagePrice; }
    public void setPackagePrice(BigDecimal packagePrice) { this.packagePrice = packagePrice; }
    public PackageDistributionMethod getPackageDistributionMethod() { return packageDistributionMethod; }
    public void setPackageDistributionMethod(PackageDistributionMethod packageDistributionMethod) { this.packageDistributionMethod = packageDistributionMethod; }
    public BigDecimal getPackageOriginalSubtotal() { return packageOriginalSubtotal; }
    public void setPackageOriginalSubtotal(BigDecimal packageOriginalSubtotal) { this.packageOriginalSubtotal = packageOriginalSubtotal; }
    public BigDecimal getPackageAdjustmentAmount() { return packageAdjustmentAmount; }
    public void setPackageAdjustmentAmount(BigDecimal packageAdjustmentAmount) { this.packageAdjustmentAmount = packageAdjustmentAmount; }
    public String getPackagePriceSourceType() { return packagePriceSourceType; }
    public void setPackagePriceSourceType(String packagePriceSourceType) { this.packagePriceSourceType = packagePriceSourceType; }
    public Long getPackagePriceSourceId() { return packagePriceSourceId; }
    public void setPackagePriceSourceId(Long packagePriceSourceId) { this.packagePriceSourceId = packagePriceSourceId; }
    public Integer getPackagePriceSourceVersion() { return packagePriceSourceVersion; }
    public void setPackagePriceSourceVersion(Integer packagePriceSourceVersion) { this.packagePriceSourceVersion = packagePriceSourceVersion; }
    public LocalDateTime getPackageAppliedAt() { return packageAppliedAt; }
    public void setPackageAppliedAt(LocalDateTime packageAppliedAt) { this.packageAppliedAt = packageAppliedAt; }
    public String getPackageOverrideReason() { return packageOverrideReason; }
    public void setPackageOverrideReason(String packageOverrideReason) { this.packageOverrideReason = packageOverrideReason; }
    public CommercialAuthorityStatus getPackageAuthorityStatus() { return packageAuthorityStatus; }
    public void setPackageAuthorityStatus(CommercialAuthorityStatus packageAuthorityStatus) { this.packageAuthorityStatus = packageAuthorityStatus; }
}

