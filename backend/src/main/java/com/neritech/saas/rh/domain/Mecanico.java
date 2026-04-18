package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.rh.domain.enums.CapacidadeDiagnostico;
import com.neritech.saas.rh.domain.enums.NivelExperiencia;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "mecanicos")
public class Mecanico extends BaseEntity {

    @Column(name = "funcionario_id", nullable = false, unique = true)
    private Long funcionarioId;

    @Column(name = "codigo_mecanico", nullable = false, length = 20)
    private String codigoMecanico;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_experiencia", length = 20)
    private NivelExperiencia nivelExperiencia;

    @Column(name = "anos_experiencia")
    private Integer anosExperiencia = 0;

    @Column(name = "especialidades_principais", columnDefinition = "jsonb")
    private String especialidadesPrincipais;

    @Column(name = "certificacoes_ativas", columnDefinition = "jsonb")
    private String certificacoesAtivas;

    @Column(name = "produtividade_media", precision = 5, scale = 2)
    private BigDecimal produtividadeMedia;

    @Column(name = "qualidade_media", precision = 5, scale = 2)
    private BigDecimal qualidadeMedia;

    @Column(name = "tempo_medio_servico")
    private Integer tempoMedioServico;

    @Column(name = "total_os_executadas")
    private Integer totalOsExecutadas = 0;

    @Column(name = "total_retrabalho")
    private Integer totalRetrabalho = 0;

    @Column(name = "percentual_retrabalho", precision = 5, scale = 2)
    private BigDecimal percentualRetrabalho = BigDecimal.ZERO;

    @Column(name = "avaliacao_media_cliente", precision = 3, scale = 2)
    private BigDecimal avaliacaoMediaCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "capacidade_diagnostico", length = 20)
    private CapacidadeDiagnostico capacidadeDiagnostico;

    @Column(name = "pode_liderar_equipe")
    private Boolean podeLiderarEquipe = false;

    @Column(name = "autorizado_test_drive")
    private Boolean autorizadoTestDrive = false;

    @Column(name = "autorizado_equipamentos_especiais")
    private Boolean autorizadoEquipamentosEspeciais = true;

    @Column(name = "meta_produtividade_mensal", precision = 5, scale = 2)
    private BigDecimal metaProdutividadeMensal;

    @Column(name = "comissao_os_percentual", precision = 5, scale = 2)
    private BigDecimal comissaoOsPercentual = BigDecimal.ZERO;

    @Column(name = "bonus_qualidade_percentual", precision = 5, scale = 2)
    private BigDecimal bonusQualidadePercentual = BigDecimal.ZERO;

    @Column(name = "observacoes_tecnicas", columnDefinition = "TEXT")
    private String observacoesTecnicas;

    @Column(name = "ativo_execucao")
    private Boolean ativoExecucao = true;

    @Column(name = "data_ultima_avaliacao")
    private java.time.LocalDate dataUltimaAvaliacao;

    @Column(name = "data_proxima_avaliacao")
    private java.time.LocalDate dataProximaAvaliacao;

    // Getters and Setters
    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getCodigoMecanico() {
        return codigoMecanico;
    }

    public void setCodigoMecanico(String codigoMecanico) {
        this.codigoMecanico = codigoMecanico;
    }

    public NivelExperiencia getNivelExperiencia() {
        return nivelExperiencia;
    }

    public void setNivelExperiencia(NivelExperiencia nivelExperiencia) {
        this.nivelExperiencia = nivelExperiencia;
    }

    public Integer getAnosExperiencia() {
        return anosExperiencia;
    }

    public void setAnosExperiencia(Integer anosExperiencia) {
        this.anosExperiencia = anosExperiencia;
    }

    public String getEspecialidadesPrincipais() {
        return especialidadesPrincipais;
    }

    public void setEspecialidadesPrincipais(String especialidadesPrincipais) {
        this.especialidadesPrincipais = especialidadesPrincipais;
    }

    public String getCertificacoesAtivas() {
        return certificacoesAtivas;
    }

    public void setCertificacoesAtivas(String certificacoesAtivas) {
        this.certificacoesAtivas = certificacoesAtivas;
    }

    public BigDecimal getProdutividadeMedia() {
        return produtividadeMedia;
    }

    public void setProdutividadeMedia(BigDecimal produtividadeMedia) {
        this.produtividadeMedia = produtividadeMedia;
    }

    public BigDecimal getQualidadeMedia() {
        return qualidadeMedia;
    }

    public void setQualidadeMedia(BigDecimal qualidadeMedia) {
        this.qualidadeMedia = qualidadeMedia;
    }

    public Integer getTempoMedioServico() {
        return tempoMedioServico;
    }

    public void setTempoMedioServico(Integer tempoMedioServico) {
        this.tempoMedioServico = tempoMedioServico;
    }

    public Integer getTotalOsExecutadas() {
        return totalOsExecutadas;
    }

    public void setTotalOsExecutadas(Integer totalOsExecutadas) {
        this.totalOsExecutadas = totalOsExecutadas;
    }

    public Integer getTotalRetrabalho() {
        return totalRetrabalho;
    }

    public void setTotalRetrabalho(Integer totalRetrabalho) {
        this.totalRetrabalho = totalRetrabalho;
    }

    public BigDecimal getPercentualRetrabalho() {
        return percentualRetrabalho;
    }

    public void setPercentualRetrabalho(BigDecimal percentualRetrabalho) {
        this.percentualRetrabalho = percentualRetrabalho;
    }

    public BigDecimal getAvaliacaoMediaCliente() {
        return avaliacaoMediaCliente;
    }

    public void setAvaliacaoMediaCliente(BigDecimal avaliacaoMediaCliente) {
        this.avaliacaoMediaCliente = avaliacaoMediaCliente;
    }

    public CapacidadeDiagnostico getCapacidadeDiagnostico() {
        return capacidadeDiagnostico;
    }

    public void setCapacidadeDiagnostico(CapacidadeDiagnostico capacidadeDiagnostico) {
        this.capacidadeDiagnostico = capacidadeDiagnostico;
    }

    public Boolean getPodeLiderarEquipe() {
        return podeLiderarEquipe;
    }

    public void setPodeLiderarEquipe(Boolean podeLiderarEquipe) {
        this.podeLiderarEquipe = podeLiderarEquipe;
    }

    public Boolean getAutorizadoTestDrive() {
        return autorizadoTestDrive;
    }

    public void setAutorizadoTestDrive(Boolean autorizadoTestDrive) {
        this.autorizadoTestDrive = autorizadoTestDrive;
    }

    public Boolean getAutorizadoEquipamentosEspeciais() {
        return autorizadoEquipamentosEspeciais;
    }

    public void setAutorizadoEquipamentosEspeciais(Boolean autorizadoEquipamentosEspeciais) {
        this.autorizadoEquipamentosEspeciais = autorizadoEquipamentosEspeciais;
    }

    public BigDecimal getMetaProdutividadeMensal() {
        return metaProdutividadeMensal;
    }

    public void setMetaProdutividadeMensal(BigDecimal metaProdutividadeMensal) {
        this.metaProdutividadeMensal = metaProdutividadeMensal;
    }

    public BigDecimal getComissaoOsPercentual() {
        return comissaoOsPercentual;
    }

    public void setComissaoOsPercentual(BigDecimal comissaoOsPercentual) {
        this.comissaoOsPercentual = comissaoOsPercentual;
    }

    public BigDecimal getBonusQualidadePercentual() {
        return bonusQualidadePercentual;
    }

    public void setBonusQualidadePercentual(BigDecimal bonusQualidadePercentual) {
        this.bonusQualidadePercentual = bonusQualidadePercentual;
    }

    public String getObservacoesTecnicas() {
        return observacoesTecnicas;
    }

    public void setObservacoesTecnicas(String observacoesTecnicas) {
        this.observacoesTecnicas = observacoesTecnicas;
    }

    public Boolean getAtivoExecucao() {
        return ativoExecucao;
    }

    public void setAtivoExecucao(Boolean ativoExecucao) {
        this.ativoExecucao = ativoExecucao;
    }

    public java.time.LocalDate getDataUltimaAvaliacao() {
        return dataUltimaAvaliacao;
    }

    public void setDataUltimaAvaliacao(java.time.LocalDate dataUltimaAvaliacao) {
        this.dataUltimaAvaliacao = dataUltimaAvaliacao;
    }

    public java.time.LocalDate getDataProximaAvaliacao() {
        return dataProximaAvaliacao;
    }

    public void setDataProximaAvaliacao(java.time.LocalDate dataProximaAvaliacao) {
        this.dataProximaAvaliacao = dataProximaAvaliacao;
    }
}
