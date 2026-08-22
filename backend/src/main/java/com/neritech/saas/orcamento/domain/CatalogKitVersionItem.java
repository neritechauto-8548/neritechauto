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
@Table(name = "catalog_kit_version_items")
public class CatalogKitVersionItem extends TenantEntity {

    public enum LineType { PART, LABOR }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kit_version_id", nullable = false)
    private CatalogKitVersion kitVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "line_type", nullable = false, length = 24)
    private LineType lineType;

    @Column(name = "catalog_item_id", nullable = false)
    private Long catalogItemId;

    @Column(name = "catalog_version")
    private Integer catalogVersion;

    @Column(name = "description_snapshot", nullable = false, length = 255)
    private String descriptionSnapshot;

    @Column(name = "reference_snapshot", length = 100)
    private String referenceSnapshot;

    @Column(name = "quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    @Column(name = "unit_price_snapshot", nullable = false, precision = 14, scale = 4)
    private BigDecimal unitPriceSnapshot;

    @Column(name = "position", nullable = false)
    private int position;

    public CatalogKitVersion getKitVersion() { return kitVersion; }
    public void setKitVersion(CatalogKitVersion kitVersion) { this.kitVersion = kitVersion; }
    public LineType getLineType() { return lineType; }
    public void setLineType(LineType lineType) { this.lineType = lineType; }
    public Long getCatalogItemId() { return catalogItemId; }
    public void setCatalogItemId(Long catalogItemId) { this.catalogItemId = catalogItemId; }
    public Integer getCatalogVersion() { return catalogVersion; }
    public void setCatalogVersion(Integer catalogVersion) { this.catalogVersion = catalogVersion; }
    public String getDescriptionSnapshot() { return descriptionSnapshot; }
    public void setDescriptionSnapshot(String descriptionSnapshot) { this.descriptionSnapshot = descriptionSnapshot; }
    public String getReferenceSnapshot() { return referenceSnapshot; }
    public void setReferenceSnapshot(String referenceSnapshot) { this.referenceSnapshot = referenceSnapshot; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPriceSnapshot() { return unitPriceSnapshot; }
    public void setUnitPriceSnapshot(BigDecimal unitPriceSnapshot) { this.unitPriceSnapshot = unitPriceSnapshot; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
}
