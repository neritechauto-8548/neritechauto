package com.neritech.saas.garantia.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.Prioridade;
import com.neritech.saas.rh.domain.Funcionario;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade que representa uma reclamaÃƒÂ§ÃƒÂ£o/acionamento de garantia
 */
@Entity
@Table(name = "gar_reclamacoes_garantia")
@Getter
@Setter
public class ReclamacaoGarantia extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garantia_id", nullable = false)
    private Garantia garantia;

    @Column(name = "numero_reclamacao", unique = true, nullable = false, length = 30)
    private String numeroReclamacao;

    @Column(name = "data_abertura")
    private LocalDateTime dataAbertura;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_reclamacao", nullable = false, length = 30)
    private TipoReclamacao tipoReclamacao;

    @Column(name = "problema_relatado", columnDefinition = "TEXT", nullable = false)
    private String problemaRelatado;

    @Column(name = "sintomas_observados", columnDefinition = "TEXT")
    private String sintomasObservados;

    @Column(name = "condicoes_uso", columnDefinition = "TEXT")
    private String condicoesUso;

    @Column(name = "kilometragem_atual")
    private Integer kilometragemAtual;

    @Column(name = "tempo_uso_desde_servico")
    private Integer tempoUsoDesdeServico;

    @Column(name = "evidencias_fornecidas", columnDefinition = "TEXT")
    private String evidenciasFornecidas; // JSON

    @Column(name = "fotos_problema", columnDefinition = "TEXT")
    private String fotosProblema; // JSON

    @Column(name = "videos_problema", columnDefinition = "TEXT")
    private String videosProblema; // JSON

    @Column(name = "documentos_anexos", columnDefinition = "TEXT")
    private String documentosAnexos; // JSON

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridade", nullable = false, length = 30)
    private PrioridadeReclamacao prioridade;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal_abertura", nullable = false, length = 30)
    private CanalAberturaReclamacao canalAbertura;

    @Column(name = "cliente_solicitante", length = 255)
    private String clienteSolicitante;

    @Column(name = "contato_solicitante", length = 255)
    private String contatoSolicitante;

    @Column(name = "endereco_atendimento", columnDefinition = "TEXT")
    private String enderecoAtendimento;

    @Column(name = "data_agendamento_analise")
    private LocalDateTime dataAgendamentoAnalise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_responsavel_id")
    private Funcionario tecnicoResponsavel;

    @Column(name = "data_inicio_analise")
    private LocalDateTime dataInicioAnalise;

    @Column(name = "data_fim_analise")
    private LocalDateTime dataFimAnalise;

    @Column(name = "tempo_analise_horas")
    private Integer tempoAnaliseHoras;

    @Column(name = "diagnostico_tecnico", columnDefinition = "TEXT")
    private String diagnosticoTecnico;

    @Column(name = "causa_identificada", columnDefinition = "TEXT")
    private String causaIdentificada;

    @Column(name = "procedimento_realizado", columnDefinition = "TEXT")
    private String procedimentoRealizado;

    @Column(name = "aprovada")
    private Boolean aprovada;

    @Column(name = "motivo_reprovacao", columnDefinition = "TEXT")
    private String motivoReprovacao;

    @Column(name = "valor_aprovado", precision = 10, scale = 2)
    private BigDecimal valorAprovado;

    @Column(name = "itens_substituidos", columnDefinition = "TEXT")
    private String itensSubstituidos; // JSON

    @Column(name = "servicos_refeitos", columnDefinition = "TEXT")
    private String servicosRefeitos; // JSON

    @Column(name = "custos_adicionais", precision = 8, scale = 2)
    private BigDecimal custosAdicionais = BigDecimal.ZERO;

    @Column(name = "justificativa_custos", columnDefinition = "TEXT")
    private String justificativaCustos;

    @Column(name = "satisfacao_cliente")
    private Integer satisfacaoCliente;

    @Column(name = "comentario_satisfacao", columnDefinition = "TEXT")
    private String comentarioSatisfacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusReclamacao status;

    @Column(name = "data_resolucao")
    private LocalDateTime dataResolucao;

    @Column(name = "observacoes_internas", columnDefinition = "TEXT")
    private String observacoesInternas;

    @Column(name = "aberta_por")
    private Long abertaPor;

    public Garantia getGarantia() {
        return this.garantia;
    }
    public void setGarantia(Garantia garantia) {
        this.garantia = garantia;
    }
    public String getNumeroReclamacao() {
        return this.numeroReclamacao;
    }
    public void setNumeroReclamacao(String numeroReclamacao) {
        this.numeroReclamacao = numeroReclamacao;
    }
    public LocalDateTime getDataAbertura() {
        return this.dataAbertura;
    }
    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }
    public TipoReclamacao getTipoReclamacao() {
        return this.tipoReclamacao;
    }
    public void setTipoReclamacao(TipoReclamacao tipoReclamacao) {
        this.tipoReclamacao = tipoReclamacao;
    }
    public String getProblemaRelatado() {
        return this.problemaRelatado;
    }
    public void setProblemaRelatado(String problemaRelatado) {
        this.problemaRelatado = problemaRelatado;
    }
    public String getSintomasObservados() {
        return this.sintomasObservados;
    }
    public void setSintomasObservados(String sintomasObservados) {
        this.sintomasObservados = sintomasObservados;
    }
    public String getCondicoesUso() {
        return this.condicoesUso;
    }
    public void setCondicoesUso(String condicoesUso) {
        this.condicoesUso = condicoesUso;
    }
    public Integer getKilometragemAtual() {
        return this.kilometragemAtual;
    }
    public void setKilometragemAtual(Integer kilometragemAtual) {
        this.kilometragemAtual = kilometragemAtual;
    }
    public Integer getTempoUsoDesdeServico() {
        return this.tempoUsoDesdeServico;
    }
    public void setTempoUsoDesdeServico(Integer tempoUsoDesdeServico) {
        this.tempoUsoDesdeServico = tempoUsoDesdeServico;
    }
    public String getEvidenciasFornecidas() {
        return this.evidenciasFornecidas;
    }
    public void setEvidenciasFornecidas(String evidenciasFornecidas) {
        this.evidenciasFornecidas = evidenciasFornecidas;
    }
    public String getFotosProblema() {
        return this.fotosProblema;
    }
    public void setFotosProblema(String fotosProblema) {
        this.fotosProblema = fotosProblema;
    }
    public String getVideosProblema() {
        return this.videosProblema;
    }
    public void setVideosProblema(String videosProblema) {
        this.videosProblema = videosProblema;
    }
    public String getDocumentosAnexos() {
        return this.documentosAnexos;
    }
    public void setDocumentosAnexos(String documentosAnexos) {
        this.documentosAnexos = documentosAnexos;
    }
    public PrioridadeReclamacao getPrioridade() {
        return this.prioridade;
    }
    public void setPrioridade(PrioridadeReclamacao prioridade) {
        this.prioridade = prioridade;
    }
    public CanalAberturaReclamacao getCanalAbertura() {
        return this.canalAbertura;
    }
    public void setCanalAbertura(CanalAberturaReclamacao canalAbertura) {
        this.canalAbertura = canalAbertura;
    }
    public String getClienteSolicitante() {
        return this.clienteSolicitante;
    }
    public void setClienteSolicitante(String clienteSolicitante) {
        this.clienteSolicitante = clienteSolicitante;
    }
    public String getContatoSolicitante() {
        return this.contatoSolicitante;
    }
    public void setContatoSolicitante(String contatoSolicitante) {
        this.contatoSolicitante = contatoSolicitante;
    }
    public String getEnderecoAtendimento() {
        return this.enderecoAtendimento;
    }
    public void setEnderecoAtendimento(String enderecoAtendimento) {
        this.enderecoAtendimento = enderecoAtendimento;
    }
    public LocalDateTime getDataAgendamentoAnalise() {
        return this.dataAgendamentoAnalise;
    }
    public void setDataAgendamentoAnalise(LocalDateTime dataAgendamentoAnalise) {
        this.dataAgendamentoAnalise = dataAgendamentoAnalise;
    }
    public Funcionario getTecnicoResponsavel() {
        return this.tecnicoResponsavel;
    }
    public void setTecnicoResponsavel(Funcionario tecnicoResponsavel) {
        this.tecnicoResponsavel = tecnicoResponsavel;
    }
    public LocalDateTime getDataInicioAnalise() {
        return this.dataInicioAnalise;
    }
    public void setDataInicioAnalise(LocalDateTime dataInicioAnalise) {
        this.dataInicioAnalise = dataInicioAnalise;
    }
    public LocalDateTime getDataFimAnalise() {
        return this.dataFimAnalise;
    }
    public void setDataFimAnalise(LocalDateTime dataFimAnalise) {
        this.dataFimAnalise = dataFimAnalise;
    }
    public Integer getTempoAnaliseHoras() {
        return this.tempoAnaliseHoras;
    }
    public void setTempoAnaliseHoras(Integer tempoAnaliseHoras) {
        this.tempoAnaliseHoras = tempoAnaliseHoras;
    }
    public String getDiagnosticoTecnico() {
        return this.diagnosticoTecnico;
    }
    public void setDiagnosticoTecnico(String diagnosticoTecnico) {
        this.diagnosticoTecnico = diagnosticoTecnico;
    }
    public String getCausaIdentificada() {
        return this.causaIdentificada;
    }
    public void setCausaIdentificada(String causaIdentificada) {
        this.causaIdentificada = causaIdentificada;
    }
    public String getProcedimentoRealizado() {
        return this.procedimentoRealizado;
    }
    public void setProcedimentoRealizado(String procedimentoRealizado) {
        this.procedimentoRealizado = procedimentoRealizado;
    }
    public Boolean isAprovada() {
        return this.aprovada;
    }
    public void setAprovada(Boolean aprovada) {
        this.aprovada = aprovada;
    }
    public String getMotivoReprovacao() {
        return this.motivoReprovacao;
    }
    public void setMotivoReprovacao(String motivoReprovacao) {
        this.motivoReprovacao = motivoReprovacao;
    }
    public BigDecimal getValorAprovado() {
        return this.valorAprovado;
    }
    public void setValorAprovado(BigDecimal valorAprovado) {
        this.valorAprovado = valorAprovado;
    }
    public String getItensSubstituidos() {
        return this.itensSubstituidos;
    }
    public void setItensSubstituidos(String itensSubstituidos) {
        this.itensSubstituidos = itensSubstituidos;
    }
    public String getServicosRefeitos() {
        return this.servicosRefeitos;
    }
    public void setServicosRefeitos(String servicosRefeitos) {
        this.servicosRefeitos = servicosRefeitos;
    }
    public String getJustificativaCustos() {
        return this.justificativaCustos;
    }
    public void setJustificativaCustos(String justificativaCustos) {
        this.justificativaCustos = justificativaCustos;
    }
    public Integer getSatisfacaoCliente() {
        return this.satisfacaoCliente;
    }
    public void setSatisfacaoCliente(Integer satisfacaoCliente) {
        this.satisfacaoCliente = satisfacaoCliente;
    }
    public String getComentarioSatisfacao() {
        return this.comentarioSatisfacao;
    }
    public void setComentarioSatisfacao(String comentarioSatisfacao) {
        this.comentarioSatisfacao = comentarioSatisfacao;
    }
    public StatusReclamacao getStatus() {
        return this.status;
    }
    public void setStatus(StatusReclamacao status) {
        this.status = status;
    }
    public LocalDateTime getDataResolucao() {
        return this.dataResolucao;
    }
    public void setDataResolucao(LocalDateTime dataResolucao) {
        this.dataResolucao = dataResolucao;
    }
    public String getObservacoesInternas() {
        return this.observacoesInternas;
    }
    public void setObservacoesInternas(String observacoesInternas) {
        this.observacoesInternas = observacoesInternas;
    }
    public Long getAbertaPor() {
        return this.abertaPor;
    }
    public void setAbertaPor(Long abertaPor) {
        this.abertaPor = abertaPor;
    }
}
