package com.neritech.saas.relatorios.domain;

import com.neritech.saas.relatorios.domain.enums.NivelLog;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "logs_sistema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empresa_id")
    private Long empresaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_log")
    private NivelLog nivelLog;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String modulo;

    private String funcao;

    @Column(nullable = false, columnDefinition = "text")
    private String mensagem;

    @Column(name = "stack_trace", columnDefinition = "text")
    private String stackTrace;

    @Column(name = "dados_contexto", columnDefinition = "text")
    private String dadosContexto; // JSON

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "sessao_id")
    private String sessaoId;

    @Column(name = "ip_origem")
    private String ipOrigem;

    @Column(name = "user_agent", columnDefinition = "text")
    private String userAgent;

    @Column(name = "url_requisicao")
    private String urlRequisicao;

    @Column(name = "metodo_http")
    private String metodoHttp;

    @Column(name = "tempo_resposta_ms")
    private Integer tempoRespostaMs;

    @Column(name = "memoria_utilizada_mb")
    private BigDecimal memoriaUtilizadaMb;

    @Column(name = "cpu_utilizada_percent")
    private BigDecimal cpuUtilizadaPercent;

    @Column(name = "exception_class")
    private String exceptionClass;

    @Column(name = "exception_message", columnDefinition = "text")
    private String exceptionMessage;

    @Column(name = "correlacao_id")
    private String correlacaoId;

    @Column(name = "thread_id")
    private String threadId;

    private String servidor;

    @Column(name = "versao_aplicacao")
    private String versaoAplicacao;

    @Builder.Default
    @Column(name = "data_ocorrencia")
    private LocalDateTime dataOcorrencia = LocalDateTime.now();

    @Builder.Default
    private Boolean resolvido = false;

    @Column(name = "data_resolucao")
    private LocalDateTime dataResolucao;

    @Column(name = "responsavel_resolucao")
    private Long responsavelResolucao;

    @Column(name = "observacoes_resolucao", columnDefinition = "text")
    private String observacoesResolucao;

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
    public NivelLog getNivelLog() {
        return this.nivelLog;
    }
    public void setNivelLog(NivelLog nivelLog) {
        this.nivelLog = nivelLog;
    }
    public String getCategoria() {
        return this.categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getModulo() {
        return this.modulo;
    }
    public void setModulo(String modulo) {
        this.modulo = modulo;
    }
    public String getFuncao() {
        return this.funcao;
    }
    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
    public String getMensagem() {
        return this.mensagem;
    }
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    public String getStackTrace() {
        return this.stackTrace;
    }
    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
    public String getDadosContexto() {
        return this.dadosContexto;
    }
    public void setDadosContexto(String dadosContexto) {
        this.dadosContexto = dadosContexto;
    }
    public Long getUsuarioId() {
        return this.usuarioId;
    }
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    public String getSessaoId() {
        return this.sessaoId;
    }
    public void setSessaoId(String sessaoId) {
        this.sessaoId = sessaoId;
    }
    public String getIpOrigem() {
        return this.ipOrigem;
    }
    public void setIpOrigem(String ipOrigem) {
        this.ipOrigem = ipOrigem;
    }
    public String getUserAgent() {
        return this.userAgent;
    }
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    public String getUrlRequisicao() {
        return this.urlRequisicao;
    }
    public void setUrlRequisicao(String urlRequisicao) {
        this.urlRequisicao = urlRequisicao;
    }
    public String getMetodoHttp() {
        return this.metodoHttp;
    }
    public void setMetodoHttp(String metodoHttp) {
        this.metodoHttp = metodoHttp;
    }
    public Integer getTempoRespostaMs() {
        return this.tempoRespostaMs;
    }
    public void setTempoRespostaMs(Integer tempoRespostaMs) {
        this.tempoRespostaMs = tempoRespostaMs;
    }
    public BigDecimal getMemoriaUtilizadaMb() {
        return this.memoriaUtilizadaMb;
    }
    public void setMemoriaUtilizadaMb(BigDecimal memoriaUtilizadaMb) {
        this.memoriaUtilizadaMb = memoriaUtilizadaMb;
    }
    public BigDecimal getCpuUtilizadaPercent() {
        return this.cpuUtilizadaPercent;
    }
    public void setCpuUtilizadaPercent(BigDecimal cpuUtilizadaPercent) {
        this.cpuUtilizadaPercent = cpuUtilizadaPercent;
    }
    public String getExceptionClass() {
        return this.exceptionClass;
    }
    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }
    public String getExceptionMessage() {
        return this.exceptionMessage;
    }
    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
    public String getCorrelacaoId() {
        return this.correlacaoId;
    }
    public void setCorrelacaoId(String correlacaoId) {
        this.correlacaoId = correlacaoId;
    }
    public String getThreadId() {
        return this.threadId;
    }
    public void setThreadId(String threadId) {
        this.threadId = threadId;
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
    public LocalDateTime getDataResolucao() {
        return this.dataResolucao;
    }
    public void setDataResolucao(LocalDateTime dataResolucao) {
        this.dataResolucao = dataResolucao;
    }
    public Long getResponsavelResolucao() {
        return this.responsavelResolucao;
    }
    public void setResponsavelResolucao(Long responsavelResolucao) {
        this.responsavelResolucao = responsavelResolucao;
    }
    public String getObservacoesResolucao() {
        return this.observacoesResolucao;
    }
    public void setObservacoesResolucao(String observacoesResolucao) {
        this.observacoesResolucao = observacoesResolucao;
    }
    public LocalDateTime getDataOcorrencia() {
        return this.dataOcorrencia;
    }
    public void setDataOcorrencia(LocalDateTime dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }
    public Boolean getResolvido() {
        return this.resolvido;
    }
    public void setResolvido(Boolean resolvido) {
        this.resolvido = resolvido;
    }
}
