package com.neritech.saas.integracao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
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
@Table(name = "integracoes_ativas")
public class IntegracaoAtiva extends BaseEntity {

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String tipo; // ERP, CRM, E-COMMERCE, MARKETPLACE, PAGAMENTO, etc.

    @Column(nullable = false)
    private String urlBase;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private boolean ativo = true;

    @Column(columnDefinition = "json")
    private String configuracoes; // JSON com configuraÃƒÂ§ÃƒÂµes especÃƒÂ­ficas

    public String getNome() {
        return this.nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getTipo() {
        return this.tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getUrlBase() {
        return this.urlBase;
    }
    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }
    public String getDescricao() {
        return this.descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getConfiguracoes() {
        return this.configuracoes;
    }
    public void setConfiguracoes(String configuracoes) {
        this.configuracoes = configuracoes;
    }
}
