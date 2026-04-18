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
@Table(name = "webhooks")
public class Webhook extends BaseEntity {

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String urlDestino;

    @Column(nullable = false)
    private String evento; // PEDIDO_CRIADO, PAGAMENTO_CONFIRMADO, ESTOQUE_ATUALIZADO, etc.

    private String metodoHttp = "POST";

    @Column(columnDefinition = "TEXT")
    private String headers; // JSON com headers customizados

    private String secretKey; // Para validaÃƒÂ§ÃƒÂ£o de assinatura

    private boolean ativo = true;

    private Integer tentativasMaximas = 3;

    public String getNome() {
        return this.nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getUrlDestino() {
        return this.urlDestino;
    }
    public void setUrlDestino(String urlDestino) {
        this.urlDestino = urlDestino;
    }
    public String getEvento() {
        return this.evento;
    }
    public void setEvento(String evento) {
        this.evento = evento;
    }
    public String getHeaders() {
        return this.headers;
    }
    public void setHeaders(String headers) {
        this.headers = headers;
    }
    public String getSecretKey() {
        return this.secretKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getMetodoHttp() {
        return this.metodoHttp;
    }

    public void setMetodoHttp(String metodoHttp) {
        this.metodoHttp = metodoHttp;
    }

    public boolean isAtivo() {
        return this.ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getTentativasMaximas() {
        return this.tentativasMaximas;
    }

    public void setTentativasMaximas(Integer tentativasMaximas) {
        this.tentativasMaximas = tentativasMaximas;
    }
}
