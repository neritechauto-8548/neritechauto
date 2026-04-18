package com.neritech.saas.fiscal.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "configuracoes_nfe")
public class ConfiguracaoNfe extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ambiente ambiente; // PRODUCAO, HOMOLOGACAO

    @Column(nullable = false)
    private Integer serie;

    @Column(nullable = false)
    private Long proximoNumero;

    @OneToOne
    @JoinColumn(name = "certificado_digital_id")
    private CertificadoDigital certificadoDigital;

    private String versaoLayout;

    private boolean enviarEmailDestinatario = true;

    private boolean salvarXml = true;

    public Ambiente getAmbiente() {
        return this.ambiente;
    }
    public void setAmbiente(Ambiente ambiente) {
        this.ambiente = ambiente;
    }
    public Integer getSerie() {
        return this.serie;
    }
    public void setSerie(Integer serie) {
        this.serie = serie;
    }
    public Long getProximoNumero() {
        return this.proximoNumero;
    }
    public void setProximoNumero(Long proximoNumero) {
        this.proximoNumero = proximoNumero;
    }
    public CertificadoDigital getCertificadoDigital() {
        return this.certificadoDigital;
    }
    public void setCertificadoDigital(CertificadoDigital certificadoDigital) {
        this.certificadoDigital = certificadoDigital;
    }
    public String getVersaoLayout() {
        return this.versaoLayout;
    }
    public void setVersaoLayout(String versaoLayout) {
        this.versaoLayout = versaoLayout;
    }
}
