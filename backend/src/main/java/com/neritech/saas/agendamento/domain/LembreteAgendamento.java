package com.neritech.saas.agendamento.domain;

import com.neritech.saas.agendamento.domain.enums.StatusEnvioLembrete;
import com.neritech.saas.agendamento.domain.enums.TipoLembrete;
import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "agd_lembretes_agendamento")
@Getter
@Setter
public class LembreteAgendamento extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_lembrete", length = 30, nullable = false)
    private TipoLembrete tipoLembrete;

    @Column(name = "destinatario", nullable = false, length = 255)
    private String destinatario;

    @Column(name = "assunto", length = 255)
    private String assunto;

    @Column(name = "mensagem", columnDefinition = "TEXT", nullable = false)
    private String mensagem;

    @Column(name = "data_agendamento_envio", nullable = false)
    private LocalDateTime dataAgendamentoEnvio;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_envio", length = 30, nullable = false)
    private StatusEnvioLembrete statusEnvio;

    @Column(name = "tentativas_envio")
    private Integer tentativasEnvio = 0;

    @Column(name = "erro_envio", columnDefinition = "TEXT")
    private String erroEnvio;

    @Column(name = "resposta_cliente", columnDefinition = "TEXT")
    private String respostaCliente;

    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;

    @Column(name = "custo_envio", precision = 6, scale = 4)
    private BigDecimal custoEnvio;

    @Column(name = "template_usado", length = 100)
    private String templateUsado;

    @Column(name = "variaveis_template", columnDefinition = "TEXT")
    private String variaveisTemplate;

    @Column(name = "automatico")
    private Boolean automatico = true;

    public Agendamento getAgendamento() {
        return this.agendamento;
    }
    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }
    public TipoLembrete getTipoLembrete() {
        return this.tipoLembrete;
    }
    public void setTipoLembrete(TipoLembrete tipoLembrete) {
        this.tipoLembrete = tipoLembrete;
    }
    public String getDestinatario() {
        return this.destinatario;
    }
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
    public String getAssunto() {
        return this.assunto;
    }
    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }
    public String getMensagem() {
        return this.mensagem;
    }
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    public LocalDateTime getDataAgendamentoEnvio() {
        return this.dataAgendamentoEnvio;
    }
    public void setDataAgendamentoEnvio(LocalDateTime dataAgendamentoEnvio) {
        this.dataAgendamentoEnvio = dataAgendamentoEnvio;
    }
    public LocalDateTime getDataEnvio() {
        return this.dataEnvio;
    }
    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }
    public StatusEnvioLembrete getStatusEnvio() {
        return this.statusEnvio;
    }
    public void setStatusEnvio(StatusEnvioLembrete statusEnvio) {
        this.statusEnvio = statusEnvio;
    }
    public String getErroEnvio() {
        return this.erroEnvio;
    }
    public void setErroEnvio(String erroEnvio) {
        this.erroEnvio = erroEnvio;
    }
    public String getRespostaCliente() {
        return this.respostaCliente;
    }
    public void setRespostaCliente(String respostaCliente) {
        this.respostaCliente = respostaCliente;
    }
    public LocalDateTime getDataResposta() {
        return this.dataResposta;
    }
    public void setDataResposta(LocalDateTime dataResposta) {
        this.dataResposta = dataResposta;
    }
    public BigDecimal getCustoEnvio() {
        return this.custoEnvio;
    }
    public void setCustoEnvio(BigDecimal custoEnvio) {
        this.custoEnvio = custoEnvio;
    }
    public String getTemplateUsado() {
        return this.templateUsado;
    }
    public void setTemplateUsado(String templateUsado) {
        this.templateUsado = templateUsado;
    }
    public String getVariaveisTemplate() {
        return this.variaveisTemplate;
    }
    public void setVariaveisTemplate(String variaveisTemplate) {
        this.variaveisTemplate = variaveisTemplate;
    }
}
