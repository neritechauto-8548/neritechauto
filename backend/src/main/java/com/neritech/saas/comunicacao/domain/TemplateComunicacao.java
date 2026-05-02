package com.neritech.saas.comunicacao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.CategoriaTemplate;
import com.neritech.saas.comunicacao.domain.enums.TipoTemplate;
import jakarta.persistence.*;

@Entity
@Table(name = "com_templates_comunicacao")
public class TemplateComunicacao extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_template", nullable = false, length = 30)
    private TipoTemplate tipoTemplate;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 30)
    private CategoriaTemplate categoria;

    @Column(name = "assunto", length = 255)
    private String assunto;

    @Column(name = "conteudo", nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "variaveis_disponiveis", columnDefinition = "TEXT")
    private String variaveisDisponiveis;

    @Column(name = "anexos_padrao", columnDefinition = "TEXT")
    private String anexosPadrao;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "padrao_categoria")
    private Boolean padraoCategoria = false;

    @Column(name = "idioma", length = 5)
    private String idioma = "pt-BR";

    @Column(name = "personalizavel")
    private Boolean personalizavel = true;

    @Column(name = "requer_aprovacao")
    private Boolean requerAprovacao = false;

    @Column(name = "tags", length = 255)
    private String tags;



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

    public TipoTemplate getTipoTemplate() {
        return tipoTemplate;
    }

    public void setTipoTemplate(TipoTemplate tipoTemplate) {
        this.tipoTemplate = tipoTemplate;
    }

    public CategoriaTemplate getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaTemplate categoria) {
        this.categoria = categoria;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getVariaveisDisponiveis() {
        return variaveisDisponiveis;
    }

    public void setVariaveisDisponiveis(String variaveisDisponiveis) {
        this.variaveisDisponiveis = variaveisDisponiveis;
    }

    public String getAnexosPadrao() {
        return anexosPadrao;
    }

    public void setAnexosPadrao(String anexosPadrao) {
        this.anexosPadrao = anexosPadrao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getPadraoCategoria() {
        return padraoCategoria;
    }

    public void setPadraoCategoria(Boolean padraoCategoria) {
        this.padraoCategoria = padraoCategoria;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Boolean getPersonalizavel() {
        return personalizavel;
    }

    public void setPersonalizavel(Boolean personalizavel) {
        this.personalizavel = personalizavel;
    }

    public Boolean getRequerAprovacao() {
        return requerAprovacao;
    }

    public void setRequerAprovacao(Boolean requerAprovacao) {
        this.requerAprovacao = requerAprovacao;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }


}
