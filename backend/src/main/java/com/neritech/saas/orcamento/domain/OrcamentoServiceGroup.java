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

@Entity
@Table(name = "estimate_service_groups")
public class OrcamentoServiceGroup extends TenantEntity {

    public enum Visibility { CUSTOMER_VISIBLE, INTERNAL_ONLY }

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
}
