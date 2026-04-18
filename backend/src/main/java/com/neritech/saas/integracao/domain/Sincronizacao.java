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
@Table(name = "sincronizacoes")
public class Sincronizacao extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "integracao_id")
    private IntegracaoAtiva integracao;

    @Column(nullable = false)
    private String entidade; // PRODUTOS, CLIENTES, PEDIDOS, ESTOQUE, etc.

    @Column(nullable = false)
    private String direcao; // ENVIAR, RECEBER, BIDIRECIONAL

    @Column(nullable = false)
    private String status; // AGENDADA, EM_ANDAMENTO, CONCLUIDA, ERRO

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    private Integer registrosProcessados = 0;

    private Integer registrosComErro = 0;

    @Column(columnDefinition = "TEXT")
    private String logErros;

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
    public String getDirecao() {
        return this.direcao;
    }
    public void setDirecao(String direcao) {
        this.direcao = direcao;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getDataInicio() {
        return this.dataInicio;
    }
    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }
    public LocalDateTime getDataFim() {
        return this.dataFim;
    }
    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }
    public String getLogErros() {
        return this.logErros;
    }
    public void setLogErros(String logErros) {
        this.logErros = logErros;
    }
}
