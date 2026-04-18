package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.financeiro.domain.enums.StatusConciliacao;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fin_conciliacoes_bancarias")
@Getter
@Setter
public class ConciliacaoBancaria extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_bancaria_id")
    private ContaBancaria contaBancaria;

    @Column(name = "periodo_inicio", nullable = false)
    private LocalDate periodoInicio;

    @Column(name = "periodo_fim", nullable = false)
    private LocalDate periodoFim;

    @Column(name = "saldo_inicial_sistema", nullable = false)
    private BigDecimal saldoInicialSistema;

    @Column(name = "saldo_final_sistema", nullable = false)
    private BigDecimal saldoFinalSistema;

    @Column(name = "saldo_inicial_extrato", nullable = false)
    private BigDecimal saldoInicialExtrato;

    @Column(name = "saldo_final_extrato", nullable = false)
    private BigDecimal saldoFinalExtrato;

    @Column(name = "diferenca_encontrada")
    private BigDecimal diferencaEncontrada;

    @Column(name = "total_movimentacoes_sistema")
    private Integer totalMovimentacoesSistema;

    @Column(name = "total_movimentacoes_extrato")
    private Integer totalMovimentacoesExtrato;

    @Column(name = "movimentacoes_nao_conciliadas")
    private Integer movimentacoesNaoConciliadas = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private StatusConciliacao status;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "arquivo_extrato_url", length = 500)
    private String arquivoExtratoUrl;

    @Column(name = "responsavel_conciliacao", nullable = false)
    private Long responsavelConciliacao;

    @Column(name = "data_inicio_conciliacao")
    private LocalDateTime dataInicioConciliacao = LocalDateTime.now();

    @Column(name = "data_fim_conciliacao")
    private LocalDateTime dataFimConciliacao;

    @Column(name = "aprovada_por")
    private Long aprovadaPor;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    public ContaBancaria getContaBancaria() {
        return this.contaBancaria;
    }
    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }
    public LocalDate getPeriodoInicio() {
        return this.periodoInicio;
    }
    public void setPeriodoInicio(LocalDate periodoInicio) {
        this.periodoInicio = periodoInicio;
    }
    public LocalDate getPeriodoFim() {
        return this.periodoFim;
    }
    public void setPeriodoFim(LocalDate periodoFim) {
        this.periodoFim = periodoFim;
    }
    public BigDecimal getSaldoInicialSistema() {
        return this.saldoInicialSistema;
    }
    public void setSaldoInicialSistema(BigDecimal saldoInicialSistema) {
        this.saldoInicialSistema = saldoInicialSistema;
    }
    public BigDecimal getSaldoFinalSistema() {
        return this.saldoFinalSistema;
    }
    public void setSaldoFinalSistema(BigDecimal saldoFinalSistema) {
        this.saldoFinalSistema = saldoFinalSistema;
    }
    public BigDecimal getSaldoInicialExtrato() {
        return this.saldoInicialExtrato;
    }
    public void setSaldoInicialExtrato(BigDecimal saldoInicialExtrato) {
        this.saldoInicialExtrato = saldoInicialExtrato;
    }
    public BigDecimal getSaldoFinalExtrato() {
        return this.saldoFinalExtrato;
    }
    public void setSaldoFinalExtrato(BigDecimal saldoFinalExtrato) {
        this.saldoFinalExtrato = saldoFinalExtrato;
    }
    public BigDecimal getDiferencaEncontrada() {
        return this.diferencaEncontrada;
    }
    public void setDiferencaEncontrada(BigDecimal diferencaEncontrada) {
        this.diferencaEncontrada = diferencaEncontrada;
    }
    public Integer getTotalMovimentacoesSistema() {
        return this.totalMovimentacoesSistema;
    }
    public void setTotalMovimentacoesSistema(Integer totalMovimentacoesSistema) {
        this.totalMovimentacoesSistema = totalMovimentacoesSistema;
    }
    public Integer getTotalMovimentacoesExtrato() {
        return this.totalMovimentacoesExtrato;
    }
    public void setTotalMovimentacoesExtrato(Integer totalMovimentacoesExtrato) {
        this.totalMovimentacoesExtrato = totalMovimentacoesExtrato;
    }
    public StatusConciliacao getStatus() {
        return this.status;
    }
    public void setStatus(StatusConciliacao status) {
        this.status = status;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    public String getArquivoExtratoUrl() {
        return this.arquivoExtratoUrl;
    }
    public void setArquivoExtratoUrl(String arquivoExtratoUrl) {
        this.arquivoExtratoUrl = arquivoExtratoUrl;
    }
    public Long getResponsavelConciliacao() {
        return this.responsavelConciliacao;
    }
    public void setResponsavelConciliacao(Long responsavelConciliacao) {
        this.responsavelConciliacao = responsavelConciliacao;
    }
    public LocalDateTime getDataFimConciliacao() {
        return this.dataFimConciliacao;
    }
    public void setDataFimConciliacao(LocalDateTime dataFimConciliacao) {
        this.dataFimConciliacao = dataFimConciliacao;
    }
    public Long getAprovadaPor() {
        return this.aprovadaPor;
    }
    public void setAprovadaPor(Long aprovadaPor) {
        this.aprovadaPor = aprovadaPor;
    }
    public LocalDateTime getDataAprovacao() {
        return this.dataAprovacao;
    }
    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }
}
