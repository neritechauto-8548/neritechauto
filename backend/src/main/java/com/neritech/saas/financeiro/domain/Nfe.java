package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.financeiro.domain.enums.AmbienteNfe;
import com.neritech.saas.financeiro.domain.enums.StatusNfe;
import com.neritech.saas.financeiro.domain.enums.TipoEmissaoNfe;
import com.neritech.saas.financeiro.domain.enums.TipoOperacaoNfe;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fin_nfe")
@Getter
@Setter
public class Nfe extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fatura_id")
    private Fatura fatura;

    @Column(name = "numero_nfe", nullable = false)
    private Long numeroNfe;

    @Column(name = "serie_nfe", nullable = false)
    private Integer serieNfe;

    @Column(name = "chave_nfe", nullable = false, length = 44)
    private String chaveNfe;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacao", length = 20)
    private TipoOperacaoNfe tipoOperacao;

    @Column(name = "modelo_nf", length = 2)
    private String modeloNf = "55";

    @Column(name = "data_emissao", nullable = false)
    private LocalDateTime dataEmissao;

    @Column(name = "data_saida_entrada")
    private LocalDateTime dataSaidaEntrada;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_emissao", length = 20)
    private TipoEmissaoNfe tipoEmissao;

    @Enumerated(EnumType.STRING)
    @Column(name = "ambiente", length = 20)
    private AmbienteNfe ambiente;

    @Column(name = "codigo_verificacao", length = 8)
    private String codigoVerificacao;

    @Column(name = "digest_value", length = 28)
    private String digestValue;

    @Column(name = "protocolo_autorizacao", length = 20)
    private String protocoloAutorizacao;

    @Column(name = "data_autorizacao")
    private LocalDateTime dataAutorizacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_sefaz", length = 50)
    private StatusNfe statusSefaz;

    @Column(name = "motivo_status", columnDefinition = "TEXT")
    private String motivoStatus;

    @Column(name = "xml_nfe", columnDefinition = "TEXT")
    private String xmlNfe;

    @Column(name = "xml_cancelamento", columnDefinition = "TEXT")
    private String xmlCancelamento;

    @Column(name = "xml_carta_correcao", columnDefinition = "TEXT")
    private String xmlCartaCorrecao;

    @Column(name = "danfe_url", length = 500)
    private String danfeUrl;

    @Column(name = "qr_code", columnDefinition = "TEXT")
    private String qrCode;

    @Column(name = "url_consulta", length = 500)
    private String urlConsulta;

    @Column(name = "cancelada")
    private Boolean cancelada = false;

    @Column(name = "data_cancelamento")
    private LocalDateTime dataCancelamento;

    @Column(name = "motivo_cancelamento", columnDefinition = "TEXT")
    private String motivoCancelamento;

    @Column(name = "carta_correcao", columnDefinition = "TEXT")
    private String cartaCorrecao;

    @Column(name = "data_carta_correcao")
    private LocalDateTime dataCartaCorrecao;

    @Column(name = "valor_total_nf", nullable = false)
    private BigDecimal valorTotalNf;

    @Column(name = "valor_total_impostos")
    private BigDecimal valorTotalImpostos;

    @Column(name = "observacoes_contribuinte", columnDefinition = "TEXT")
    private String observacoesContribuinte;

    @Column(name = "informacoes_adicionais", columnDefinition = "TEXT")
    private String informacoesAdicionais;

    @Column(name = "gerada_por")
    private Long geradaPor;

    public Fatura getFatura() {
        return this.fatura;
    }
    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
    }
    public Long getNumeroNfe() {
        return this.numeroNfe;
    }
    public void setNumeroNfe(Long numeroNfe) {
        this.numeroNfe = numeroNfe;
    }
    public Integer getSerieNfe() {
        return this.serieNfe;
    }
    public void setSerieNfe(Integer serieNfe) {
        this.serieNfe = serieNfe;
    }
    public String getChaveNfe() {
        return this.chaveNfe;
    }
    public void setChaveNfe(String chaveNfe) {
        this.chaveNfe = chaveNfe;
    }
    public TipoOperacaoNfe getTipoOperacao() {
        return this.tipoOperacao;
    }
    public void setTipoOperacao(TipoOperacaoNfe tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }
    public LocalDateTime getDataEmissao() {
        return this.dataEmissao;
    }
    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }
    public LocalDateTime getDataSaidaEntrada() {
        return this.dataSaidaEntrada;
    }
    public void setDataSaidaEntrada(LocalDateTime dataSaidaEntrada) {
        this.dataSaidaEntrada = dataSaidaEntrada;
    }
    public TipoEmissaoNfe getTipoEmissao() {
        return this.tipoEmissao;
    }
    public void setTipoEmissao(TipoEmissaoNfe tipoEmissao) {
        this.tipoEmissao = tipoEmissao;
    }
    public AmbienteNfe getAmbiente() {
        return this.ambiente;
    }
    public void setAmbiente(AmbienteNfe ambiente) {
        this.ambiente = ambiente;
    }
    public String getCodigoVerificacao() {
        return this.codigoVerificacao;
    }
    public void setCodigoVerificacao(String codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
    }
    public String getDigestValue() {
        return this.digestValue;
    }
    public void setDigestValue(String digestValue) {
        this.digestValue = digestValue;
    }
    public String getProtocoloAutorizacao() {
        return this.protocoloAutorizacao;
    }
    public void setProtocoloAutorizacao(String protocoloAutorizacao) {
        this.protocoloAutorizacao = protocoloAutorizacao;
    }
    public LocalDateTime getDataAutorizacao() {
        return this.dataAutorizacao;
    }
    public void setDataAutorizacao(LocalDateTime dataAutorizacao) {
        this.dataAutorizacao = dataAutorizacao;
    }
    public StatusNfe getStatusSefaz() {
        return this.statusSefaz;
    }
    public void setStatusSefaz(StatusNfe statusSefaz) {
        this.statusSefaz = statusSefaz;
    }
    public String getMotivoStatus() {
        return this.motivoStatus;
    }
    public void setMotivoStatus(String motivoStatus) {
        this.motivoStatus = motivoStatus;
    }
    public String getXmlNfe() {
        return this.xmlNfe;
    }
    public void setXmlNfe(String xmlNfe) {
        this.xmlNfe = xmlNfe;
    }
    public String getXmlCancelamento() {
        return this.xmlCancelamento;
    }
    public void setXmlCancelamento(String xmlCancelamento) {
        this.xmlCancelamento = xmlCancelamento;
    }
    public String getXmlCartaCorrecao() {
        return this.xmlCartaCorrecao;
    }
    public void setXmlCartaCorrecao(String xmlCartaCorrecao) {
        this.xmlCartaCorrecao = xmlCartaCorrecao;
    }
    public String getDanfeUrl() {
        return this.danfeUrl;
    }
    public void setDanfeUrl(String danfeUrl) {
        this.danfeUrl = danfeUrl;
    }
    public String getQrCode() {
        return this.qrCode;
    }
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
    public String getUrlConsulta() {
        return this.urlConsulta;
    }
    public void setUrlConsulta(String urlConsulta) {
        this.urlConsulta = urlConsulta;
    }
    public LocalDateTime getDataCancelamento() {
        return this.dataCancelamento;
    }
    public void setDataCancelamento(LocalDateTime dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }
    public String getMotivoCancelamento() {
        return this.motivoCancelamento;
    }
    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }
    public String getCartaCorrecao() {
        return this.cartaCorrecao;
    }
    public void setCartaCorrecao(String cartaCorrecao) {
        this.cartaCorrecao = cartaCorrecao;
    }
    public LocalDateTime getDataCartaCorrecao() {
        return this.dataCartaCorrecao;
    }
    public void setDataCartaCorrecao(LocalDateTime dataCartaCorrecao) {
        this.dataCartaCorrecao = dataCartaCorrecao;
    }
    public BigDecimal getValorTotalNf() {
        return this.valorTotalNf;
    }
    public void setValorTotalNf(BigDecimal valorTotalNf) {
        this.valorTotalNf = valorTotalNf;
    }
    public BigDecimal getValorTotalImpostos() {
        return this.valorTotalImpostos;
    }
    public void setValorTotalImpostos(BigDecimal valorTotalImpostos) {
        this.valorTotalImpostos = valorTotalImpostos;
    }
    public String getObservacoesContribuinte() {
        return this.observacoesContribuinte;
    }
    public void setObservacoesContribuinte(String observacoesContribuinte) {
        this.observacoesContribuinte = observacoesContribuinte;
    }
    public String getInformacoesAdicionais() {
        return this.informacoesAdicionais;
    }
    public void setInformacoesAdicionais(String informacoesAdicionais) {
        this.informacoesAdicionais = informacoesAdicionais;
    }
    public Long getGeradaPor() {
        return this.geradaPor;
    }
    public void setGeradaPor(Long geradaPor) {
        this.geradaPor = geradaPor;
    }
}
