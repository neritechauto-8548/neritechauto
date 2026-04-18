package com.neritech.saas.comunicacao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import com.neritech.saas.comunicacao.domain.enums.ProvedorSms;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "com_configuracoes_sms")
public class ConfiguracaoSms extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provedor_servico", nullable = false, length = 30)
    private ProvedorSms provedorServico;

    @Column(name = "api_endpoint", length = 500)
    private String apiEndpoint;

    @Column(name = "api_key", length = 255)
    private String apiKey;

    @Column(name = "api_secret", length = 255)
    private String apiSecret;

    @Column(name = "usuario_api", length = 100)
    private String usuarioApi;

    @Column(name = "senha_api", length = 255)
    private String senhaApi;

    @Column(name = "remetente_padrao", length = 20)
    private String remetentePadrao;

    @Column(name = "limite_caracteres")
    private Integer limiteCaracteres = 160;

    @Column(name = "custo_por_sms", precision = 6, scale = 4)
    private BigDecimal custoPorSms;

    @Column(name = "creditos_disponiveis")
    private Integer creditosDisponiveis = 0;

    @Column(name = "webhook_entrega", length = 500)
    private String webhookEntrega;

    @Column(name = "webhook_resposta", length = 500)
    private String webhookResposta;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "ambiente", length = 30)
    private Ambiente ambiente;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "data_configuracao")
    private LocalDateTime dataConfiguracao;

    @Column(name = "configurado_por")
    private Long configuradoPor;

    @Column(name = "data_ultima_sincronizacao")
    private LocalDateTime dataUltimaSincronizacao;

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public ProvedorSms getProvedorServico() {
        return provedorServico;
    }

    public void setProvedorServico(ProvedorSms provedorServico) {
        this.provedorServico = provedorServico;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
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

    public String getUsuarioApi() {
        return usuarioApi;
    }

    public void setUsuarioApi(String usuarioApi) {
        this.usuarioApi = usuarioApi;
    }

    public String getSenhaApi() {
        return senhaApi;
    }

    public void setSenhaApi(String senhaApi) {
        this.senhaApi = senhaApi;
    }

    public String getRemetentePadrao() {
        return remetentePadrao;
    }

    public void setRemetentePadrao(String remetentePadrao) {
        this.remetentePadrao = remetentePadrao;
    }

    public Integer getLimiteCaracteres() {
        return limiteCaracteres;
    }

    public void setLimiteCaracteres(Integer limiteCaracteres) {
        this.limiteCaracteres = limiteCaracteres;
    }

    public BigDecimal getCustoPorSms() {
        return custoPorSms;
    }

    public void setCustoPorSms(BigDecimal custoPorSms) {
        this.custoPorSms = custoPorSms;
    }

    public Integer getCreditosDisponiveis() {
        return creditosDisponiveis;
    }

    public void setCreditosDisponiveis(Integer creditosDisponiveis) {
        this.creditosDisponiveis = creditosDisponiveis;
    }

    public String getWebhookEntrega() {
        return webhookEntrega;
    }

    public void setWebhookEntrega(String webhookEntrega) {
        this.webhookEntrega = webhookEntrega;
    }

    public String getWebhookResposta() {
        return webhookResposta;
    }

    public void setWebhookResposta(String webhookResposta) {
        this.webhookResposta = webhookResposta;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
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

    public LocalDateTime getDataUltimaSincronizacao() {
        return dataUltimaSincronizacao;
    }

    public void setDataUltimaSincronizacao(LocalDateTime dataUltimaSincronizacao) {
        this.dataUltimaSincronizacao = dataUltimaSincronizacao;
    }
}
