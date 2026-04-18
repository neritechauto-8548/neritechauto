package com.neritech.saas.fiscal.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "aliquotas_impostos")
public class AliquotaImposto extends BaseEntity {

    @Column(nullable = false)
    private String nomeImposto; // ICMS, ISS, PIS, COFINS

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal aliquota;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String uf; // Para ICMS por estado

    private boolean padrao = false;

    public String getNomeImposto() {
        return this.nomeImposto;
    }
    public void setNomeImposto(String nomeImposto) {
        this.nomeImposto = nomeImposto;
    }
    public BigDecimal getAliquota() {
        return this.aliquota;
    }
    public void setAliquota(BigDecimal aliquota) {
        this.aliquota = aliquota;
    }
    public String getDescricao() {
        return this.descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getUf() {
        return this.uf;
    }
    public void setUf(String uf) {
        this.uf = uf;
    }
}
