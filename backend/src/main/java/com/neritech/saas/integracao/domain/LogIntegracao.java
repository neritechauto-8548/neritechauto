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
@Table(name = "logs_integracoes")
public class LogIntegracao extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "integracao_id")
    private IntegracaoAtiva integracao;

    @Column(nullable = false)
    private String operacao; // GET, POST, PUT, DELETE, SYNC

    @Column(nullable = false)
    private String status; // SUCESSO, ERRO, PENDENTE

    @Column(columnDefinition = "TEXT")
    private String requisicao;

    @Column(columnDefinition = "TEXT")
    private String resposta;

    @Column(columnDefinition = "TEXT")
    private String mensagemErro;

    private Integer codigoHttp;

    private LocalDateTime dataExecucao;

    public IntegracaoAtiva getIntegracao() {
        return this.integracao;
    }
    public void setIntegracao(IntegracaoAtiva integracao) {
        this.integracao = integracao;
    }
    public String getOperacao() {
        return this.operacao;
    }
    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRequisicao() {
        return this.requisicao;
    }
    public void setRequisicao(String requisicao) {
        this.requisicao = requisicao;
    }
    public String getResposta() {
        return this.resposta;
    }
    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
    public String getMensagemErro() {
        return this.mensagemErro;
    }
    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }
    public Integer getCodigoHttp() {
        return this.codigoHttp;
    }
    public void setCodigoHttp(Integer codigoHttp) {
        this.codigoHttp = codigoHttp;
    }
    public LocalDateTime getDataExecucao() {
        return this.dataExecucao;
    }
    public void setDataExecucao(LocalDateTime dataExecucao) {
        this.dataExecucao = dataExecucao;
    }
}
