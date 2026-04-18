package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.financeiro.domain.enums.StatusPagamento;
import com.neritech.saas.financeiro.domain.enums.TipoComissao;
import com.neritech.saas.rh.domain.enums.BaseCalculoComissao;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "comissoes")
public class Comissao extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "funcionario_id", nullable = false)
    private Long funcionarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comissao", length = 20)
    private TipoComissao tipoComissao;

    @Column(name = "periodo_referencia", nullable = false, length = 7)
    private String periodoReferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "base_calculo", length = 20)
    private BaseCalculoComissao baseCalculo;

    @Column(name = "valor_base", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorBase;

    @Column(name = "percentual_comissao", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentualComissao;

    @Column(name = "valor_comissao", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorComissao;

    @Column(name = "meta_estabelecida", precision = 12, scale = 2)
    private BigDecimal metaEstabelecida;

    @Column(name = "meta_atingida", precision = 12, scale = 2)
    private BigDecimal metaAtingida;

    @Column(name = "percentual_meta_atingido", precision = 5, scale = 2)
    private BigDecimal percentualMetaAtingido;

    @Column(name = "bonus_meta", precision = 8, scale = 2)
    private BigDecimal bonusMeta = BigDecimal.ZERO;

    @Column(name = "desconto_retrabalho", precision = 8, scale = 2)
    private BigDecimal descontoRetrabalho = BigDecimal.ZERO;

    @Column(name = "valor_liquido", precision = 10, scale = 2)
    private BigDecimal valorLiquido;

    @Column(name = "data_competencia", nullable = false)
    private LocalDate dataCompetencia;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", length = 20)
    private StatusPagamento statusPagamento;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "aprovada_por")
    private Long aprovadaPor;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "paga_por")
    private Long pagaPor;

    // Getters and Setters omitted for brevity - will be generated
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public TipoComissao getTipoComissao() {
        return tipoComissao;
    }

    public void setTipoComissao(TipoComissao tipoComissao) {
        this.tipoComissao = tipoComissao;
    }

    public String getPeriodoReferencia() {
        return periodoReferencia;
    }

    public void setPeriodoReferencia(String periodoReferencia) {
        this.periodoReferencia = periodoReferencia;
    }

    public BaseCalculoComissao getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(BaseCalculoComissao baseCalculo) {
        this.baseCalculo = baseCalculo;
    }

    public BigDecimal getValorBase() {
        return valorBase;
    }

    public void setValorBase(BigDecimal valorBase) {
        this.valorBase = valorBase;
    }

    public BigDecimal getPercentualComissao() {
        return percentualComissao;
    }

    public void setPercentualComissao(BigDecimal percentualComissao) {
        this.percentualComissao = percentualComissao;
    }

    public BigDecimal getValorComissao() {
        return valorComissao;
    }

    public void setValorComissao(BigDecimal valorComissao) {
        this.valorComissao = valorComissao;
    }

    public BigDecimal getMetaEstabelecida() {
        return metaEstabelecida;
    }

    public void setMetaEstabelecida(BigDecimal metaEstabelecida) {
        this.metaEstabelecida = metaEstabelecida;
    }

    public BigDecimal getMetaAtingida() {
        return metaAtingida;
    }

    public void setMetaAtingida(BigDecimal metaAtingida) {
        this.metaAtingida = metaAtingida;
    }

    public BigDecimal getPercentualMetaAtingido() {
        return percentualMetaAtingido;
    }

    public void setPercentualMetaAtingido(BigDecimal percentualMetaAtingido) {
        this.percentualMetaAtingido = percentualMetaAtingido;
    }

    public BigDecimal getBonusMeta() {
        return bonusMeta;
    }

    public void setBonusMeta(BigDecimal bonusMeta) {
        this.bonusMeta = bonusMeta;
    }

    public BigDecimal getDescontoRetrabalho() {
        return descontoRetrabalho;
    }

    public void setDescontoRetrabalho(BigDecimal descontoRetrabalho) {
        this.descontoRetrabalho = descontoRetrabalho;
    }

    public BigDecimal getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(BigDecimal valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    public LocalDate getDataCompetencia() {
        return dataCompetencia;
    }

    public void setDataCompetencia(LocalDate dataCompetencia) {
        this.dataCompetencia = dataCompetencia;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getAprovadaPor() {
        return aprovadaPor;
    }

    public void setAprovadaPor(Long aprovadaPor) {
        this.aprovadaPor = aprovadaPor;
    }

    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public Long getPagaPor() {
        return pagaPor;
    }

    public void setPagaPor(Long pagaPor) {
        this.pagaPor = pagaPor;
    }
}
