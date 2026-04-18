package com.neritech.saas.rh.domain;

import com.neritech.saas.agendamento.domain.enums.SolicitadoPor;
import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.rh.domain.enums.StatusFerias;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ferias_funcionarios")
public class FeriasFuncionario extends BaseEntity {

    @Column(name = "funcionario_id", nullable = false)
    private Long funcionarioId;

    @Column(name = "periodo_aquisitivo_inicio", nullable = false)
    private LocalDate periodoAquisitivoInicio;

    @Column(name = "periodo_aquisitivo_fim", nullable = false)
    private LocalDate periodoAquisitivoFim;

    @Column(name = "dias_direito", nullable = false)
    private Integer diasDireito = 30;

    @Column(name = "dias_gozados")
    private Integer diasGozados = 0;

    @Column(name = "dias_restantes")
    private Integer diasRestantes = 30;

    @Column(name = "data_inicio_ferias")
    private LocalDate dataInicioFerias;

    @Column(name = "data_fim_ferias")
    private LocalDate dataFimFerias;

    @Column(name = "data_retorno_previsto")
    private LocalDate dataRetornoPrevisto;

    @Column(name = "abono_pecuniario")
    private Boolean abonoPecuniario = false;

    @Column(name = "dias_abono")
    private Integer diasAbono = 0;

    @Column(name = "fracionada")
    private Boolean fracionada = false;

    @Column(name = "numero_fracao")
    private Integer numeroFracao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusFerias status;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "solicitado_por")
    private Long solicitadoPor;

    @Column(name = "data_solicitacao")
    private LocalDate dataSolicitacao;

    @Column(name = "aprovado_por")
    private Long aprovadoPor;

    @Column(name = "data_aprovacao")
    private LocalDate dataAprovacao;

    @Column(name = "cancelado_por")
    private Long canceladoPor;

    @Column(name = "data_cancelamento")
    private LocalDate dataCancelamento;

    @Column(name = "motivo_cancelamento", columnDefinition = "TEXT")
    private String motivoCancelamento;

    // Getters and Setters
    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public LocalDate getPeriodoAquisitivoInicio() {
        return periodoAquisitivoInicio;
    }

    public void setPeriodoAquisitivoInicio(LocalDate periodoAquisitivoInicio) {
        this.periodoAquisitivoInicio = periodoAquisitivoInicio;
    }

    public LocalDate getPeriodoAquisitivoFim() {
        return periodoAquisitivoFim;
    }

    public void setPeriodoAquisitivoFim(LocalDate periodoAquisitivoFim) {
        this.periodoAquisitivoFim = periodoAquisitivoFim;
    }

    public Integer getDiasDireito() {
        return diasDireito;
    }

    public void setDiasDireito(Integer diasDireito) {
        this.diasDireito = diasDireito;
    }

    public Integer getDiasGozados() {
        return diasGozados;
    }

    public void setDiasGozados(Integer diasGozados) {
        this.diasGozados = diasGozados;
    }

    public Integer getDiasRestantes() {
        return diasRestantes;
    }

    public void setDiasRestantes(Integer diasRestantes) {
        this.diasRestantes = diasRestantes;
    }

    public LocalDate getDataInicioFerias() {
        return dataInicioFerias;
    }

    public void setDataInicioFerias(LocalDate dataInicioFerias) {
        this.dataInicioFerias = dataInicioFerias;
    }

    public LocalDate getDataFimFerias() {
        return dataFimFerias;
    }

    public void setDataFimFerias(LocalDate dataFimFerias) {
        this.dataFimFerias = dataFimFerias;
    }

    public LocalDate getDataRetornoPrevisto() {
        return dataRetornoPrevisto;
    }

    public void setDataRetornoPrevisto(LocalDate dataRetornoPrevisto) {
        this.dataRetornoPrevisto = dataRetornoPrevisto;
    }

    public Boolean getAbonoPecuniario() {
        return abonoPecuniario;
    }

    public void setAbonoPecuniario(Boolean abonoPecuniario) {
        this.abonoPecuniario = abonoPecuniario;
    }

    public Integer getDiasAbono() {
        return diasAbono;
    }

    public void setDiasAbono(Integer diasAbono) {
        this.diasAbono = diasAbono;
    }

    public Boolean getFracionada() {
        return fracionada;
    }

    public void setFracionada(Boolean fracionada) {
        this.fracionada = fracionada;
    }

    public Integer getNumeroFracao() {
        return numeroFracao;
    }

    public void setNumeroFracao(Integer numeroFracao) {
        this.numeroFracao = numeroFracao;
    }

    public StatusFerias getStatus() {
        return status;
    }

    public void setStatus(StatusFerias status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getSolicitadoPor() {
        return solicitadoPor;
    }

    public void setSolicitadoPor(Long solicitadoPor) {
        this.solicitadoPor = solicitadoPor;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDate dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public Long getAprovadoPor() {
        return aprovadoPor;
    }

    public void setAprovadoPor(Long aprovadoPor) {
        this.aprovadoPor = aprovadoPor;
    }

    public LocalDate getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(LocalDate dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public Long getCanceladoPor() {
        return canceladoPor;
    }

    public void setCanceladoPor(Long canceladoPor) {
        this.canceladoPor = canceladoPor;
    }

    public LocalDate getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(LocalDate dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }
}
