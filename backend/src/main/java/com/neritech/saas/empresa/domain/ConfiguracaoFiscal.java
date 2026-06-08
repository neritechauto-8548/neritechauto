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

    // --- NFe Padrões ---
    @Column(name = "situacao_tributaria_icms_nfe", length = 100)
    private String situacaoTributariaIcmsNfe;

    @Column(name = "situacao_tributaria_pis_nfe", length = 100)
    private String situacaoTributariaPisNfe;

    @Column(name = "situacao_tributaria_cofins_nfe", length = 100)
    private String situacaoTributariaCofinsNfe;

    @Column(name = "mensagem_dados_adicionais_nfe", columnDefinition = "TEXT")
    private String mensagemDadosAdicionaisNfe;

    @Column(name = "mostrar_cnpj_nfe")
    private Boolean mostrarCnpjNfe = false;

    @Column(name = "utilizar_codigo_produto_original_nfe")
    private Boolean utilizarCodigoProdutoOriginalNfe = false;

    // --- NFCe Padrões ---
    @Column(name = "situacao_tributaria_icms_nfce", length = 100)
    private String situacaoTributariaIcmsNfce;

    @Column(name = "situacao_tributaria_pis_nfce", length = 100)
    private String situacaoTributariaPisNfce;

    @Column(name = "situacao_tributaria_cofins_nfce", length = 100)
    private String situacaoTributariaCofinsNfce;

    @Column(name = "mensagem_dados_adicionais_nfce", columnDefinition = "TEXT")
    private String mensagemDadosAdicionaisNfce;

    @Column(name = "serie_nfce")
    private Integer serieNfce = 1;

    @Column(name = "cfop_padrao_nfce", length = 4)
    private String cfopPadraoNfce = "5102";

    // --- NFSe Padrões Homologação ---
    @Column(name = "sequencial_rpse_homologacao")
    private Long sequencialRpseHomologacao;

    @Column(name = "serie_rpse_homologacao", length = 5)
    private String serieRpseHomologacao;

    @Column(name = "sequencial_lote_rpse_homologacao")
    private Long sequencialLoteRpseHomologacao;

    @Column(name = "usuario_acesso_provedor_homologacao", length = 100)
    private String usuarioAcessoProvedorHomologacao;

    @Column(name = "token_acesso_provedor_homologacao", length = 255)
    private String tokenAcessoProvedorHomologacao;

    // --- NFSe Padrões Produção ---
    @Column(name = "sequencial_rpse_producao")
    private Long sequencialRpseProducao;

    @Column(name = "serie_rpse_producao", length = 5)
    private String serieRpseProducao;

    @Column(name = "sequencial_lote_rpse_producao")
    private Long sequencialLoteRpseProducao;

    @Column(name = "usuario_acesso_provedor_producao", length = 100)
    private String usuarioAcessoProvedorProducao;

    @Column(name = "token_acesso_provedor_producao", length = 255)
    private String tokenAcessoProvedorProducao;

    // --- NFSe Padrões Gerais ---
    @Column(name = "cnae_servico", length = 20)
    private String cnaeServico;

    @Column(name = "codigo_servico_municipal", length = 50)
    private String codigoServicoMunicipal;

    @Column(name = "item_lista_servico", length = 50)
    private String itemListaServico;

    @Column(name = "codigo_nbs", length = 20)
    private String codigoNbs;

    @Column(name = "codigo_tributacao_municipio", length = 50)
    private String codigoTributacaoMunicipio;

    @Column(name = "unidade_servico", length = 20)
    private String unidadeServico;

    @Column(name = "descricao_servico_municipal", columnDefinition = "TEXT")
    private String descricaoServicoMunicipal;

    @Column(name = "natureza_operacao_nfse", length = 50)
    private String naturezaOperacaoNfse;

    @Column(name = "regime_especial_tributacao_nfse", length = 50)
    private String regimeEspecialTributacaoNfse;

    @Column(name = "iss_retido_fonte")
    private Boolean issRetidoFonte = false;

    @Column(name = "outras_informacoes_nfse", columnDefinition = "TEXT")
    private String outrasInformacoesNfse;

    @Column(name = "logo_nfse_path", length = 255)
    private String logoNfsePath;

    // --- Reforma Tributária (IBS/CBS) ---
    @Column(name = "aliquota_ibs", precision = 8, scale = 4)
    private BigDecimal aliquotaIbs;

    @Column(name = "percentual_diferimento_ibs", precision = 8, scale = 4)
    private BigDecimal percentualDiferimentoIbs;

    @Column(name = "percentual_reducao_ibs", precision = 8, scale = 4)
    private BigDecimal percentualReducaoIbs;

    @Column(name = "aliquota_cbs", precision = 8, scale = 4)
    private BigDecimal aliquotaCbs;

    @Column(name = "percentual_diferimento_cbs", precision = 8, scale = 4)
    private BigDecimal percentualDiferimentoCbs;

    @Column(name = "percentual_reducao_cbs", precision = 8, scale = 4)
    private BigDecimal percentualReducaoCbs;

    @Column(name = "situacao_tributaria_ibs_cbs", length = 100)
    private String situacaoTributariaIbsCbs;

    @Column(name = "classificacao_tributaria_ibs_cbs", length = 100)
    private String classificacaoTributariaIbsCbs;

    // Getters and Setters NFe Padrões
    public String getSituacaoTributariaIcmsNfe() { return situacaoTributariaIcmsNfe; }
    public void setSituacaoTributariaIcmsNfe(String situacaoTributariaIcmsNfe) { this.situacaoTributariaIcmsNfe = situacaoTributariaIcmsNfe; }

    public String getSituacaoTributariaPisNfe() { return situacaoTributariaPisNfe; }
    public void setSituacaoTributariaPisNfe(String situacaoTributariaPisNfe) { this.situacaoTributariaPisNfe = situacaoTributariaPisNfe; }

    public String getSituacaoTributariaCofinsNfe() { return situacaoTributariaCofinsNfe; }
    public void setSituacaoTributariaCofinsNfe(String situacaoTributariaCofinsNfe) { this.situacaoTributariaCofinsNfe = situacaoTributariaCofinsNfe; }

    public String getMensagemDadosAdicionaisNfe() { return mensagemDadosAdicionaisNfe; }
    public void setMensagemDadosAdicionaisNfe(String mensagemDadosAdicionaisNfe) { this.mensagemDadosAdicionaisNfe = mensagemDadosAdicionaisNfe; }

    public Boolean getMostrarCnpjNfe() { return mostrarCnpjNfe; }
    public void setMostrarCnpjNfe(Boolean mostrarCnpjNfe) { this.mostrarCnpjNfe = mostrarCnpjNfe; }

    public Boolean getUtilizarCodigoProdutoOriginalNfe() { return utilizarCodigoProdutoOriginalNfe; }
    public void setUtilizarCodigoProdutoOriginalNfe(Boolean utilizarCodigoProdutoOriginalNfe) { this.utilizarCodigoProdutoOriginalNfe = utilizarCodigoProdutoOriginalNfe; }

    // Getters and Setters NFCe Padrões
    public String getSituacaoTributariaIcmsNfce() { return situacaoTributariaIcmsNfce; }
    public void setSituacaoTributariaIcmsNfce(String situacaoTributariaIcmsNfce) { this.situacaoTributariaIcmsNfce = situacaoTributariaIcmsNfce; }

    public String getSituacaoTributariaPisNfce() { return situacaoTributariaPisNfce; }
    public void setSituacaoTributariaPisNfce(String situacaoTributariaPisNfce) { this.situacaoTributariaPisNfce = situacaoTributariaPisNfce; }

    public String getSituacaoTributariaCofinsNfce() { return situacaoTributariaCofinsNfce; }
    public void setSituacaoTributariaCofinsNfce(String situacaoTributariaCofinsNfce) { this.situacaoTributariaCofinsNfce = situacaoTributariaCofinsNfce; }

    public String getMensagemDadosAdicionaisNfce() { return mensagemDadosAdicionaisNfce; }
    public void setMensagemDadosAdicionaisNfce(String mensagemDadosAdicionaisNfce) { this.mensagemDadosAdicionaisNfce = mensagemDadosAdicionaisNfce; }

    public Integer getSerieNfce() { return serieNfce; }
    public void setSerieNfce(Integer serieNfce) { this.serieNfce = serieNfce; }

    public String getCfopPadraoNfce() { return cfopPadraoNfce; }
    public void setCfopPadraoNfce(String cfopPadraoNfce) { this.cfopPadraoNfce = cfopPadraoNfce; }

    // Getters and Setters NFSe Padrões Homologação
    public Long getSequencialRpseHomologacao() { return sequencialRpseHomologacao; }
    public void setSequencialRpseHomologacao(Long sequencialRpseHomologacao) { this.sequencialRpseHomologacao = sequencialRpseHomologacao; }

    public String getSerieRpseHomologacao() { return serieRpseHomologacao; }
    public void setSerieRpseHomologacao(String serieRpseHomologacao) { this.serieRpseHomologacao = serieRpseHomologacao; }

    public Long getSequencialLoteRpseHomologacao() { return sequencialLoteRpseHomologacao; }
    public void setSequencialLoteRpseHomologacao(Long sequencialLoteRpseHomologacao) { this.sequencialLoteRpseHomologacao = sequencialLoteRpseHomologacao; }

    public String getUsuarioAcessoProvedorHomologacao() { return usuarioAcessoProvedorHomologacao; }
    public void setUsuarioAcessoProvedorHomologacao(String usuarioAcessoProvedorHomologacao) { this.usuarioAcessoProvedorHomologacao = usuarioAcessoProvedorHomologacao; }

    public String getTokenAcessoProvedorHomologacao() { return tokenAcessoProvedorHomologacao; }
    public void setTokenAcessoProvedorHomologacao(String tokenAcessoProvedorHomologacao) { this.tokenAcessoProvedorHomologacao = tokenAcessoProvedorHomologacao; }

    // Getters and Setters NFSe Padrões Produção
    public Long getSequencialRpseProducao() { return sequencialRpseProducao; }
    public void setSequencialRpseProducao(Long sequencialRpseProducao) { this.sequencialRpseProducao = sequencialRpseProducao; }

    public String getSerieRpseProducao() { return serieRpseProducao; }
    public void setSerieRpseProducao(String serieRpseProducao) { this.serieRpseProducao = serieRpseProducao; }

    public Long getSequencialLoteRpseProducao() { return sequencialLoteRpseProducao; }
    public void setSequencialLoteRpseProducao(Long sequencialLoteRpseProducao) { this.sequencialLoteRpseProducao = sequencialLoteRpseProducao; }

    public String getUsuarioAcessoProvedorProducao() { return usuarioAcessoProvedorProducao; }
    public void setUsuarioAcessoProvedorProducao(String usuarioAcessoProvedorProducao) { this.usuarioAcessoProvedorProducao = usuarioAcessoProvedorProducao; }

    public String getTokenAcessoProvedorProducao() { return tokenAcessoProvedorProducao; }
    public void setTokenAcessoProvedorProducao(String tokenAcessoProvedorProducao) { this.tokenAcessoProvedorProducao = tokenAcessoProvedorProducao; }

    // Getters and Setters NFSe Padrões Gerais
    public String getCnaeServico() { return cnaeServico; }
    public void setCnaeServico(String cnaeServico) { this.cnaeServico = cnaeServico; }

    public String getCodigoServicoMunicipal() { return codigoServicoMunicipal; }
    public void setCodigoServicoMunicipal(String codigoServicoMunicipal) { this.codigoServicoMunicipal = codigoServicoMunicipal; }

    public String getItemListaServico() { return itemListaServico; }
    public void setItemListaServico(String itemListaServico) { this.itemListaServico = itemListaServico; }

    public String getCodigoNbs() { return codigoNbs; }
    public void setCodigoNbs(String codigoNbs) { this.codigoNbs = codigoNbs; }

    public String getCodigoTributacaoMunicipio() { return codigoTributacaoMunicipio; }
    public void setCodigoTributacaoMunicipio(String codigoTributacaoMunicipio) { this.codigoTributacaoMunicipio = codigoTributacaoMunicipio; }

    public String getUnidadeServico() { return unidadeServico; }
    public void setUnidadeServico(String unidadeServico) { this.unidadeServico = unidadeServico; }

    public String getDescricaoServicoMunicipal() { return descricaoServicoMunicipal; }
    public void setDescricaoServicoMunicipal(String descricaoServicoMunicipal) { this.descricaoServicoMunicipal = descricaoServicoMunicipal; }

    public String getNaturezaOperacaoNfse() { return naturezaOperacaoNfse; }
    public void setNaturezaOperacaoNfse(String naturezaOperacaoNfse) { this.naturezaOperacaoNfse = naturezaOperacaoNfse; }

    public String getRegimeEspecialTributacaoNfse() { return regimeEspecialTributacaoNfse; }
    public void setRegimeEspecialTributacaoNfse(String regimeEspecialTributacaoNfse) { this.regimeEspecialTributacaoNfse = regimeEspecialTributacaoNfse; }

    public Boolean getIssRetidoFonte() { return issRetidoFonte; }
    public void setIssRetidoFonte(Boolean issRetidoFonte) { this.issRetidoFonte = issRetidoFonte; }

    public String getOutrasInformacoesNfse() { return outrasInformacoesNfse; }
    public void setOutrasInformacoesNfse(String outrasInformacoesNfse) { this.outrasInformacoesNfse = outrasInformacoesNfse; }

    public String getLogoNfsePath() { return logoNfsePath; }
    public void setLogoNfsePath(String logoNfsePath) { this.logoNfsePath = logoNfsePath; }

    // Getters and Setters Reforma Tributária (IBS/CBS)
    public BigDecimal getAliquotaIbs() { return aliquotaIbs; }
    public void setAliquotaIbs(BigDecimal aliquotaIbs) { this.aliquotaIbs = aliquotaIbs; }

    public BigDecimal getPercentualDiferimentoIbs() { return percentualDiferimentoIbs; }
    public void setPercentualDiferimentoIbs(BigDecimal percentualDiferimentoIbs) { this.percentualDiferimentoIbs = percentualDiferimentoIbs; }

    public BigDecimal getPercentualReducaoIbs() { return percentualReducaoIbs; }
    public void setPercentualReducaoIbs(BigDecimal percentualReducaoIbs) { this.percentualReducaoIbs = percentualReducaoIbs; }

    public BigDecimal getAliquotaCbs() { return aliquotaCbs; }
    public void setAliquotaCbs(BigDecimal aliquotaCbs) { this.aliquotaCbs = aliquotaCbs; }

    public BigDecimal getPercentualDiferimentoCbs() { return percentualDiferimentoCbs; }
    public void setPercentualDiferimentoCbs(BigDecimal percentualDiferimentoCbs) { this.percentualDiferimentoCbs = percentualDiferimentoCbs; }

    public BigDecimal getPercentualReducaoCbs() { return percentualReducaoCbs; }
    public void setPercentualReducaoCbs(BigDecimal percentualReducaoCbs) { this.percentualReducaoCbs = percentualReducaoCbs; }

    public String getSituacaoTributariaIbsCbs() { return situacaoTributariaIbsCbs; }
    public void setSituacaoTributariaIbsCbs(String situacaoTributariaIbsCbs) { this.situacaoTributariaIbsCbs = situacaoTributariaIbsCbs; }

    public String getClassificacaoTributariaIbsCbs() { return classificacaoTributariaIbsCbs; }
    public void setClassificacaoTributariaIbsCbs(String classificacaoTributariaIbsCbs) { this.classificacaoTributariaIbsCbs = classificacaoTributariaIbsCbs; }
}
