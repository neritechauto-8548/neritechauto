package com.neritech.saas.relatorios.dto;

import com.neritech.saas.relatorios.domain.enums.TendenciaMetrica;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MetricaPerformanceRequest {
    private Long empresaId;
    private String metrica;
    private String categoria;
    private BigDecimal valorNumerico;
    private String valorTexto;
    private String unidadeMedida;
    private LocalDateTime dataMedicao;
    private String periodoReferencia;
    private String contextoAdicional; // JSON
    private String servidor;
    private String versaoAplicacao;
    private BigDecimal benchmarkAnterior;
    private BigDecimal variacaoPercentual;
    private TendenciaMetrica tendencia;
    private Boolean alertaGerado;
    private BigDecimal limiteAlertaMin;
    private BigDecimal limiteAlertaMax;
    private String observacoes;

    public Long getEmpresaId() {
        return this.empresaId;
    }
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
    public String getMetrica() {
        return this.metrica;
    }
    public void setMetrica(String metrica) {
        this.metrica = metrica;
    }
    public String getCategoria() {
        return this.categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public BigDecimal getValorNumerico() {
        return this.valorNumerico;
    }
    public void setValorNumerico(BigDecimal valorNumerico) {
        this.valorNumerico = valorNumerico;
    }
    public String getValorTexto() {
        return this.valorTexto;
    }
    public void setValorTexto(String valorTexto) {
        this.valorTexto = valorTexto;
    }
    public String getUnidadeMedida() {
        return this.unidadeMedida;
    }
    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
    public LocalDateTime getDataMedicao() {
        return this.dataMedicao;
    }
    public void setDataMedicao(LocalDateTime dataMedicao) {
        this.dataMedicao = dataMedicao;
    }
    public String getPeriodoReferencia() {
        return this.periodoReferencia;
    }
    public void setPeriodoReferencia(String periodoReferencia) {
        this.periodoReferencia = periodoReferencia;
    }
    public String getContextoAdicional() {
        return this.contextoAdicional;
    }
    public void setContextoAdicional(String contextoAdicional) {
        this.contextoAdicional = contextoAdicional;
    }
    public String getServidor() {
        return this.servidor;
    }
    public void setServidor(String servidor) {
        this.servidor = servidor;
    }
    public String getVersaoAplicacao() {
        return this.versaoAplicacao;
    }
    public void setVersaoAplicacao(String versaoAplicacao) {
        this.versaoAplicacao = versaoAplicacao;
    }
    public BigDecimal getBenchmarkAnterior() {
        return this.benchmarkAnterior;
    }
    public void setBenchmarkAnterior(BigDecimal benchmarkAnterior) {
        this.benchmarkAnterior = benchmarkAnterior;
    }
    public BigDecimal getVariacaoPercentual() {
        return this.variacaoPercentual;
    }
    public void setVariacaoPercentual(BigDecimal variacaoPercentual) {
        this.variacaoPercentual = variacaoPercentual;
    }
    public TendenciaMetrica getTendencia() {
        return this.tendencia;
    }
    public void setTendencia(TendenciaMetrica tendencia) {
        this.tendencia = tendencia;
    }
    public Boolean isAlertaGerado() {
        return this.alertaGerado;
    }
    public void setAlertaGerado(Boolean alertaGerado) {
        this.alertaGerado = alertaGerado;
    }
    public BigDecimal getLimiteAlertaMin() {
        return this.limiteAlertaMin;
    }
    public void setLimiteAlertaMin(BigDecimal limiteAlertaMin) {
        this.limiteAlertaMin = limiteAlertaMin;
    }
    public BigDecimal getLimiteAlertaMax() {
        return this.limiteAlertaMax;
    }
    public void setLimiteAlertaMax(BigDecimal limiteAlertaMax) {
        this.limiteAlertaMax = limiteAlertaMax;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
