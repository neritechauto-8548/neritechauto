package com.neritech.saas.relatorios.dto;

import com.neritech.saas.relatorios.domain.enums.FrequenciaCalculo;
import com.neritech.saas.relatorios.domain.enums.TendenciaKpi;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class KpiOficinaResponse {
    private Long id;
    private Long empresaId;
    private String nomeKpi;
    private String categoria;
    private String descricao;
    private String formulaCalculo;
    private BigDecimal valorAtual;
    private BigDecimal valorMeta;
    private BigDecimal valorPeriodoAnterior;
    private String unidadeMedida;
    private FrequenciaCalculo frequenciaCalculo;
    private String periodoReferencia;
    private LocalDateTime dataCalculo;
    private BigDecimal percentualMetaAtingido;
    private BigDecimal variacaoPeriodoAnterior;
    private TendenciaKpi tendencia;
    private String corIndicador;
    private String origemDados; // JSON
    private String observacoes;
    private Boolean ativo;
    private Integer ordemExibicao;
    private Boolean dashboardPublico;
    private Boolean calculadoAutomaticamente;
    private LocalDateTime dataProximaAtualizacao;

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getEmpresaId() {
        return this.empresaId;
    }
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
    public String getNomeKpi() {
        return this.nomeKpi;
    }
    public void setNomeKpi(String nomeKpi) {
        this.nomeKpi = nomeKpi;
    }
    public String getCategoria() {
        return this.categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getDescricao() {
        return this.descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getFormulaCalculo() {
        return this.formulaCalculo;
    }
    public void setFormulaCalculo(String formulaCalculo) {
        this.formulaCalculo = formulaCalculo;
    }
    public BigDecimal getValorAtual() {
        return this.valorAtual;
    }
    public void setValorAtual(BigDecimal valorAtual) {
        this.valorAtual = valorAtual;
    }
    public BigDecimal getValorMeta() {
        return this.valorMeta;
    }
    public void setValorMeta(BigDecimal valorMeta) {
        this.valorMeta = valorMeta;
    }
    public BigDecimal getValorPeriodoAnterior() {
        return this.valorPeriodoAnterior;
    }
    public void setValorPeriodoAnterior(BigDecimal valorPeriodoAnterior) {
        this.valorPeriodoAnterior = valorPeriodoAnterior;
    }
    public String getUnidadeMedida() {
        return this.unidadeMedida;
    }
    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
    public FrequenciaCalculo getFrequenciaCalculo() {
        return this.frequenciaCalculo;
    }
    public void setFrequenciaCalculo(FrequenciaCalculo frequenciaCalculo) {
        this.frequenciaCalculo = frequenciaCalculo;
    }
    public String getPeriodoReferencia() {
        return this.periodoReferencia;
    }
    public void setPeriodoReferencia(String periodoReferencia) {
        this.periodoReferencia = periodoReferencia;
    }
    public LocalDateTime getDataCalculo() {
        return this.dataCalculo;
    }
    public void setDataCalculo(LocalDateTime dataCalculo) {
        this.dataCalculo = dataCalculo;
    }
    public BigDecimal getPercentualMetaAtingido() {
        return this.percentualMetaAtingido;
    }
    public void setPercentualMetaAtingido(BigDecimal percentualMetaAtingido) {
        this.percentualMetaAtingido = percentualMetaAtingido;
    }
    public BigDecimal getVariacaoPeriodoAnterior() {
        return this.variacaoPeriodoAnterior;
    }
    public void setVariacaoPeriodoAnterior(BigDecimal variacaoPeriodoAnterior) {
        this.variacaoPeriodoAnterior = variacaoPeriodoAnterior;
    }
    public TendenciaKpi getTendencia() {
        return this.tendencia;
    }
    public void setTendencia(TendenciaKpi tendencia) {
        this.tendencia = tendencia;
    }
    public String getCorIndicador() {
        return this.corIndicador;
    }
    public void setCorIndicador(String corIndicador) {
        this.corIndicador = corIndicador;
    }
    public String getOrigemDados() {
        return this.origemDados;
    }
    public void setOrigemDados(String origemDados) {
        this.origemDados = origemDados;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    public Boolean isAtivo() {
        return this.ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    public Integer getOrdemExibicao() {
        return this.ordemExibicao;
    }
    public void setOrdemExibicao(Integer ordemExibicao) {
        this.ordemExibicao = ordemExibicao;
    }
    public Boolean isDashboardPublico() {
        return this.dashboardPublico;
    }
    public void setDashboardPublico(Boolean dashboardPublico) {
        this.dashboardPublico = dashboardPublico;
    }
    public Boolean isCalculadoAutomaticamente() {
        return this.calculadoAutomaticamente;
    }
    public void setCalculadoAutomaticamente(Boolean calculadoAutomaticamente) {
        this.calculadoAutomaticamente = calculadoAutomaticamente;
    }
    public LocalDateTime getDataProximaAtualizacao() {
        return this.dataProximaAtualizacao;
    }
    public void setDataProximaAtualizacao(LocalDateTime dataProximaAtualizacao) {
        this.dataProximaAtualizacao = dataProximaAtualizacao;
    }
}
