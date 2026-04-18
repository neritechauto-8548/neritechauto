package com.neritech.saas.comunicacao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import com.neritech.saas.comunicacao.domain.enums.TipoIntegracao;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "com_configuracoes_whatsapp")
public class ConfiguracaoWhatsapp extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_integracao", nullable = false, length = 30)
    private TipoIntegracao tipoIntegracao;

    @Column(name = "provedor_servico", length = 100)
    private String provedorServico;

    @Column(name = "numero_whatsapp", nullable = false, length = 20)
    private String numeroWhatsapp;

    @Column(name = "token_acesso", length = 500)
    private String tokenAcesso;

    @Column(name = "webhook_url", length = 500)
    private String webhookUrl;

    @Column(name = "webhook_token", length = 255)
    private String webhookToken;

    @Column(name = "phone_number_id", length = 50)
    private String phoneNumberId;

    @Column(name = "business_account_id", length = 50)
    private String businessAccountId;

    @Column(name = "app_id", length = 50)
    private String appId;

    @Column(name = "app_secret", length = 255)
    private String appSecret;

    @Column(name = "mensagens_template_aprovadas", columnDefinition = "TEXT")
    private String mensagensTemplateAprovadas;

    @Column(name = "horario_atendimento_inicio")
    private LocalTime horarioAtendimentoInicio;

    @Column(name = "horario_atendimento_fim")
    private LocalTime horarioAtendimentoFim;

    @Column(name = "mensagem_fora_horario", columnDefinition = "TEXT")
    private String mensagemForaHorario;

    @Column(name = "mensagem_boas_vindas", columnDefinition = "TEXT")
    private String mensagemBoasVindas;

    @Column(name = "mensagem_menu_principal", columnDefinition = "TEXT")
    private String mensagemMenuPrincipal;

    @Column(name = "chatbot_ativo")
    private Boolean chatbotAtivo = false;

    @Column(name = "integracao_ativa")
    private Boolean integracaoAtiva = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "ambiente", length = 30)
    private Ambiente ambiente;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "data_configuracao")
    private LocalDateTime dataConfiguracao;

    @Column(name = "configurado_por")
    private Long configuradoPor;

    @Column(name = "data_ultima_verificacao")
    private LocalDateTime dataUltimaVerificacao;

    @Column(name = "status_verificacao", length = 50)
    private String statusVerificacao;

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public TipoIntegracao getTipoIntegracao() {
        return tipoIntegracao;
    }

    public void setTipoIntegracao(TipoIntegracao tipoIntegracao) {
        this.tipoIntegracao = tipoIntegracao;
    }

    public String getProvedorServico() {
        return provedorServico;
    }

    public void setProvedorServico(String provedorServico) {
        this.provedorServico = provedorServico;
    }

    public String getNumeroWhatsapp() {
        return numeroWhatsapp;
    }

    public void setNumeroWhatsapp(String numeroWhatsapp) {
        this.numeroWhatsapp = numeroWhatsapp;
    }

    public String getTokenAcesso() {
        return tokenAcesso;
    }

    public void setTokenAcesso(String tokenAcesso) {
        this.tokenAcesso = tokenAcesso;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getWebhookToken() {
        return webhookToken;
    }

    public void setWebhookToken(String webhookToken) {
        this.webhookToken = webhookToken;
    }

    public String getPhoneNumberId() {
        return phoneNumberId;
    }

    public void setPhoneNumberId(String phoneNumberId) {
        this.phoneNumberId = phoneNumberId;
    }

    public String getBusinessAccountId() {
        return businessAccountId;
    }

    public void setBusinessAccountId(String businessAccountId) {
        this.businessAccountId = businessAccountId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getMensagensTemplateAprovadas() {
        return mensagensTemplateAprovadas;
    }

    public void setMensagensTemplateAprovadas(String mensagensTemplateAprovadas) {
        this.mensagensTemplateAprovadas = mensagensTemplateAprovadas;
    }

    public LocalTime getHorarioAtendimentoInicio() {
        return horarioAtendimentoInicio;
    }

    public void setHorarioAtendimentoInicio(LocalTime horarioAtendimentoInicio) {
        this.horarioAtendimentoInicio = horarioAtendimentoInicio;
    }

    public LocalTime getHorarioAtendimentoFim() {
        return horarioAtendimentoFim;
    }

    public void setHorarioAtendimentoFim(LocalTime horarioAtendimentoFim) {
        this.horarioAtendimentoFim = horarioAtendimentoFim;
    }

    public String getMensagemForaHorario() {
        return mensagemForaHorario;
    }

    public void setMensagemForaHorario(String mensagemForaHorario) {
        this.mensagemForaHorario = mensagemForaHorario;
    }

    public String getMensagemBoasVindas() {
        return mensagemBoasVindas;
    }

    public void setMensagemBoasVindas(String mensagemBoasVindas) {
        this.mensagemBoasVindas = mensagemBoasVindas;
    }

    public String getMensagemMenuPrincipal() {
        return mensagemMenuPrincipal;
    }

    public void setMensagemMenuPrincipal(String mensagemMenuPrincipal) {
        this.mensagemMenuPrincipal = mensagemMenuPrincipal;
    }

    public Boolean getChatbotAtivo() {
        return chatbotAtivo;
    }

    public void setChatbotAtivo(Boolean chatbotAtivo) {
        this.chatbotAtivo = chatbotAtivo;
    }

    public Boolean getIntegracaoAtiva() {
        return integracaoAtiva;
    }

    public void setIntegracaoAtiva(Boolean integracaoAtiva) {
        this.integracaoAtiva = integracaoAtiva;
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(Ambiente ambiente) {
        this.ambiente = ambiente;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataConfiguracao() {
        return dataConfiguracao;
    }

    public void setDataConfiguracao(LocalDateTime dataConfiguracao) {
        this.dataConfiguracao = dataConfiguracao;
    }

    public Long getConfiguradoPor() {
        return configuradoPor;
    }

    public void setConfiguradoPor(Long configuradoPor) {
        this.configuradoPor = configuradoPor;
    }

    public LocalDateTime getDataUltimaVerificacao() {
        return dataUltimaVerificacao;
    }

    public void setDataUltimaVerificacao(LocalDateTime dataUltimaVerificacao) {
        this.dataUltimaVerificacao = dataUltimaVerificacao;
    }

    public String getStatusVerificacao() {
        return statusVerificacao;
    }

    public void setStatusVerificacao(String statusVerificacao) {
        this.statusVerificacao = statusVerificacao;
    }
}
