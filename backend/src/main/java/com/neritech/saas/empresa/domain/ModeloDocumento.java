package com.neritech.saas.empresa.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.enums.OrientacaoDocumento;
import com.neritech.saas.empresa.domain.enums.TamanhoPapel;
import com.neritech.saas.empresa.domain.enums.TipoDocumento;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "modelos_documentos")
public class ModeloDocumento extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "tipo_documento", length = 20)
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @NotBlank
    @Column(name = "template_html", columnDefinition = "TEXT", nullable = false)
    private String templateHtml;

    @Column(name = "template_css", columnDefinition = "TEXT")
    private String templateCss;

    @Column(name = "variaveis_disponiveis", columnDefinition = "TEXT")
    private String variaveisDisponiveis;

    @Column(name = "orientacao", length = 20)
    @Enumerated(EnumType.STRING)
    private OrientacaoDocumento orientacao = OrientacaoDocumento.RETRATO;

    @Column(name = "tamanho_papel", length = 20)
    @Enumerated(EnumType.STRING)
    private TamanhoPapel tamanhoPapel = TamanhoPapel.A4;

    @Column(name = "margens_cm", length = 20)
    private String margensCm = "2,2,2,2";

    @Column(name = "cabecalho_padrao")
    private Boolean cabecalhoPadrao = true;

    @Column(name = "rodape_padrao")
    private Boolean rodapePadrao = true;

    @Column(name = "numeracao_automatica")
    private Boolean numeracaoAutomatica = true;

    @Column(name = "padrao")
    private Boolean padrao = false;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "criado_por")
    private Long criadoPor;

    @Column(name = "atualizado_por")
    private Long atualizadoPor;

    public ModeloDocumento() {
    }

    // Getters and Setters
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTemplateHtml() {
        return templateHtml;
    }

    public void setTemplateHtml(String templateHtml) {
        this.templateHtml = templateHtml;
    }

    public String getTemplateCss() {
        return templateCss;
    }

    public void setTemplateCss(String templateCss) {
        this.templateCss = templateCss;
    }

    public String getVariaveisDisponiveis() {
        return variaveisDisponiveis;
    }

    public void setVariaveisDisponiveis(String variaveisDisponiveis) {
        this.variaveisDisponiveis = variaveisDisponiveis;
    }

    public OrientacaoDocumento getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(OrientacaoDocumento orientacao) {
        this.orientacao = orientacao;
    }

    public TamanhoPapel getTamanhoPapel() {
        return tamanhoPapel;
    }

    public void setTamanhoPapel(TamanhoPapel tamanhoPapel) {
        this.tamanhoPapel = tamanhoPapel;
    }

    public String getMargensCm() {
        return margensCm;
    }

    public void setMargensCm(String margensCm) {
        this.margensCm = margensCm;
    }

    public Boolean getCabecalhoPadrao() {
        return cabecalhoPadrao;
    }

    public void setCabecalhoPadrao(Boolean cabecalhoPadrao) {
        this.cabecalhoPadrao = cabecalhoPadrao;
    }

    public Boolean getRodapePadrao() {
        return rodapePadrao;
    }

    public void setRodapePadrao(Boolean rodapePadrao) {
        this.rodapePadrao = rodapePadrao;
    }

    public Boolean getNumeracaoAutomatica() {
        return numeracaoAutomatica;
    }

    public void setNumeracaoAutomatica(Boolean numeracaoAutomatica) {
        this.numeracaoAutomatica = numeracaoAutomatica;
    }

    public Boolean getPadrao() {
        return padrao;
    }

    public void setPadrao(Boolean padrao) {
        this.padrao = padrao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(Long criadoPor) {
        this.criadoPor = criadoPor;
    }

    public Long getAtualizadoPor() {
        return atualizadoPor;
    }

    public void setAtualizadoPor(Long atualizadoPor) {
        this.atualizadoPor = atualizadoPor;
    }
}
