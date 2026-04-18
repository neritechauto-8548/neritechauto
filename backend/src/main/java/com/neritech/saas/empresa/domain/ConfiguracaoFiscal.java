package com.neritech.saas.empresa.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.empresa.domain.enums.RegimeTributario;
import com.neritech.saas.empresa.domain.enums.AmbienteNFe;
import com.neritech.saas.empresa.domain.enums.AnexoSimples;
import com.neritech.saas.empresa.domain.Empresa;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "configuracoes_fiscais", uniqueConstraints = {
        @UniqueConstraint(name = "uk_config_fiscal_empresa", columnNames = "empresa_id")
})
public class ConfiguracaoFiscal extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "regime_tributario", length = 20)
    @Enumerated(EnumType.STRING)
    private RegimeTributario regimeTributario;

    @Column(name = "anexo_simples", length = 5)
    @Enumerated(EnumType.STRING)
    private AnexoSimples anexoSimples;

    @Column(name = "aliquota_iss", precision = 8, scale = 4)
    private BigDecimal aliquotaIss;

    @Column(name = "aliquota_icms", precision = 8, scale = 4)
    private BigDecimal aliquotaIcms;

    @Column(name = "aliquota_pis", precision = 8, scale = 4)
    private BigDecimal aliquotaPis;

    @Column(name = "aliquota_cofins", precision = 8, scale = 4)
    private BigDecimal aliquotaCofins;

    @Column(name = "aliquota_csll", precision = 8, scale = 4)
    private BigDecimal aliquotaCsll;

    @Column(name = "aliquota_irpj", precision = 8, scale = 4)
    private BigDecimal aliquotaIrpj;

    @Column(name = "codigo_municipio_prestacao", length = 7)
    private String codigoMunicipioPrestacao;

    @Column(name = "cfop_venda_dentro_estado", length = 4)
    private String cfopVendaDentroEstado = "5102";

    @Column(name = "cfop_venda_fora_estado", length = 4)
    private String cfopVendaForaEstado = "6102";

    @Column(name = "cfop_servico", length = 4)
    private String cfopServico = "5933";

    @Column(name = "serie_nfe")
    private Integer serieNfe = 1;

    @Column(name = "numeracao_nfe")
    private Long numeracaoNfe = 1L;

    @Column(name = "ambiente_nfe", length = 20)
    @Enumerated(EnumType.STRING)
    private AmbienteNFe ambienteNfe = AmbienteNFe.HOMOLOGACAO;

    @Column(name = "certificado_digital_a1", columnDefinition = "TEXT")
    private String certificadoDigitalA1;

    @Column(name = "senha_certificado", length = 255)
    private String senhaCertificado;

    @Column(name = "validade_certificado")
    private LocalDate validadeCertificado;

    public ConfiguracaoFiscal() {
    }

    // Getters and Setters
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public RegimeTributario getRegimeTributario() {
        return regimeTributario;
    }

    public void setRegimeTributario(RegimeTributario regimeTributario) {
        this.regimeTributario = regimeTributario;
    }

    public AnexoSimples getAnexoSimples() {
        return anexoSimples;
    }

    public void setAnexoSimples(AnexoSimples anexoSimples) {
        this.anexoSimples = anexoSimples;
    }

    public BigDecimal getAliquotaIss() {
        return aliquotaIss;
    }

    public void setAliquotaIss(BigDecimal aliquotaIss) {
        this.aliquotaIss = aliquotaIss;
    }

    public BigDecimal getAliquotaIcms() {
        return aliquotaIcms;
    }

    public void setAliquotaIcms(BigDecimal aliquotaIcms) {
        this.aliquotaIcms = aliquotaIcms;
    }

    public BigDecimal getAliquotaPis() {
        return aliquotaPis;
    }

    public void setAliquotaPis(BigDecimal aliquotaPis) {
        this.aliquotaPis = aliquotaPis;
    }

    public BigDecimal getAliquotaCofins() {
        return aliquotaCofins;
    }

    public void setAliquotaCofins(BigDecimal aliquotaCofins) {
        this.aliquotaCofins = aliquotaCofins;
    }

    public BigDecimal getAliquotaCsll() {
        return aliquotaCsll;
    }

    public void setAliquotaCsll(BigDecimal aliquotaCsll) {
        this.aliquotaCsll = aliquotaCsll;
    }

    public BigDecimal getAliquotaIrpj() {
        return aliquotaIrpj;
    }

    public void setAliquotaIrpj(BigDecimal aliquotaIrpj) {
        this.aliquotaIrpj = aliquotaIrpj;
    }

    public String getCodigoMunicipioPrestacao() {
        return codigoMunicipioPrestacao;
    }

    public void setCodigoMunicipioPrestacao(String codigoMunicipioPrestacao) {
        this.codigoMunicipioPrestacao = codigoMunicipioPrestacao;
    }

    public String getCfopVendaDentroEstado() {
        return cfopVendaDentroEstado;
    }

    public void setCfopVendaDentroEstado(String cfopVendaDentroEstado) {
        this.cfopVendaDentroEstado = cfopVendaDentroEstado;
    }

    public String getCfopVendaForaEstado() {
        return cfopVendaForaEstado;
    }

    public void setCfopVendaForaEstado(String cfopVendaForaEstado) {
        this.cfopVendaForaEstado = cfopVendaForaEstado;
    }

    public String getCfopServico() {
        return cfopServico;
    }

    public void setCfopServico(String cfopServico) {
        this.cfopServico = cfopServico;
    }

    public Integer getSerieNfe() {
        return serieNfe;
    }

    public void setSerieNfe(Integer serieNfe) {
        this.serieNfe = serieNfe;
    }

    public Long getNumeracaoNfe() {
        return numeracaoNfe;
    }

    public void setNumeracaoNfe(Long numeracaoNfe) {
        this.numeracaoNfe = numeracaoNfe;
    }

    public AmbienteNFe getAmbienteNfe() {
        return ambienteNfe;
    }

    public void setAmbienteNfe(AmbienteNFe ambienteNfe) {
        this.ambienteNfe = ambienteNfe;
    }

    public String getCertificadoDigitalA1() {
        return certificadoDigitalA1;
    }

    public void setCertificadoDigitalA1(String certificadoDigitalA1) {
        this.certificadoDigitalA1 = certificadoDigitalA1;
    }

    public String getSenhaCertificado() {
        return senhaCertificado;
    }

    public void setSenhaCertificado(String senhaCertificado) {
        this.senhaCertificado = senhaCertificado;
    }

    public LocalDate getValidadeCertificado() {
        return validadeCertificado;
    }

    public void setValidadeCertificado(LocalDate validadeCertificado) {
        this.validadeCertificado = validadeCertificado;
    }
}
