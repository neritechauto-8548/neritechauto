package com.neritech.saas.empresa.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "situacao")
public class Situacao extends BaseEntity {

    @NotBlank
    @Size(max = 255)
    @Column(name = "nm_situacao", nullable = false, length = 255)
    private String nmSituacao;

    @Size(max = 1000)
    @Column(name = "ds_situacao", length = 1000)
    private String dsSituacao;

    @Size(max = 7)
    @Column(name = "cor_situacao", length = 7)
    private String corSituacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    public String getNmSituacao() {
        return nmSituacao;
    }

    public void setNmSituacao(String nmSituacao) {
        this.nmSituacao = nmSituacao;
    }

    public String getDsSituacao() {
        return dsSituacao;
    }

    public void setDsSituacao(String dsSituacao) {
        this.dsSituacao = dsSituacao;
    }

    public String getCorSituacao() {
        return corSituacao;
    }

    public void setCorSituacao(String corSituacao) {
        this.corSituacao = corSituacao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}

