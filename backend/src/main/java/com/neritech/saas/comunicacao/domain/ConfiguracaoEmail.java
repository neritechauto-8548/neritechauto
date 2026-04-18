package com.neritech.saas.comunicacao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.Criptografia;
import com.neritech.saas.comunicacao.domain.enums.ProvedorEmail;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "com_configuracoes_email")
public class ConfiguracaoEmail extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provedor_servico", nullable = false, length = 30)
    private ProvedorEmail provedorServico;

    @Column(name = "servidor_smtp", length = 255)
    private String servidorSmtp;

    @Column(name = "porta_smtp")
    private Integer portaSmtp = 587;

    @Column(name = "usuario_smtp", length = 255)
    private String usuarioSmtp;

    @Column(name = "senha_smtp", length = 255)
    private String senhaSmtp;

    @Enumerated(EnumType.STRING)
    @Column(name = "criptografia", length = 30)
    private Criptografia criptografia;

    @Column(name = "remetente_nome", length = 255)
    private String remetenteNome;

    @Column(name = "remetente_email", length = 255)
    private String remetenteEmail;

    @Column(name = "email_resposta", length = 255)
    private String emailResposta;

    @Column(name = "email_copia_oculta", length = 255)
    private String emailCopiaOculta;

    @Column(name = "api_key", length = 255)
    private String apiKey;

    @Column(name = "api_secret", length = 255)
    private String apiSecret;

    @Column(name = "dominio_autorizado", length = 255)
    private String dominioAutorizado;

    @Column(name = "limite_envios_dia")
    private Integer limiteEnviosDia;

    @Column(name = "envios_realizados_hoje")
    private Integer enviosRealizadosHoje = 0;

    @Column(name = "webhook_entrega", length = 500)
    private String webhookEntrega;

    @Column(name = "webhook_abertura", length = 500)
    private String webhookAbertura;

    @Column(name = "webhook_clique", length = 500)
    private String webhookClique;

    @Column(name = "assinatura_padrao", columnDefinition = "TEXT")
    private String assinaturaPadrao;

    @Column(name = "template_cabecalho", columnDefinition = "TEXT")
    private String templateCabecalho;

    @Column(name = "template_rodape", columnDefinition = "TEXT")
    private String templateRodape;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "testado")
    private Boolean testado = false;

    @Column(name = "data_ultimo_teste")
    private LocalDateTime dataUltimoTeste;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "data_configuracao")
    private LocalDateTime dataConfiguracao;

    @Column(name = "configurado_por")
    private Long configuradoPor;

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public ProvedorEmail getProvedorServico() {
        return provedorServico;
    }

    public void setProvedorServico(ProvedorEmail provedorServico) {
        this.provedorServico = provedorServico;
    }

    public String getServidorSmtp() {
        return servidorSmtp;
    }

    public void setServidorSmtp(String servidorSmtp) {
        this.servidorSmtp = servidorSmtp;
    }

    public Integer getPortaSmtp() {
        return portaSmtp;
    }

    public void setPortaSmtp(Integer portaSmtp) {
        this.portaSmtp = portaSmtp;
    }

    public String getUsuarioSmtp() {
        return usuarioSmtp;
    }

    public void setUsuarioSmtp(String usuarioSmtp) {
        this.usuarioSmtp = usuarioSmtp;
    }

    public String getSenhaSmtp() {
        return senhaSmtp;
    }

    public void setSenhaSmtp(String senhaSmtp) {
        this.senhaSmtp = senhaSmtp;
    }

    public Criptografia getCriptografia() {
        return criptografia;
    }

    public void setCriptografia(Criptografia criptografia) {
        this.criptografia = criptografia;
    }

    public String getRemetenteNome() {
        return remetenteNome;
    }

    public void setRemetenteNome(String remetenteNome) {
        this.remetenteNome = remetenteNome;
    }

    public String getRemetenteEmail() {
        return remetenteEmail;
    }

    public void setRemetenteEmail(String remetenteEmail) {
        this.remetenteEmail = remetenteEmail;
    }

    public String getEmailResposta() {
        return emailResposta;
    }

    public void setEmailResposta(String emailResposta) {
        this.emailResposta = emailResposta;
    }

    public String getEmailCopiaOculta() {
        return emailCopiaOculta;
    }

    public void setEmailCopiaOculta(String emailCopiaOculta) {
        this.emailCopiaOculta = emailCopiaOculta;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getDominioAutorizado() {
        return dominioAutorizado;
    }

    public void setDominioAutorizado(String dominioAutorizado) {
        this.dominioAutorizado = dominioAutorizado;
    }

    public Integer getLimiteEnviosDia() {
        return limiteEnviosDia;
    }

    public void setLimiteEnviosDia(Integer limiteEnviosDia) {
        this.limiteEnviosDia = limiteEnviosDia;
    }

    public Integer getEnviosRealizadosHoje() {
        return enviosRealizadosHoje;
    }

    public void setEnviosRealizadosHoje(Integer enviosRealizadosHoje) {
        this.enviosRealizadosHoje = enviosRealizadosHoje;
    }

    public String getWebhookEntrega() {
        return webhookEntrega;
    }

    public void setWebhookEntrega(String webhookEntrega) {
        this.webhookEntrega = webhookEntrega;
    }

    public String getWebhookAbertura() {
        return webhookAbertura;
    }

    public void setWebhookAbertura(String webhookAbertura) {
        this.webhookAbertura = webhookAbertura;
    }

    public String getWebhookClique() {
        return webhookClique;
    }

    public void setWebhookClique(String webhookClique) {
        this.webhookClique = webhookClique;
    }

    public String getAssinaturaPadrao() {
        return assinaturaPadrao;
    }

    public void setAssinaturaPadrao(String assinaturaPadrao) {
        this.assinaturaPadrao = assinaturaPadrao;
    }

    public String getTemplateCabecalho() {
        return templateCabecalho;
    }

    public void setTemplateCabecalho(String templateCabecalho) {
        this.templateCabecalho = templateCabecalho;
    }

    public String getTemplateRodape() {
        return templateRodape;
    }

    public void setTemplateRodape(String templateRodape) {
        this.templateRodape = templateRodape;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getTestado() {
        return testado;
    }

    public void setTestado(Boolean testado) {
        this.testado = testado;
    }

    public LocalDateTime getDataUltimoTeste() {
        return dataUltimoTeste;
    }

    public void setDataUltimoTeste(LocalDateTime dataUltimoTeste) {
        this.dataUltimoTeste = dataUltimoTeste;
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
}
