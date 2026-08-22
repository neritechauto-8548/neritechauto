package com.neritech.saas.orcamento.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "estimate_discount_authority_limits")
public class OrcamentoDiscountAuthorityLimit extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "funcao_id", nullable = false)
    private Funcao funcao;

    @Column(name = "max_percentage", nullable = false, precision = 7, scale = 4)
    private BigDecimal maxPercentage;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public Funcao getFuncao() { return funcao; }
    public void setFuncao(Funcao funcao) { this.funcao = funcao; }
    public BigDecimal getMaxPercentage() { return maxPercentage; }
    public void setMaxPercentage(BigDecimal maxPercentage) { this.maxPercentage = maxPercentage; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

