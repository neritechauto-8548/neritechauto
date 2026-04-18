package com.neritech.saas.relatorios.domain;

import com.neritech.saas.common.tenancy.TenantEntity;

import com.neritech.saas.relatorios.domain.enums.FrequenciaCalculo;
import com.neritech.saas.relatorios.domain.enums.TendenciaKpi;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "kpis_oficina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KpiOficina extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_kpi", nullable = false)
    private String nomeKpi;

    private String categoria;

    @Column(columnDefinition = "text")
    private String descricao;

    @Column(name = "formula_calculo", columnDefinition = "text")
    private String formulaCalculo;

    @Column(name = "valor_atual")
    private BigDecimal valorAtual;

    @Column(name = "valor_meta")
    private BigDecimal valorMeta;

    @Column(name = "valor_periodo_anterior")
    private BigDecimal valorPeriodoAnterior;

    @Column(name = "unidade_medida")
    private String unidadeMedida;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequencia_calculo")
    private FrequenciaCalculo frequenciaCalculo;

    @Column(name = "periodo_referencia", nullable = false)
    private String periodoReferencia;

    @Column(name = "data_calculo", nullable = false)
    private LocalDateTime dataCalculo;

    @Column(name = "percentual_meta_atingido")
    private BigDecimal percentualMetaAtingido;

    @Column(name = "variacao_periodo_anterior")
    private BigDecimal variacaoPeriodoAnterior;

    @Enumerated(EnumType.STRING)
    private TendenciaKpi tendencia;

    @Column(name = "cor_indicador")
    private String corIndicador;

    @Column(name = "origem_dados", columnDefinition = "text")
    private String origemDados; // JSON

    @Column(columnDefinition = "text")
    private String observacoes;

    @Builder.Default
    private Boolean ativo = true;

    @Builder.Default
    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao = 0;

    @Builder.Default
    @Column(name = "dashboard_publico")
    private Boolean dashboardPublico = false;

    @Builder.Default
    @Column(name = "calculado_automaticamente")
    private Boolean calculadoAutomaticamente = true;

    @Column(name = "data_proxima_atualizacao")
    private LocalDateTime dataProximaAtualizacao;

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public LocalDateTime getDataProximaAtualizacao() {
        return this.dataProximaAtualizacao;
    }
    public void setDataProximaAtualizacao(LocalDateTime dataProximaAtualizacao) {
        this.dataProximaAtualizacao = dataProximaAtualizacao;
    }
}
