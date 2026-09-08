package com.neritech.saas.orcamento.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalog_kit_versions")
public class CatalogKitVersion extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kit_id", nullable = false)
    private CatalogKit kit;

    @Column(name = "version_number", nullable = false)
    private int versionNumber;

    @Column(name = "title_snapshot", nullable = false, length = 120)
    private String titleSnapshot;

    @Column(name = "description_snapshot", columnDefinition = "TEXT")
    private String descriptionSnapshot;

    @Column(name = "recommended_default", nullable = false)
    private boolean recommendedDefault;

    @Column(name = "published", nullable = false)
    private boolean published = true;

    public CatalogKit getKit() { return kit; }
    public void setKit(CatalogKit kit) { this.kit = kit; }
    public int getVersionNumber() { return versionNumber; }
    public void setVersionNumber(int versionNumber) { this.versionNumber = versionNumber; }
    public String getTitleSnapshot() { return titleSnapshot; }
    public void setTitleSnapshot(String titleSnapshot) { this.titleSnapshot = titleSnapshot; }
    public String getDescriptionSnapshot() { return descriptionSnapshot; }
    public void setDescriptionSnapshot(String descriptionSnapshot) { this.descriptionSnapshot = descriptionSnapshot; }
    public boolean isRecommendedDefault() { return recommendedDefault; }
    public void setRecommendedDefault(boolean recommendedDefault) { this.recommendedDefault = recommendedDefault; }
    public boolean isPublished() { return published; }
    public void setPublished(boolean published) { this.published = published; }
}
