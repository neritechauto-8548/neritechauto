package com.neritech.saas.comunicacao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.DestinatarioTipo;
import com.neritech.saas.comunicacao.domain.enums.StatusComunicacao;
import com.neritech.saas.comunicacao.domain.enums.TipoComunicacao;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "com_comunicacoes_enviadas")
public class ComunicacaoEnviada extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "template_id")
    private Long templateId;

    @Column(name = "campanha_id")
    private Long campanhaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comunicacao", nullable = false, length = 30)
    private TipoComunicacao tipoComunicacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "destinatario_tipo", nullable = false, length = 30)
    private DestinatarioTipo destinatarioTipo;

    @Column(name = "destinatario_id", nullable = false)
    private Long destinatarioId;

    @Column(name = "destinatario_nome", nullable = false, length = 255)
    private String destinatarioNome;

    @Column(name = "destinatario_contato", nullable = false, length = 255)
    private String destinatarioContato;

    @Column(name = "assunto", length = 255)
    private String assunto;

    @Column(name = "conteudo", nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "anexos", columnDefinition = "TEXT")
    private String anexos;

    @Column(name = "agendada_para")
    private LocalDateTime agendadaPara;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    @Column(name = "data_leitura")
    private LocalDateTime dataLeitura;

    @Column(name = "data_clique")
    private LocalDateTime dataClique;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusComunicacao status;

    @Column(name = "tentativas_envio")
    private Integer tentativasEnvio = 0;

    @Column(name = "erro_envio", columnDefinition = "TEXT")
    private String erroEnvio;

    @Column(name = "custo_envio", precision = 8, scale = 4)
    private BigDecimal custoEnvio;

    @Column(name = "resposta_destinatario", columnDefinition = "TEXT")
    private String respostaDestinatario;

    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;

    @Column(name = "avaliacao_conteudo")
    private Integer avaliacaoConteudo;

    @Column(name = "motivo_avaliacao", columnDefinition = "TEXT")
    private String motivoAvaliacao;

    @Column(name = "automatica")
    private Boolean automatica = false;

    @Column(name = "ordem_servico_id")
    private Long ordemServicoId;

    @Column(name = "agendamento_id")
    private Long agendamentoId;

    @Column(name = "fatura_id")
    private Long faturaId;

    @Column(name = "usuario_envio")
    private Long usuarioEnvio;

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getCampanhaId() {
        return campanhaId;
    }

    public void setCampanhaId(Long campanhaId) {
        this.campanhaId = campanhaId;
    }

    public TipoComunicacao getTipoComunicacao() {
        return tipoComunicacao;
    }

    public void setTipoComunicacao(TipoComunicacao tipoComunicacao) {
        this.tipoComunicacao = tipoComunicacao;
    }

    public DestinatarioTipo getDestinatarioTipo() {
        return destinatarioTipo;
    }

    public void setDestinatarioTipo(DestinatarioTipo destinatarioTipo) {
        this.destinatarioTipo = destinatarioTipo;
    }

    public Long getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(Long destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public String getDestinatarioNome() {
        return destinatarioNome;
    }

    public void setDestinatarioNome(String destinatarioNome) {
        this.destinatarioNome = destinatarioNome;
    }

    public String getDestinatarioContato() {
        return destinatarioContato;
    }

    public void setDestinatarioContato(String destinatarioContato) {
        this.destinatarioContato = destinatarioContato;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getAnexos() {
        return anexos;
    }

    public void setAnexos(String anexos) {
        this.anexos = anexos;
    }

    public LocalDateTime getAgendadaPara() {
        return agendadaPara;
    }

    public void setAgendadaPara(LocalDateTime agendadaPara) {
        this.agendadaPara = agendadaPara;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public LocalDateTime getDataLeitura() {
        return dataLeitura;
    }

    public void setDataLeitura(LocalDateTime dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    public LocalDateTime getDataClique() {
        return dataClique;
    }

    public void setDataClique(LocalDateTime dataClique) {
        this.dataClique = dataClique;
    }

    public StatusComunicacao getStatus() {
        return status;
    }

    public void setStatus(StatusComunicacao status) {
        this.status = status;
    }

    public Integer getTentativasEnvio() {
        return tentativasEnvio;
    }

    public void setTentativasEnvio(Integer tentativasEnvio) {
        this.tentativasEnvio = tentativasEnvio;
    }

    public String getErroEnvio() {
        return erroEnvio;
    }

    public void setErroEnvio(String erroEnvio) {
        this.erroEnvio = erroEnvio;
    }

    public BigDecimal getCustoEnvio() {
        return custoEnvio;
    }

    public void setCustoEnvio(BigDecimal custoEnvio) {
        this.custoEnvio = custoEnvio;
    }

    public String getRespostaDestinatario() {
        return respostaDestinatario;
    }

    public void setRespostaDestinatario(String respostaDestinatario) {
        this.respostaDestinatario = respostaDestinatario;
    }

    public LocalDateTime getDataResposta() {
        return dataResposta;
    }

    public void setDataResposta(LocalDateTime dataResposta) {
        this.dataResposta = dataResposta;
    }

    public Integer getAvaliacaoConteudo() {
        return avaliacaoConteudo;
    }

    public void setAvaliacaoConteudo(Integer avaliacaoConteudo) {
        this.avaliacaoConteudo = avaliacaoConteudo;
    }

    public String getMotivoAvaliacao() {
        return motivoAvaliacao;
    }

    public void setMotivoAvaliacao(String motivoAvaliacao) {
        this.motivoAvaliacao = motivoAvaliacao;
    }

    public Boolean getAutomatica() {
        return automatica;
    }

    public void setAutomatica(Boolean automatica) {
        this.automatica = automatica;
    }

    public Long getOrdemServicoId() {
        return ordemServicoId;
    }

    public void setOrdemServicoId(Long ordemServicoId) {
        this.ordemServicoId = ordemServicoId;
    }

    public Long getAgendamentoId() {
        return agendamentoId;
    }

    public void setAgendamentoId(Long agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public Long getFaturaId() {
        return faturaId;
    }

    public void setFaturaId(Long faturaId) {
        this.faturaId = faturaId;
    }

    public Long getUsuarioEnvio() {
        return usuarioEnvio;
    }

    public void setUsuarioEnvio(Long usuarioEnvio) {
        this.usuarioEnvio = usuarioEnvio;
    }
}
