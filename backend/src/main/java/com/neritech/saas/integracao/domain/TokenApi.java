package com.neritech.saas.integracao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tokens_api")
public class TokenApi extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "integracao_id")
    private IntegracaoAtiva integracao;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String token;

    private String tipo = "BEARER"; // BEARER, API_KEY, OAUTH

    private LocalDateTime dataExpiracao;

    private boolean ativo = true;

    @Column(columnDefinition = "TEXT")
    private String escopos; // JSON com permissÃƒÂµes/escopos

    public IntegracaoAtiva getIntegracao() {
        return this.integracao;
    }
    public void setIntegracao(IntegracaoAtiva integracao) {
        this.integracao = integracao;
    }
    public String getNome() {
        return this.nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public LocalDateTime getDataExpiracao() {
        return this.dataExpiracao;
    }
    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }
    public String getEscopos() {
        return this.escopos;
    }
    public void setEscopos(String escopos) {
        this.escopos = escopos;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isAtivo() {
        return this.ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
