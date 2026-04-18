package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.rh.domain.enums.StatusTreinamento;
import com.neritech.saas.rh.domain.enums.TipoTreinamento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "treinamentos")
public class Treinamento extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "nome_treinamento", nullable = false, length = 255)
    private String nomeTreinamento;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_treinamento", length = 20)
    private TipoTreinamento tipoTreinamento;

    @Column(name = "instrutor_interno_id")
    private Long instrutorInternoId;

    @Column(name = "instrutor_externo", length = 255)
    private String instrutorExterno;

    @Column(name = "empresa_treinamento", length = 255)
    private String empresaTreinamento;

    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "local_realizacao", length = 255)
    private String localRealizacao;

    @Column(name = "capacidade_maxima")
    private Integer capacidadeMaxima;

    @Column(name = "custo_total", precision = 10, scale = 2)
    private BigDecimal custoTotal;

    @Column(name = "custo_por_participante", precision = 8, scale = 2)
    private BigDecimal custoPorParticipante;

    @Column(name = "objetivo_treinamento", columnDefinition = "TEXT")
    private String objetivoTreinamento;

    @Column(name = "conteudo_programatico", columnDefinition = "TEXT")
    private String conteudoProgramatico;

    @Column(name = "material_necessario", columnDefinition = "TEXT")
    private String materialNecessario;

    @Column(name = "certificacao_emitida")
    private Boolean certificacaoEmitida = false;

    @Column(name = "nome_certificacao", length = 255)
    private String nomeCertificacao;

    @Column(name = "validade_certificacao_meses")
    private Integer validadeCertificacaoMeses;

    @Column(name = "obrigatorio")
    private Boolean obrigatorio = false;

    @Column(name = "cargos_obrigatorios", columnDefinition = "jsonb")
    private String cargosObrigatorios;

    @Column(name = "departamentos_obrigatorios", columnDefinition = "jsonb")
    private String departamentosObrigatorios;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusTreinamento status;

    @Column(name = "avaliacao_media", precision = 3, scale = 2)
    private BigDecimal avaliacaoMedia;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "criado_por")
    private Long criadoPor;

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getNomeTreinamento() {
        return nomeTreinamento;
    }

    public void setNomeTreinamento(String nomeTreinamento) {
        this.nomeTreinamento = nomeTreinamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public TipoTreinamento getTipoTreinamento() {
        return tipoTreinamento;
    }

    public void setTipoTreinamento(TipoTreinamento tipoTreinamento) {
        this.tipoTreinamento = tipoTreinamento;
    }

    public Long getInstrutorInternoId() {
        return instrutorInternoId;
    }

    public void setInstrutorInternoId(Long instrutorInternoId) {
        this.instrutorInternoId = instrutorInternoId;
    }

    public String getInstrutorExterno() {
        return instrutorExterno;
    }

    public void setInstrutorExterno(String instrutorExterno) {
        this.instrutorExterno = instrutorExterno;
    }

    public String getEmpresaTreinamento() {
        return empresaTreinamento;
    }

    public void setEmpresaTreinamento(String empresaTreinamento) {
        this.empresaTreinamento = empresaTreinamento;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public String getLocalRealizacao() {
        return localRealizacao;
    }

    public void setLocalRealizacao(String localRealizacao) {
        this.localRealizacao = localRealizacao;
    }

    public Integer getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(Integer capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public BigDecimal getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(BigDecimal custoTotal) {
        this.custoTotal = custoTotal;
    }

    public BigDecimal getCustoPorParticipante() {
        return custoPorParticipante;
    }

    public void setCustoPorParticipante(BigDecimal custoPorParticipante) {
        this.custoPorParticipante = custoPorParticipante;
    }

    public String getObjetivoTreinamento() {
        return objetivoTreinamento;
    }

    public void setObjetivoTreinamento(String objetivoTreinamento) {
        this.objetivoTreinamento = objetivoTreinamento;
    }

    public String getConteudoProgramatico() {
        return conteudoProgramatico;
    }

    public void setConteudoProgramatico(String conteudoProgramatico) {
        this.conteudoProgramatico = conteudoProgramatico;
    }

    public String getMaterialNecessario() {
        return materialNecessario;
    }

    public void setMaterialNecessario(String materialNecessario) {
        this.materialNecessario = materialNecessario;
    }

    public Boolean getCertificacaoEmitida() {
        return certificacaoEmitida;
    }

    public void setCertificacaoEmitida(Boolean certificacaoEmitida) {
        this.certificacaoEmitida = certificacaoEmitida;
    }

    public String getNomeCertificacao() {
        return nomeCertificacao;
    }

    public void setNomeCertificacao(String nomeCertificacao) {
        this.nomeCertificacao = nomeCertificacao;
    }

    public Integer getValidadeCertificacaoMeses() {
        return validadeCertificacaoMeses;
    }

    public void setValidadeCertificacaoMeses(Integer validadeCertificacaoMeses) {
        this.validadeCertificacaoMeses = validadeCertificacaoMeses;
    }

    public Boolean getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public String getCargosObrigatorios() {
        return cargosObrigatorios;
    }

    public void setCargosObrigatorios(String cargosObrigatorios) {
        this.cargosObrigatorios = cargosObrigatorios;
    }

    public String getDepartamentosObrigatorios() {
        return departamentosObrigatorios;
    }

    public void setDepartamentosObrigatorios(String departamentosObrigatorios) {
        this.departamentosObrigatorios = departamentosObrigatorios;
    }

    public StatusTreinamento getStatus() {
        return status;
    }

    public void setStatus(StatusTreinamento status) {
        this.status = status;
    }

    public BigDecimal getAvaliacaoMedia() {
        return avaliacaoMedia;
    }

    public void setAvaliacaoMedia(BigDecimal avaliacaoMedia) {
        this.avaliacaoMedia = avaliacaoMedia;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(Long criadoPor) {
        this.criadoPor = criadoPor;
    }
}
