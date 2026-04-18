package com.neritech.saas.comunicacao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.Prioridade;
import com.neritech.saas.comunicacao.domain.enums.TipoNotificacao;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "com_notificacoes_sistema")
public class NotificacaoSistema extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "usuario_destinatario_id", nullable = false)
    private Long usuarioDestinatarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_notificacao", nullable = false, length = 30)
    private TipoNotificacao tipoNotificacao;

    @Column(name = "categoria", length = 50)
    private String categoria;

    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    @Column(name = "mensagem", nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    @Column(name = "dados_contexto", columnDefinition = "TEXT")
    private String dadosContexto;

    @Column(name = "link_acao", length = 500)
    private String linkAcao;

    @Column(name = "texto_botao_acao", length = 50)
    private String textoBotaoAcao;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridade", nullable = false, length = 30)
    private Prioridade prioridade;

    @Column(name = "exige_confirmacao")
    private Boolean exigeConfirmacao = false;

    @Column(name = "lida")
    private Boolean lida = false;

    @Column(name = "data_leitura")
    private LocalDateTime dataLeitura;

    @Column(name = "confirmada")
    private Boolean confirmada = false;

    @Column(name = "data_confirmacao")
    private LocalDateTime dataConfirmacao;

    @Column(name = "data_expiracao")
    private LocalDateTime dataExpiracao;

    @Column(name = "icone", length = 50)
    private String icone;

    @Column(name = "cor", length = 7)
    private String cor;

    @Column(name = "som_notificacao", length = 100)
    private String somNotificacao;

    @Column(name = "enviar_email")
    private Boolean enviarEmail = false;

    @Column(name = "enviar_sms")
    private Boolean enviarSms = false;

    @Column(name = "enviar_push")
    private Boolean enviarPush = false;

    @Column(name = "origem_sistema", length = 100)
    private String origemSistema;

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Long getUsuarioDestinatarioId() {
        return usuarioDestinatarioId;
    }

    public void setUsuarioDestinatarioId(Long usuarioDestinatarioId) {
        this.usuarioDestinatarioId = usuarioDestinatarioId;
    }

    public TipoNotificacao getTipoNotificacao() {
        return tipoNotificacao;
    }

    public void setTipoNotificacao(TipoNotificacao tipoNotificacao) {
        this.tipoNotificacao = tipoNotificacao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDadosContexto() {
        return dadosContexto;
    }

    public void setDadosContexto(String dadosContexto) {
        this.dadosContexto = dadosContexto;
    }

    public String getLinkAcao() {
        return linkAcao;
    }

    public void setLinkAcao(String linkAcao) {
        this.linkAcao = linkAcao;
    }

    public String getTextoBotaoAcao() {
        return textoBotaoAcao;
    }

    public void setTextoBotaoAcao(String textoBotaoAcao) {
        this.textoBotaoAcao = textoBotaoAcao;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Boolean getExigeConfirmacao() {
        return exigeConfirmacao;
    }

    public void setExigeConfirmacao(Boolean exigeConfirmacao) {
        this.exigeConfirmacao = exigeConfirmacao;
    }

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
    }

    public LocalDateTime getDataLeitura() {
        return dataLeitura;
    }

    public void setDataLeitura(LocalDateTime dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    public Boolean getConfirmada() {
        return confirmada;
    }

    public void setConfirmada(Boolean confirmada) {
        this.confirmada = confirmada;
    }

    public LocalDateTime getDataConfirmacao() {
        return dataConfirmacao;
    }

    public void setDataConfirmacao(LocalDateTime dataConfirmacao) {
        this.dataConfirmacao = dataConfirmacao;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getSomNotificacao() {
        return somNotificacao;
    }

    public void setSomNotificacao(String somNotificacao) {
        this.somNotificacao = somNotificacao;
    }

    public Boolean getEnviarEmail() {
        return enviarEmail;
    }

    public void setEnviarEmail(Boolean enviarEmail) {
        this.enviarEmail = enviarEmail;
    }

    public Boolean getEnviarSms() {
        return enviarSms;
    }

    public void setEnviarSms(Boolean enviarSms) {
        this.enviarSms = enviarSms;
    }

    public Boolean getEnviarPush() {
        return enviarPush;
    }

    public void setEnviarPush(Boolean enviarPush) {
        this.enviarPush = enviarPush;
    }

    public String getOrigemSistema() {
        return origemSistema;
    }

    public void setOrigemSistema(String origemSistema) {
        this.origemSistema = origemSistema;
    }
}
