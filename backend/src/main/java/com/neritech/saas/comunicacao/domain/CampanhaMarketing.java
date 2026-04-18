package com.neritech.saas.comunicacao.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.StatusCampanha;
import com.neritech.saas.comunicacao.domain.enums.TipoCampanha;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "com_campanhas_marketing")
public class CampanhaMarketing extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_campanha", nullable = false, length = 30)
    private TipoCampanha tipoCampanha;

    @Column(name = "objetivo", columnDefinition = "TEXT")
    private String objetivo;

    @Column(name = "publico_alvo", columnDefinition = "TEXT")
    private String publicoAlvo;

    @Column(name = "canais_comunicacao", columnDefinition = "TEXT")
    private String canaisComunicacao;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    @Column(name = "orcamento_total", precision = 10, scale = 2)
    private BigDecimal orcamentoTotal;

    @Column(name = "custo_realizado", precision = 10, scale = 2)
    private BigDecimal custoRealizado = BigDecimal.ZERO;

    @Column(name = "meta_alcance")
    private Integer metaAlcance;

    @Column(name = "alcance_realizado")
    private Integer alcanceRealizado = 0;

    @Column(name = "meta_conversao")
    private Integer metaConversao;

    @Column(name = "conversao_realizada")
    private Integer conversaoRealizada = 0;

    @Column(name = "template_email_id")
    private Long templateEmailId;

    @Column(name = "template_sms_id")
    private Long templateSmsId;

    @Column(name = "template_whatsapp_id")
    private Long templateWhatsappId;

    @Column(name = "promocao_associada_id")
    private Long promocaoAssociadaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusCampanha status;

    @Column(name = "aprovada_por")
    private Long aprovadaPor;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "resultados_detalhados", columnDefinition = "TEXT")
    private String resultadosDetalhados;

    @Column(name = "roi_calculado", precision = 8, scale = 2)
    private BigDecimal roiCalculado;

    @Column(name = "criada_por")
    private Long criadaPor;

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoCampanha getTipoCampanha() {
        return tipoCampanha;
    }

    public void setTipoCampanha(TipoCampanha tipoCampanha) {
        this.tipoCampanha = tipoCampanha;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getPublicoAlvo() {
        return publicoAlvo;
    }

    public void setPublicoAlvo(String publicoAlvo) {
        this.publicoAlvo = publicoAlvo;
    }

    public String getCanaisComunicacao() {
        return canaisComunicacao;
    }

    public void setCanaisComunicacao(String canaisComunicacao) {
        this.canaisComunicacao = canaisComunicacao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public BigDecimal getOrcamentoTotal() {
        return orcamentoTotal;
    }

    public void setOrcamentoTotal(BigDecimal orcamentoTotal) {
        this.orcamentoTotal = orcamentoTotal;
    }

    public BigDecimal getCustoRealizado() {
        return custoRealizado;
    }

    public void setCustoRealizado(BigDecimal custoRealizado) {
        this.custoRealizado = custoRealizado;
    }

    public Integer getMetaAlcance() {
        return metaAlcance;
    }

    public void setMetaAlcance(Integer metaAlcance) {
        this.metaAlcance = metaAlcance;
    }

    public Integer getAlcanceRealizado() {
        return alcanceRealizado;
    }

    public void setAlcanceRealizado(Integer alcanceRealizado) {
        this.alcanceRealizado = alcanceRealizado;
    }

    public Integer getMetaConversao() {
        return metaConversao;
    }

    public void setMetaConversao(Integer metaConversao) {
        this.metaConversao = metaConversao;
    }

    public Integer getConversaoRealizada() {
        return conversaoRealizada;
    }

    public void setConversaoRealizada(Integer conversaoRealizada) {
        this.conversaoRealizada = conversaoRealizada;
    }

    public Long getTemplateEmailId() {
        return templateEmailId;
    }

    public void setTemplateEmailId(Long templateEmailId) {
        this.templateEmailId = templateEmailId;
    }

    public Long getTemplateSmsId() {
        return templateSmsId;
    }

    public void setTemplateSmsId(Long templateSmsId) {
        this.templateSmsId = templateSmsId;
    }

    public Long getTemplateWhatsappId() {
        return templateWhatsappId;
    }

    public void setTemplateWhatsappId(Long templateWhatsappId) {
        this.templateWhatsappId = templateWhatsappId;
    }

    public Long getPromocaoAssociadaId() {
        return promocaoAssociadaId;
    }

    public void setPromocaoAssociadaId(Long promocaoAssociadaId) {
        this.promocaoAssociadaId = promocaoAssociadaId;
    }

    public StatusCampanha getStatus() {
        return status;
    }

    public void setStatus(StatusCampanha status) {
        this.status = status;
    }

    public Long getAprovadaPor() {
        return aprovadaPor;
    }

    public void setAprovadaPor(Long aprovadaPor) {
        this.aprovadaPor = aprovadaPor;
    }

    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getResultadosDetalhados() {
        return resultadosDetalhados;
    }

    public void setResultadosDetalhados(String resultadosDetalhados) {
        this.resultadosDetalhados = resultadosDetalhados;
    }

    public BigDecimal getRoiCalculado() {
        return roiCalculado;
    }

    public void setRoiCalculado(BigDecimal roiCalculado) {
        this.roiCalculado = roiCalculado;
    }

    public Long getCriadaPor() {
        return criadaPor;
    }

    public void setCriadaPor(Long criadaPor) {
        this.criadaPor = criadaPor;
    }
}
