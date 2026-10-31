package com.neritech.saas.empresa.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "seguradoras")
public class Seguradora extends TenantEntity {
    @Column(name = "cnpj", length = 18)
    private String cnpj;
    @Column(name = "razao_social", nullable = false, length = 255)
    private String razaoSocial;
    @Column(name = "nome_fantasia", length = 255)
    private String nomeFantasia;
    @Column(name = "inscricao_estadual", length = 50)
    private String inscricaoEstadual;
    @Column(name = "inscricao_municipal", length = 50)
    private String inscricaoMunicipal;
    @Column(name = "email", length = 255)
    private String email;
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
    @Column(name = "margem_refatoramento", precision = 5, scale = 2)
    private BigDecimal margemRefatoramento;
    @Column(name = "site", length = 500)
    private String site;
    @Column(name = "retencao_iss", nullable = false)
    private Boolean retencaoIss = false;
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    public String getCnpj() { return cnpj; }
    public void setCnpj(String value) { cnpj = value; }
    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String value) { razaoSocial = value; }
    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String value) { nomeFantasia = value; }
    public String getInscricaoEstadual() { return inscricaoEstadual; }
    public void setInscricaoEstadual(String value) { inscricaoEstadual = value; }
    public String getInscricaoMunicipal() { return inscricaoMunicipal; }
    public void setInscricaoMunicipal(String value) { inscricaoMunicipal = value; }
    public String getEmail() { return email; }
    public void setEmail(String value) { email = value; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String value) { observacoes = value; }
    public BigDecimal getMargemRefatoramento() { return margemRefatoramento; }
    public void setMargemRefatoramento(BigDecimal value) { margemRefatoramento = value; }
    public String getSite() { return site; }
    public void setSite(String value) { site = value; }
    public Boolean getRetencaoIss() { return retencaoIss; }
    public void setRetencaoIss(Boolean value) { retencaoIss = value; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean value) { ativo = value; }
}
