package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.rh.domain.enums.NivelComplexidade;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "especialidades")
public class Especialidade extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "categoria", length = 50)
    private String categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_complexidade", length = 20)
    private NivelComplexidade nivelComplexidade;

    @Column(name = "certificacao_obrigatoria")
    private Boolean certificacaoObrigatoria = false;

    @Column(name = "nome_certificacao", length = 255)
    private String nomeCertificacao;

    @Column(name = "entidade_certificadora", length = 255)
    private String entidadeCertificadora;

    @Column(name = "validade_certificacao_meses")
    private Integer validadeCertificacaoMeses;

    @Column(name = "experiencia_minima_anos")
    private Integer experienciaMinimaAnos = 0;

    @Column(name = "adicional_salarial_percentual", precision = 5, scale = 2)
    private BigDecimal adicionalSalarialPercentual = BigDecimal.ZERO;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "criado_por")
    private Long criadoPor;

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public NivelComplexidade getNivelComplexidade() {
        return nivelComplexidade;
    }

    public void setNivelComplexidade(NivelComplexidade nivelComplexidade) {
        this.nivelComplexidade = nivelComplexidade;
    }

    public Boolean getCertificacaoObrigatoria() {
        return certificacaoObrigatoria;
    }

    public void setCertificacaoObrigatoria(Boolean certificacaoObrigatoria) {
        this.certificacaoObrigatoria = certificacaoObrigatoria;
    }

    public String getNomeCertificacao() {
        return nomeCertificacao;
    }

    public void setNomeCertificacao(String nomeCertificacao) {
        this.nomeCertificacao = nomeCertificacao;
    }

    public String getEntidadeCertificadora() {
        return entidadeCertificadora;
    }

    public void setEntidadeCertificadora(String entidadeCertificadora) {
        this.entidadeCertificadora = entidadeCertificadora;
    }

    public Integer getValidadeCertificacaoMeses() {
        return validadeCertificacaoMeses;
    }

    public void setValidadeCertificacaoMeses(Integer validadeCertificacaoMeses) {
        this.validadeCertificacaoMeses = validadeCertificacaoMeses;
    }

    public Integer getExperienciaMinimaAnos() {
        return experienciaMinimaAnos;
    }

    public void setExperienciaMinimaAnos(Integer experienciaMinimaAnos) {
        this.experienciaMinimaAnos = experienciaMinimaAnos;
    }

    public BigDecimal getAdicionalSalarialPercentual() {
        return adicionalSalarialPercentual;
    }

    public void setAdicionalSalarialPercentual(BigDecimal adicionalSalarialPercentual) {
        this.adicionalSalarialPercentual = adicionalSalarialPercentual;
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
}
