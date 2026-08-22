package com.neritech.saas.orcamento.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
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
@Table(name = "estimate_line_items")
public class OrcamentoLineItem extends TenantEntity {

    public enum LineType { PART, LABOR, FEE, SUBLET, DISCOUNT, NOTE }
    public enum Source { PRODUCT_CATALOG, SERVICE_CATALOG, KIT, MANUAL }
    public enum AvailabilityStatus { AVAILABLE, PARTIAL, NEEDED, NOT_APPLICABLE }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_group_id", nullable = false)
    private OrcamentoServiceGroup group;

    @Enumerated(EnumType.STRING)
    @Column(name = "line_type", nullable = false, length = 24)
    private LineType lineType;

    @Column(name = "catalog_item_id")
    private Long catalogItemId;

    @Column(name = "catalog_version")
    private Integer catalogVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false, length = 24)
    private Source source;

    @Column(name = "description_snapshot", nullable = false, length = 255)
    private String descriptionSnapshot;

    @Column(name = "reference_snapshot", length = 100)
    private String referenceSnapshot;

    @Column(name = "quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    @Column(name = "unit_price", nullable = false, precision = 14, scale = 4)
    private BigDecimal unitPrice;

    @Column(name = "discount_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability_status", nullable = false, length = 24)
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.NOT_APPLICABLE;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "kit_origin_id")
    private Long kitOriginId;

    @Column(name = "kit_origin_version")
    private Integer kitOriginVersion;

    public OrcamentoServiceGroup getGroup() { return group; }
    public void setGroup(OrcamentoServiceGroup group) { this.group = group; }
    public LineType getLineType() { return lineType; }
    public void setLineType(LineType lineType) { this.lineType = lineType; }
    public Long getCatalogItemId() { return catalogItemId; }
    public void setCatalogItemId(Long catalogItemId) { this.catalogItemId = catalogItemId; }
    public Integer getCatalogVersion() { return catalogVersion; }
    public void setCatalogVersion(Integer catalogVersion) { this.catalogVersion = catalogVersion; }
    public Source getSource() { return source; }
    public void setSource(Source source) { this.source = source; }
    public String getDescriptionSnapshot() { return descriptionSnapshot; }
    public void setDescriptionSnapshot(String descriptionSnapshot) { this.descriptionSnapshot = descriptionSnapshot; }
    public String getReferenceSnapshot() { return referenceSnapshot; }
    public void setReferenceSnapshot(String referenceSnapshot) { this.referenceSnapshot = referenceSnapshot; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public AvailabilityStatus getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) { this.availabilityStatus = availabilityStatus; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public Long getKitOriginId() { return kitOriginId; }
    public void setKitOriginId(Long kitOriginId) { this.kitOriginId = kitOriginId; }
    public Integer getKitOriginVersion() { return kitOriginVersion; }
    public void setKitOriginVersion(Integer kitOriginVersion) { this.kitOriginVersion = kitOriginVersion; }
}
