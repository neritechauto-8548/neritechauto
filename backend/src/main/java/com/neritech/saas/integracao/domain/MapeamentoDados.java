package com.neritech.saas.integracao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
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
@Table(name = "mapeamentos_dados")
public class MapeamentoDados extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "integracao_id")
    private IntegracaoAtiva integracao;

    @Column(nullable = false)
    private String entidade; // PRODUTO, CLIENTE, PEDIDO, etc.

    @Column(nullable = false)
    private String campoOrigem;

    @Column(nullable = false)
    private String campoDestino;

    private String transformacao; // UPPERCASE, LOWERCASE, DATE_FORMAT, CUSTOM

    @Column(columnDefinition = "TEXT")
    private String regrasTransformacao; // JSON com regras de transformaÃƒÂ§ÃƒÂ£o

    private boolean ativo = true;

    public IntegracaoAtiva getIntegracao() {
        return this.integracao;
    }
    public void setIntegracao(IntegracaoAtiva integracao) {
        this.integracao = integracao;
    }
    public String getEntidade() {
        return this.entidade;
    }
    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }
    public String getCampoOrigem() {
        return this.campoOrigem;
    }
    public void setCampoOrigem(String campoOrigem) {
        this.campoOrigem = campoOrigem;
    }
    public String getCampoDestino() {
        return this.campoDestino;
    }
    public void setCampoDestino(String campoDestino) {
        this.campoDestino = campoDestino;
    }
    public String getTransformacao() {
        return this.transformacao;
    }
    public void setTransformacao(String transformacao) {
        this.transformacao = transformacao;
    }
    public String getRegrasTransformacao() {
        return this.regrasTransformacao;
    }
    public void setRegrasTransformacao(String regrasTransformacao) {
        this.regrasTransformacao = regrasTransformacao;
    }
}
