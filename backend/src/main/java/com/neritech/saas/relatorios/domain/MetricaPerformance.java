package com.neritech.saas.relatorios.domain;

import com.neritech.saas.common.tenancy.TenantEntity;

import com.neritech.saas.relatorios.domain.enums.TendenciaMetrica;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "metricas_performance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricaPerformance extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String metrica;

    private String categoria;

    @Column(name = "valor_numerico")
    private BigDecimal valorNumerico;

    @Column(name = "valor_texto")
    private String valorTexto;

    @Column(name = "unidade_medida")
    private String unidadeMedida;

    @Column(name = "data_medicao", nullable = false)
    private LocalDateTime dataMedicao;

    @Column(name = "periodo_referencia")
    private String periodoReferencia;

    @Column(name = "contexto_adicional", columnDefinition = "text")
    private String contextoAdicional; // JSON

    private String servidor;

    @Column(name = "versao_aplicacao")
    private String versaoAplicacao;

    @Column(name = "benchmark_anterior")
    private BigDecimal benchmarkAnterior;

    @Column(name = "variacao_percentual")
    private BigDecimal variacaoPercentual;

    @Enumerated(EnumType.STRING)
    private TendenciaMetrica tendencia;

    @Builder.Default
    @Column(name = "alerta_gerado")
    private Boolean alertaGerado = false;

    @Column(name = "limite_alerta_min")
    private BigDecimal limiteAlertaMin;

    @Column(name = "limite_alerta_max")
    private BigDecimal limiteAlertaMax;

    @Column(columnDefinition = "text")
    private String observacoes;

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
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
