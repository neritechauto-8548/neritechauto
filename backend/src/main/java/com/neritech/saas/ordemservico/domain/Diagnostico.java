package com.neritech.saas.ordemservico.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.ordemservico.domain.enums.SistemaVeiculo;
import com.neritech.saas.ordemservico.domain.enums.UrgenciaDiagnostico;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "diagnosticos")
public class Diagnostico extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;

    @Enumerated(EnumType.STRING)
    @Column(name = "sistema_veiculo", length = 30)
    private SistemaVeiculo sistemaVeiculo;

    @Column(name = "componente_especifico")
    private String componenteEspecifico;

    @Column(name = "problema_identificado", nullable = false, columnDefinition = "TEXT")
    private String problemaIdentificado;

    @Column(name = "causa_provavel", columnDefinition = "TEXT")
    private String causaProvavel;

    @Column(name = "solucao_recomendada", columnDefinition = "TEXT")
    private String solucaoRecomendada;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgencia", length = 20)
    private UrgenciaDiagnostico urgencia;

    @Column(name = "impacto_seguranca")
    private Boolean impactoSeguranca = false;

    @Column(name = "impacto_dirigibilidade")
    private Boolean impactoDirigibilidade = false;

    @Column(name = "custo_estimado", precision = 10, scale = 2)
    private BigDecimal custoEstimado;

    @Column(name = "tempo_estimado_reparo")
    private Integer tempoEstimadoReparo;

    @Column(name = "ferramentas_necessarias", columnDefinition = "TEXT")
    private String ferramentasNecessarias;

    @Column(name = "pecas_necessarias", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String pecasNecessarias;

    @Column(name = "evidencias_encontradas", columnDefinition = "TEXT")
    private String evidenciasEncontradas;

    @Column(name = "testes_realizados", columnDefinition = "TEXT")
    private String testesRealizados;

    @Column(name = "codigo_erro", length = 50)
    private String codigoErro;

    @Column(name = "mecanico_diagnostico_id")
    private Long mecanicoDiagnosticoId;

    @Column(name = "data_diagnostico")
    private LocalDateTime dataDiagnostico;

    @Column(name = "fotos_diagnostico", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String fotosDiagnostico;

    @Column(name = "videos_diagnostico", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String videosDiagnostico;

    @Column(name = "aprovado_cliente")
    private Boolean aprovadoCliente = false;

    @Column(name = "data_aprovacao_cliente")
    private LocalDateTime dataAprovacaoCliente;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    // Getters and Setters
    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public SistemaVeiculo getSistemaVeiculo() {
        return sistemaVeiculo;
    }

    public void setSistemaVeiculo(SistemaVeiculo sistemaVeiculo) {
        this.sistemaVeiculo = sistemaVeiculo;
    }

    public String getComponenteEspecifico() {
        return componenteEspecifico;
    }

    public void setComponenteEspecifico(String componenteEspecifico) {
        this.componenteEspecifico = componenteEspecifico;
    }

    public String getProblemaIdentificado() {
        return problemaIdentificado;
    }

    public void setProblemaIdentificado(String problemaIdentificado) {
        this.problemaIdentificado = problemaIdentificado;
    }

    public String getCausaProvavel() {
        return causaProvavel;
    }

    public void setCausaProvavel(String causaProvavel) {
        this.causaProvavel = causaProvavel;
    }

    public String getSolucaoRecomendada() {
        return solucaoRecomendada;
    }

    public void setSolucaoRecomendada(String solucaoRecomendada) {
        this.solucaoRecomendada = solucaoRecomendada;
    }

    public UrgenciaDiagnostico getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(UrgenciaDiagnostico urgencia) {
        this.urgencia = urgencia;
    }

    public Boolean getImpactoSeguranca() {
        return impactoSeguranca;
    }

    public void setImpactoSeguranca(Boolean impactoSeguranca) {
        this.impactoSeguranca = impactoSeguranca;
    }

    public Boolean getImpactoDirigibilidade() {
        return impactoDirigibilidade;
    }

    public void setImpactoDirigibilidade(Boolean impactoDirigibilidade) {
        this.impactoDirigibilidade = impactoDirigibilidade;
    }

    public BigDecimal getCustoEstimado() {
        return custoEstimado;
    }

    public void setCustoEstimado(BigDecimal custoEstimado) {
        this.custoEstimado = custoEstimado;
    }

    public Integer getTempoEstimadoReparo() {
        return tempoEstimadoReparo;
    }

    public void setTempoEstimadoReparo(Integer tempoEstimadoReparo) {
        this.tempoEstimadoReparo = tempoEstimadoReparo;
    }

    public String getFerramentasNecessarias() {
        return ferramentasNecessarias;
    }

    public void setFerramentasNecessarias(String ferramentasNecessarias) {
        this.ferramentasNecessarias = ferramentasNecessarias;
    }

    public String getPecasNecessarias() {
        return pecasNecessarias;
    }

    public void setPecasNecessarias(String pecasNecessarias) {
        this.pecasNecessarias = pecasNecessarias;
    }

    public String getEvidenciasEncontradas() {
        return evidenciasEncontradas;
    }

    public void setEvidenciasEncontradas(String evidenciasEncontradas) {
        this.evidenciasEncontradas = evidenciasEncontradas;
    }

    public String getTestesRealizados() {
        return testesRealizados;
    }

    public void setTestesRealizados(String testesRealizados) {
        this.testesRealizados = testesRealizados;
    }

    public String getCodigoErro() {
        return codigoErro;
    }

    public void setCodigoErro(String codigoErro) {
        this.codigoErro = codigoErro;
    }

    public Long getMecanicoDiagnosticoId() {
        return mecanicoDiagnosticoId;
    }

    public void setMecanicoDiagnosticoId(Long mecanicoDiagnosticoId) {
        this.mecanicoDiagnosticoId = mecanicoDiagnosticoId;
    }

    public LocalDateTime getDataDiagnostico() {
        return dataDiagnostico;
    }

    public void setDataDiagnostico(LocalDateTime dataDiagnostico) {
        this.dataDiagnostico = dataDiagnostico;
    }

    public String getFotosDiagnostico() {
        return fotosDiagnostico;
    }

    public void setFotosDiagnostico(String fotosDiagnostico) {
        this.fotosDiagnostico = fotosDiagnostico;
    }

    public String getVideosDiagnostico() {
        return videosDiagnostico;
    }

    public void setVideosDiagnostico(String videosDiagnostico) {
        this.videosDiagnostico = videosDiagnostico;
    }

    public Boolean getAprovadoCliente() {
        return aprovadoCliente;
    }

    public void setAprovadoCliente(Boolean aprovadoCliente) {
        this.aprovadoCliente = aprovadoCliente;
    }

    public LocalDateTime getDataAprovacaoCliente() {
        return dataAprovacaoCliente;
    }

    public void setDataAprovacaoCliente(LocalDateTime dataAprovacaoCliente) {
        this.dataAprovacaoCliente = dataAprovacaoCliente;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
