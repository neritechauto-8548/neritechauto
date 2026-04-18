package com.neritech.saas.comunicacao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.FrequenciaVerificacao;
import com.neritech.saas.comunicacao.domain.enums.TipoAlerta;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "com_alertas_automaticos")
public class AlertaAutomatico extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "nome_alerta", nullable = false, length = 255)
    private String nomeAlerta;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_alerta", nullable = false, length = 30)
    private TipoAlerta tipoAlerta;

    @Column(name = "condicoes_disparo", columnDefinition = "TEXT")
    private String condicoesDisparo;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequencia_verificacao", nullable = false, length = 30)
    private FrequenciaVerificacao frequenciaVerificacao;

    @Column(name = "horario_verificacao")
    private LocalTime horarioVerificacao;

    @Column(name = "dia_semana_verificacao")
    private Integer diaSemanaVerificacao;

    @Column(name = "dia_mes_verificacao")
    private Integer diaMesVerificacao;

    @Column(name = "usuarios_notificar", columnDefinition = "TEXT")
    private String usuariosNotificar;

    @Column(name = "canais_notificacao", columnDefinition = "TEXT")
    private String canaisNotificacao;

    @Column(name = "template_notificacao_id")
    private Long templateNotificacaoId;

    @Column(name = "mensagem_personalizada", columnDefinition = "TEXT")
    private String mensagemPersonalizada;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "ultima_execucao")
    private LocalDateTime ultimaExecucao;

    @Column(name = "proxima_execucao")
    private LocalDateTime proximaExecucao;

    @Column(name = "total_disparos")
    private Integer totalDisparos = 0;

    @Column(name = "total_erros")
    private Integer totalErros = 0;

    @Column(name = "log_execucoes", columnDefinition = "TEXT")
    private String logExecucoes;

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

    public String getNomeAlerta() {
        return nomeAlerta;
    }

    public void setNomeAlerta(String nomeAlerta) {
        this.nomeAlerta = nomeAlerta;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoAlerta getTipoAlerta() {
        return tipoAlerta;
    }

    public void setTipoAlerta(TipoAlerta tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }

    public String getCondicoesDisparo() {
        return condicoesDisparo;
    }

    public void setCondicoesDisparo(String condicoesDisparo) {
        this.condicoesDisparo = condicoesDisparo;
    }

    public FrequenciaVerificacao getFrequenciaVerificacao() {
        return frequenciaVerificacao;
    }

    public void setFrequenciaVerificacao(FrequenciaVerificacao frequenciaVerificacao) {
        this.frequenciaVerificacao = frequenciaVerificacao;
    }

    public LocalTime getHorarioVerificacao() {
        return horarioVerificacao;
    }

    public void setHorarioVerificacao(LocalTime horarioVerificacao) {
        this.horarioVerificacao = horarioVerificacao;
    }

    public Integer getDiaSemanaVerificacao() {
        return diaSemanaVerificacao;
    }

    public void setDiaSemanaVerificacao(Integer diaSemanaVerificacao) {
        this.diaSemanaVerificacao = diaSemanaVerificacao;
    }

    public Integer getDiaMesVerificacao() {
        return diaMesVerificacao;
    }

    public void setDiaMesVerificacao(Integer diaMesVerificacao) {
        this.diaMesVerificacao = diaMesVerificacao;
    }

    public String getUsuariosNotificar() {
        return usuariosNotificar;
    }

    public void setUsuariosNotificar(String usuariosNotificar) {
        this.usuariosNotificar = usuariosNotificar;
    }

    public String getCanaisNotificacao() {
        return canaisNotificacao;
    }

    public void setCanaisNotificacao(String canaisNotificacao) {
        this.canaisNotificacao = canaisNotificacao;
    }

    public Long getTemplateNotificacaoId() {
        return templateNotificacaoId;
    }

    public void setTemplateNotificacaoId(Long templateNotificacaoId) {
        this.templateNotificacaoId = templateNotificacaoId;
    }

    public String getMensagemPersonalizada() {
        return mensagemPersonalizada;
    }

    public void setMensagemPersonalizada(String mensagemPersonalizada) {
        this.mensagemPersonalizada = mensagemPersonalizada;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getUltimaExecucao() {
        return ultimaExecucao;
    }

    public void setUltimaExecucao(LocalDateTime ultimaExecucao) {
        this.ultimaExecucao = ultimaExecucao;
    }

    public LocalDateTime getProximaExecucao() {
        return proximaExecucao;
    }

    public void setProximaExecucao(LocalDateTime proximaExecucao) {
        this.proximaExecucao = proximaExecucao;
    }

    public Integer getTotalDisparos() {
        return totalDisparos;
    }

    public void setTotalDisparos(Integer totalDisparos) {
        this.totalDisparos = totalDisparos;
    }

    public Integer getTotalErros() {
        return totalErros;
    }

    public void setTotalErros(Integer totalErros) {
        this.totalErros = totalErros;
    }

    public String getLogExecucoes() {
        return logExecucoes;
    }

    public void setLogExecucoes(String logExecucoes) {
        this.logExecucoes = logExecucoes;
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
