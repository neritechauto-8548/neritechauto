package com.neritech.saas.produtoServico.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos_fiscais")
public class ProdutoFiscal extends TenantEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    // Dados básicos
    @Column(name = "cean_trib", length = 50) private String ceanTrib;
    @Column(name = "cfop", length = 10) private String cfop;
    @Column(name = "ncm", length = 20) private String ncm;
    @Column(name = "cest", length = 20) private String cest;
    @Column(name = "un_comercial", length = 10) private String unComercial;
    @Column(name = "qtd_comercial", precision = 15, scale = 4) private BigDecimal qtdComercial;
    @Column(name = "valor_unit_comercial", precision = 15, scale = 4) private BigDecimal valorUnitComercial;
    @Column(name = "un_trib", length = 10) private String unTrib;
    @Column(name = "qtd_trib", precision = 15, scale = 4) private BigDecimal qtdTrib;
    @Column(name = "valor_unit_tributavel", precision = 15, scale = 4) private BigDecimal valorUnitTributavel;
    @Column(name = "total_seguro", precision = 15, scale = 4) private BigDecimal totalSeguro;
    @Column(name = "desconto", precision = 15, scale = 4) private BigDecimal desconto;
    @Column(name = "total_frete", precision = 15, scale = 4) private BigDecimal totalFrete;
    @Column(name = "outras_despesas", precision = 15, scale = 4) private BigDecimal outrasDespesas;
    @Column(name = "valor_total_bruto", precision = 15, scale = 4) private BigDecimal valorTotalBruto;
    @Column(name = "ex_tipi", length = 10) private String exTipi;
    @Column(name = "indicador_escala_relevante", length = 50) private String indicadorEscalaRelevante;
    @Column(name = "cnpj_fabricante", length = 20) private String cnpjFabricante;
    @Column(name = "codigo_beneficio_fiscal", length = 50) private String codigoBeneficioFiscal;
    @Column(name = "valor_bruto_compoe_total") private Boolean valorBrutoCompoeTotal;
    @Column(name = "pedido_compra", length = 50) private String pedidoCompra;
    @Column(name = "item_pedido_compra") private Integer itemPedidoCompra;
    @Column(name = "numero_fci", length = 50) private String numeroFci;
    @Column(name = "imposto_federal_aprox", precision = 5, scale = 2) private BigDecimal impostoFederalAprox;
    @Column(name = "imposto_estadual_aprox", precision = 5, scale = 2) private BigDecimal impostoEstadualAprox;

    // IPI
    @Column(name = "ipi_sit_trib", length = 100) private String ipiSitTrib;
    @Column(name = "ipi_classe_enquadramento", length = 50) private String ipiClasseEnquadramento;
    @Column(name = "ipi_cod_enquadramento", length = 50) private String ipiCodEnquadramento;
    @Column(name = "ipi_cnpj_produtor", length = 20) private String ipiCnpjProdutor;
    @Column(name = "ipi_cod_selo_controle", length = 50) private String ipiCodSeloControle;
    @Column(name = "ipi_qtd_selo") private Integer ipiQtdSelo;
    @Column(name = "ipi_tipo_calculo", length = 50) private String ipiTipoCalculo;
    @Column(name = "ipi_base_calc", precision = 15, scale = 4) private BigDecimal ipiBaseCalc;
    @Column(name = "ipi_aliquota", precision = 5, scale = 2) private BigDecimal ipiAliquota;
    @Column(name = "ipi_valor_unid_trib", precision = 15, scale = 4) private BigDecimal ipiValorUnidTrib;
    @Column(name = "ipi_valor", precision = 15, scale = 4) private BigDecimal ipiValor;

    // ICMS
    @Column(name = "icms_sit_trib", length = 100) private String icmsSitTrib;
    @Column(name = "icms_origem", length = 100) private String icmsOrigem;
    @Column(name = "icms_mod_bc", length = 50) private String icmsModBc;
    @Column(name = "icms_red_bc", precision = 5, scale = 2) private BigDecimal icmsRedBc;
    @Column(name = "icms_base_calc", precision = 15, scale = 4) private BigDecimal icmsBaseCalc;
    @Column(name = "icms_aliquota", precision = 5, scale = 2) private BigDecimal icmsAliquota;
    @Column(name = "icms_motivo_desoneracao", length = 50) private String icmsMotivoDesoneracao;
    @Column(name = "icms_valor_desoneracao", precision = 15, scale = 4) private BigDecimal icmsValorDesoneracao;
    @Column(name = "icms_valor", precision = 15, scale = 4) private BigDecimal icmsValor;
    @Column(name = "icms_bc_st_uf_origem", precision = 15, scale = 4) private BigDecimal icmsBcStUfOrigem;
    @Column(name = "icms_retido_antes", precision = 15, scale = 4) private BigDecimal icmsRetidoAntes;
    @Column(name = "icms_bc_st_uf_dest", precision = 15, scale = 4) private BigDecimal icmsBcStUfDest;
    @Column(name = "icms_st_uf_destino", precision = 15, scale = 4) private BigDecimal icmsStUfDestino;
    @Column(name = "icms_aliq_calc_credito", precision = 5, scale = 2) private BigDecimal icmsAliqCalcCredito;
    @Column(name = "icms_valor_credito", precision = 15, scale = 4) private BigDecimal icmsValorCredito;
    @Column(name = "icms_mod_bc_st", length = 50) private String icmsModBcSt;
    @Column(name = "icms_margem_valor_adicionado", precision = 5, scale = 2) private BigDecimal icmsMargemValorAdicionado;
    @Column(name = "icms_preco_unit_pauta_st", precision = 15, scale = 4) private BigDecimal icmsPrecoUnitPautaSt;
    @Column(name = "icms_red_bc_st", precision = 5, scale = 2) private BigDecimal icmsRedBcSt;
    @Column(name = "icms_base_calc_st", precision = 15, scale = 4) private BigDecimal icmsBaseCalcSt;
    @Column(name = "icms_aliquota_st", precision = 5, scale = 2) private BigDecimal icmsAliquotaSt;
    @Column(name = "icms_valor_st", precision = 15, scale = 4) private BigDecimal icmsValorSt;
    @Column(name = "icms_perc_bc_op_propria", precision = 5, scale = 2) private BigDecimal icmsPercBcOpPropria;
    @Column(name = "icms_uf_pgto_st", length = 2) private String icmsUfPgtoSt;
    @Column(name = "icms_perc_fcp", precision = 5, scale = 2) private BigDecimal icmsPercFcp;
    @Column(name = "icms_valor_fcp", precision = 15, scale = 4) private BigDecimal icmsValorFcp;
    @Column(name = "icms_bc_fcp", precision = 15, scale = 4) private BigDecimal icmsBcFcp;
    @Column(name = "icms_valor_bc_ret_st", precision = 15, scale = 4) private BigDecimal icmsValorBcRetSt;
    @Column(name = "icms_perc_fcp_ret_st", precision = 5, scale = 2) private BigDecimal icmsPercFcpRetSt;
    @Column(name = "icms_vb_calc_fcp_ret_ant_st", precision = 15, scale = 4) private BigDecimal icmsVbCalcFcpRetAntSt;
    @Column(name = "icms_perc_fcp_ret_ant_st", precision = 5, scale = 2) private BigDecimal icmsPercFcpRetAntSt;
    @Column(name = "icms_valor_bc_fcp_uf_dest", precision = 15, scale = 4) private BigDecimal icmsValorBcFcpUfDest;

    // PIS
    @Column(name = "pis_sit_trib", length = 100) private String pisSitTrib;
    @Column(name = "pis_tipo_calculo", length = 50) private String pisTipoCalculo;
    @Column(name = "pis_base_calc", precision = 15, scale = 4) private BigDecimal pisBaseCalc;
    @Column(name = "pis_aliquota", precision = 5, scale = 2) private BigDecimal pisAliquota;
    @Column(name = "pis_qv", precision = 15, scale = 4) private BigDecimal pisQv;
    @Column(name = "pis_rv", precision = 15, scale = 4) private BigDecimal pisRv;
    @Column(name = "pis_rvbc", precision = 15, scale = 4) private BigDecimal pisRvbc;
    @Column(name = "pis_valor_unid_trib", precision = 15, scale = 4) private BigDecimal pisValorUnidTrib;
    @Column(name = "pis_tipo_calculo_st", length = 50) private String pisTipoCalculoSt;
    @Column(name = "pis_aliquota_st", precision = 5, scale = 2) private BigDecimal pisAliquotaSt;
    @Column(name = "pis_valor_unid_trib_st", precision = 15, scale = 4) private BigDecimal pisValorUnidTribSt;

    // COFINS
    @Column(name = "cofins_sit_trib", length = 100) private String cofinsSitTrib;
    @Column(name = "cofins_base_calc", precision = 15, scale = 4) private BigDecimal cofinsBaseCalc;
    @Column(name = "cofins_aliquota", precision = 5, scale = 2) private BigDecimal cofinsAliquota;
    @Column(name = "cofins_valor_unid_trib", precision = 15, scale = 4) private BigDecimal cofinsValorUnidTrib;
    @Column(name = "cofins_valor", precision = 15, scale = 4) private BigDecimal cofinsValor;
    @Column(name = "cofins_tipo_calculo_st", length = 50) private String cofinsTipoCalculoSt;
    @Column(name = "cofins_aliquota_st", precision = 5, scale = 2) private BigDecimal cofinsAliquotaSt;
    @Column(name = "cofins_valor_unid_trib_st", precision = 15, scale = 4) private BigDecimal cofinsValorUnidTribSt;

    // Importadas
    @Column(name = "import_valor_bc", precision = 15, scale = 4) private BigDecimal importValorBc;
    @Column(name = "import_valor_desp_aduaneiras", precision = 15, scale = 4) private BigDecimal importValorDespAduaneiras;
    @Column(name = "import_valor_imposto", precision = 15, scale = 4) private BigDecimal importValorImposto;
    @Column(name = "import_valor_iof", precision = 15, scale = 4) private BigDecimal importValorIof;

    // Combustíveis
    @Column(name = "comb_cod_anp", length = 50) private String combCodAnp;
    @Column(name = "comb_perc_glp", precision = 5, scale = 2) private BigDecimal combPercGlp;
    @Column(name = "comb_cod_autorizacao_codif", length = 50) private String combCodAutorizacaoCodif;
    @Column(name = "comb_uf_consumo", length = 2) private String combUfConsumo;
    @Column(name = "comb_bc_cide", precision = 15, scale = 4) private BigDecimal combBcCide;
    @Column(name = "comb_aliquota_cide", precision = 15, scale = 4) private BigDecimal combAliquotaCide;
    @Column(name = "comb_valor_cide", precision = 15, scale = 4) private BigDecimal combValorCide;
    @Column(name = "comb_desc_anp", length = 255) private String combDescAnp;
    @Column(name = "comb_perc_glp_deriv_petroleo", precision = 5, scale = 2) private BigDecimal combPercGlpDerivPetroleo;
    @Column(name = "comb_perc_gas_nac", precision = 5, scale = 2) private BigDecimal combPercGasNac;
    @Column(name = "comb_perc_gas_imp", precision = 5, scale = 2) private BigDecimal combPercGasImp;
    @Column(name = "comb_valor_partida", precision = 15, scale = 4) private BigDecimal combValorPartida;

    // IBS e CBS
    @Column(name = "ibs_sit_trib", length = 50) private String ibsSitTrib;
    @Column(name = "ibs_classificacao_trib", length = 50) private String ibsClassificacaoTrib;
    @Column(name = "ibs_aliquota_uf", precision = 5, scale = 2) private BigDecimal ibsAliquotaUf;
    @Column(name = "ibs_perc_diferimento_uf", precision = 5, scale = 2) private BigDecimal ibsPercDiferimentoUf;
    @Column(name = "ibs_perc_red_aliquota_uf", precision = 5, scale = 2) private BigDecimal ibsPercRedAliquotaUf;
    @Column(name = "ibs_aliquota_municipio", precision = 5, scale = 2) private BigDecimal ibsAliquotaMunicipio;
    @Column(name = "cbs_aliquota", precision = 5, scale = 2) private BigDecimal cbsAliquota;
    @Column(name = "cbs_perc_diferimento", precision = 5, scale = 2) private BigDecimal cbsPercDiferimento;
    @Column(name = "cbs_perc_red_aliquota", precision = 5, scale = 2) private BigDecimal cbsPercRedAliquota;

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public String getCeanTrib() { return ceanTrib; } public void setCeanTrib(String ceanTrib) { this.ceanTrib = ceanTrib; }
    public String getCfop() { return cfop; } public void setCfop(String cfop) { this.cfop = cfop; }
    public String getNcm() { return ncm; } public void setNcm(String ncm) { this.ncm = ncm; }
    public String getCest() { return cest; } public void setCest(String cest) { this.cest = cest; }
    public String getUnComercial() { return unComercial; } public void setUnComercial(String unComercial) { this.unComercial = unComercial; }
    public BigDecimal getQtdComercial() { return qtdComercial; } public void setQtdComercial(BigDecimal qtdComercial) { this.qtdComercial = qtdComercial; }
    public BigDecimal getValorUnitComercial() { return valorUnitComercial; } public void setValorUnitComercial(BigDecimal valorUnitComercial) { this.valorUnitComercial = valorUnitComercial; }
    public String getUnTrib() { return unTrib; } public void setUnTrib(String unTrib) { this.unTrib = unTrib; }
    public BigDecimal getQtdTrib() { return qtdTrib; } public void setQtdTrib(BigDecimal qtdTrib) { this.qtdTrib = qtdTrib; }
    public BigDecimal getValorUnitTributavel() { return valorUnitTributavel; } public void setValorUnitTributavel(BigDecimal valorUnitTributavel) { this.valorUnitTributavel = valorUnitTributavel; }
    public BigDecimal getTotalSeguro() { return totalSeguro; } public void setTotalSeguro(BigDecimal totalSeguro) { this.totalSeguro = totalSeguro; }
    public BigDecimal getDesconto() { return desconto; } public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }
    public BigDecimal getTotalFrete() { return totalFrete; } public void setTotalFrete(BigDecimal totalFrete) { this.totalFrete = totalFrete; }
    public BigDecimal getOutrasDespesas() { return outrasDespesas; } public void setOutrasDespesas(BigDecimal outrasDespesas) { this.outrasDespesas = outrasDespesas; }
    public BigDecimal getValorTotalBruto() { return valorTotalBruto; } public void setValorTotalBruto(BigDecimal valorTotalBruto) { this.valorTotalBruto = valorTotalBruto; }
    public String getExTipi() { return exTipi; } public void setExTipi(String exTipi) { this.exTipi = exTipi; }
    public String getIndicadorEscalaRelevante() { return indicadorEscalaRelevante; } public void setIndicadorEscalaRelevante(String indicadorEscalaRelevante) { this.indicadorEscalaRelevante = indicadorEscalaRelevante; }
    public String getCnpjFabricante() { return cnpjFabricante; } public void setCnpjFabricante(String cnpjFabricante) { this.cnpjFabricante = cnpjFabricante; }
    public String getCodigoBeneficioFiscal() { return codigoBeneficioFiscal; } public void setCodigoBeneficioFiscal(String codigoBeneficioFiscal) { this.codigoBeneficioFiscal = codigoBeneficioFiscal; }
    public Boolean getValorBrutoCompoeTotal() { return valorBrutoCompoeTotal; } public void setValorBrutoCompoeTotal(Boolean valorBrutoCompoeTotal) { this.valorBrutoCompoeTotal = valorBrutoCompoeTotal; }
    public String getPedidoCompra() { return pedidoCompra; } public void setPedidoCompra(String pedidoCompra) { this.pedidoCompra = pedidoCompra; }
    public Integer getItemPedidoCompra() { return itemPedidoCompra; } public void setItemPedidoCompra(Integer itemPedidoCompra) { this.itemPedidoCompra = itemPedidoCompra; }
    public String getNumeroFci() { return numeroFci; } public void setNumeroFci(String numeroFci) { this.numeroFci = numeroFci; }
    public BigDecimal getImpostoFederalAprox() { return impostoFederalAprox; } public void setImpostoFederalAprox(BigDecimal impostoFederalAprox) { this.impostoFederalAprox = impostoFederalAprox; }
    public BigDecimal getImpostoEstadualAprox() { return impostoEstadualAprox; } public void setImpostoEstadualAprox(BigDecimal impostoEstadualAprox) { this.impostoEstadualAprox = impostoEstadualAprox; }

    public String getIpiSitTrib() { return ipiSitTrib; } public void setIpiSitTrib(String ipiSitTrib) { this.ipiSitTrib = ipiSitTrib; }
    public String getIpiClasseEnquadramento() { return ipiClasseEnquadramento; } public void setIpiClasseEnquadramento(String ipiClasseEnquadramento) { this.ipiClasseEnquadramento = ipiClasseEnquadramento; }
    public String getIpiCodEnquadramento() { return ipiCodEnquadramento; } public void setIpiCodEnquadramento(String ipiCodEnquadramento) { this.ipiCodEnquadramento = ipiCodEnquadramento; }
    public String getIpiCnpjProdutor() { return ipiCnpjProdutor; } public void setIpiCnpjProdutor(String ipiCnpjProdutor) { this.ipiCnpjProdutor = ipiCnpjProdutor; }
    public String getIpiCodSeloControle() { return ipiCodSeloControle; } public void setIpiCodSeloControle(String ipiCodSeloControle) { this.ipiCodSeloControle = ipiCodSeloControle; }
    public Integer getIpiQtdSelo() { return ipiQtdSelo; } public void setIpiQtdSelo(Integer ipiQtdSelo) { this.ipiQtdSelo = ipiQtdSelo; }
    public String getIpiTipoCalculo() { return ipiTipoCalculo; } public void setIpiTipoCalculo(String ipiTipoCalculo) { this.ipiTipoCalculo = ipiTipoCalculo; }
    public BigDecimal getIpiBaseCalc() { return ipiBaseCalc; } public void setIpiBaseCalc(BigDecimal ipiBaseCalc) { this.ipiBaseCalc = ipiBaseCalc; }
    public BigDecimal getIpiAliquota() { return ipiAliquota; } public void setIpiAliquota(BigDecimal ipiAliquota) { this.ipiAliquota = ipiAliquota; }
    public BigDecimal getIpiValorUnidTrib() { return ipiValorUnidTrib; } public void setIpiValorUnidTrib(BigDecimal ipiValorUnidTrib) { this.ipiValorUnidTrib = ipiValorUnidTrib; }
    public BigDecimal getIpiValor() { return ipiValor; } public void setIpiValor(BigDecimal ipiValor) { this.ipiValor = ipiValor; }

    public String getIcmsSitTrib() { return icmsSitTrib; } public void setIcmsSitTrib(String icmsSitTrib) { this.icmsSitTrib = icmsSitTrib; }
    public String getIcmsOrigem() { return icmsOrigem; } public void setIcmsOrigem(String icmsOrigem) { this.icmsOrigem = icmsOrigem; }
    public String getIcmsModBc() { return icmsModBc; } public void setIcmsModBc(String icmsModBc) { this.icmsModBc = icmsModBc; }
    public BigDecimal getIcmsRedBc() { return icmsRedBc; } public void setIcmsRedBc(BigDecimal icmsRedBc) { this.icmsRedBc = icmsRedBc; }
    public BigDecimal getIcmsBaseCalc() { return icmsBaseCalc; } public void setIcmsBaseCalc(BigDecimal icmsBaseCalc) { this.icmsBaseCalc = icmsBaseCalc; }
    public BigDecimal getIcmsAliquota() { return icmsAliquota; } public void setIcmsAliquota(BigDecimal icmsAliquota) { this.icmsAliquota = icmsAliquota; }
    public String getIcmsMotivoDesoneracao() { return icmsMotivoDesoneracao; } public void setIcmsMotivoDesoneracao(String icmsMotivoDesoneracao) { this.icmsMotivoDesoneracao = icmsMotivoDesoneracao; }
    public BigDecimal getIcmsValorDesoneracao() { return icmsValorDesoneracao; } public void setIcmsValorDesoneracao(BigDecimal icmsValorDesoneracao) { this.icmsValorDesoneracao = icmsValorDesoneracao; }
    public BigDecimal getIcmsValor() { return icmsValor; } public void setIcmsValor(BigDecimal icmsValor) { this.icmsValor = icmsValor; }
    public BigDecimal getIcmsBcStUfOrigem() { return icmsBcStUfOrigem; } public void setIcmsBcStUfOrigem(BigDecimal icmsBcStUfOrigem) { this.icmsBcStUfOrigem = icmsBcStUfOrigem; }
    public BigDecimal getIcmsRetidoAntes() { return icmsRetidoAntes; } public void setIcmsRetidoAntes(BigDecimal icmsRetidoAntes) { this.icmsRetidoAntes = icmsRetidoAntes; }
    public BigDecimal getIcmsBcStUfDest() { return icmsBcStUfDest; } public void setIcmsBcStUfDest(BigDecimal icmsBcStUfDest) { this.icmsBcStUfDest = icmsBcStUfDest; }
    public BigDecimal getIcmsStUfDestino() { return icmsStUfDestino; } public void setIcmsStUfDestino(BigDecimal icmsStUfDestino) { this.icmsStUfDestino = icmsStUfDestino; }
    public BigDecimal getIcmsAliqCalcCredito() { return icmsAliqCalcCredito; } public void setIcmsAliqCalcCredito(BigDecimal icmsAliqCalcCredito) { this.icmsAliqCalcCredito = icmsAliqCalcCredito; }
    public BigDecimal getIcmsValorCredito() { return icmsValorCredito; } public void setIcmsValorCredito(BigDecimal icmsValorCredito) { this.icmsValorCredito = icmsValorCredito; }
    public String getIcmsModBcSt() { return icmsModBcSt; } public void setIcmsModBcSt(String icmsModBcSt) { this.icmsModBcSt = icmsModBcSt; }
    public BigDecimal getIcmsMargemValorAdicionado() { return icmsMargemValorAdicionado; } public void setIcmsMargemValorAdicionado(BigDecimal icmsMargemValorAdicionado) { this.icmsMargemValorAdicionado = icmsMargemValorAdicionado; }
    public BigDecimal getIcmsPrecoUnitPautaSt() { return icmsPrecoUnitPautaSt; } public void setIcmsPrecoUnitPautaSt(BigDecimal icmsPrecoUnitPautaSt) { this.icmsPrecoUnitPautaSt = icmsPrecoUnitPautaSt; }
    public BigDecimal getIcmsRedBcSt() { return icmsRedBcSt; } public void setIcmsRedBcSt(BigDecimal icmsRedBcSt) { this.icmsRedBcSt = icmsRedBcSt; }
    public BigDecimal getIcmsBaseCalcSt() { return icmsBaseCalcSt; } public void setIcmsBaseCalcSt(BigDecimal icmsBaseCalcSt) { this.icmsBaseCalcSt = icmsBaseCalcSt; }
    public BigDecimal getIcmsAliquotaSt() { return icmsAliquotaSt; } public void setIcmsAliquotaSt(BigDecimal icmsAliquotaSt) { this.icmsAliquotaSt = icmsAliquotaSt; }
    public BigDecimal getIcmsValorSt() { return icmsValorSt; } public void setIcmsValorSt(BigDecimal icmsValorSt) { this.icmsValorSt = icmsValorSt; }
    public BigDecimal getIcmsPercBcOpPropria() { return icmsPercBcOpPropria; } public void setIcmsPercBcOpPropria(BigDecimal icmsPercBcOpPropria) { this.icmsPercBcOpPropria = icmsPercBcOpPropria; }
    public String getIcmsUfPgtoSt() { return icmsUfPgtoSt; } public void setIcmsUfPgtoSt(String icmsUfPgtoSt) { this.icmsUfPgtoSt = icmsUfPgtoSt; }
    public BigDecimal getIcmsPercFcp() { return icmsPercFcp; } public void setIcmsPercFcp(BigDecimal icmsPercFcp) { this.icmsPercFcp = icmsPercFcp; }
    public BigDecimal getIcmsValorFcp() { return icmsValorFcp; } public void setIcmsValorFcp(BigDecimal icmsValorFcp) { this.icmsValorFcp = icmsValorFcp; }
    public BigDecimal getIcmsBcFcp() { return icmsBcFcp; } public void setIcmsBcFcp(BigDecimal icmsBcFcp) { this.icmsBcFcp = icmsBcFcp; }
    public BigDecimal getIcmsValorBcRetSt() { return icmsValorBcRetSt; } public void setIcmsValorBcRetSt(BigDecimal icmsValorBcRetSt) { this.icmsValorBcRetSt = icmsValorBcRetSt; }
    public BigDecimal getIcmsPercFcpRetSt() { return icmsPercFcpRetSt; } public void setIcmsPercFcpRetSt(BigDecimal icmsPercFcpRetSt) { this.icmsPercFcpRetSt = icmsPercFcpRetSt; }
    public BigDecimal getIcmsVbCalcFcpRetAntSt() { return icmsVbCalcFcpRetAntSt; } public void setIcmsVbCalcFcpRetAntSt(BigDecimal icmsVbCalcFcpRetAntSt) { this.icmsVbCalcFcpRetAntSt = icmsVbCalcFcpRetAntSt; }
    public BigDecimal getIcmsPercFcpRetAntSt() { return icmsPercFcpRetAntSt; } public void setIcmsPercFcpRetAntSt(BigDecimal icmsPercFcpRetAntSt) { this.icmsPercFcpRetAntSt = icmsPercFcpRetAntSt; }
    public BigDecimal getIcmsValorBcFcpUfDest() { return icmsValorBcFcpUfDest; } public void setIcmsValorBcFcpUfDest(BigDecimal icmsValorBcFcpUfDest) { this.icmsValorBcFcpUfDest = icmsValorBcFcpUfDest; }

    public String getPisSitTrib() { return pisSitTrib; } public void setPisSitTrib(String pisSitTrib) { this.pisSitTrib = pisSitTrib; }
    public String getPisTipoCalculo() { return pisTipoCalculo; } public void setPisTipoCalculo(String pisTipoCalculo) { this.pisTipoCalculo = pisTipoCalculo; }
    public BigDecimal getPisBaseCalc() { return pisBaseCalc; } public void setPisBaseCalc(BigDecimal pisBaseCalc) { this.pisBaseCalc = pisBaseCalc; }
    public BigDecimal getPisAliquota() { return pisAliquota; } public void setPisAliquota(BigDecimal pisAliquota) { this.pisAliquota = pisAliquota; }
    public BigDecimal getPisQv() { return pisQv; } public void setPisQv(BigDecimal pisQv) { this.pisQv = pisQv; }
    public BigDecimal getPisRv() { return pisRv; } public void setPisRv(BigDecimal pisRv) { this.pisRv = pisRv; }
    public BigDecimal getPisRvbc() { return pisRvbc; } public void setPisRvbc(BigDecimal pisRvbc) { this.pisRvbc = pisRvbc; }
    public BigDecimal getPisValorUnidTrib() { return pisValorUnidTrib; } public void setPisValorUnidTrib(BigDecimal pisValorUnidTrib) { this.pisValorUnidTrib = pisValorUnidTrib; }
    public String getPisTipoCalculoSt() { return pisTipoCalculoSt; } public void setPisTipoCalculoSt(String pisTipoCalculoSt) { this.pisTipoCalculoSt = pisTipoCalculoSt; }
    public BigDecimal getPisAliquotaSt() { return pisAliquotaSt; } public void setPisAliquotaSt(BigDecimal pisAliquotaSt) { this.pisAliquotaSt = pisAliquotaSt; }
    public BigDecimal getPisValorUnidTribSt() { return pisValorUnidTribSt; } public void setPisValorUnidTribSt(BigDecimal pisValorUnidTribSt) { this.pisValorUnidTribSt = pisValorUnidTribSt; }

    public String getCofinsSitTrib() { return cofinsSitTrib; } public void setCofinsSitTrib(String cofinsSitTrib) { this.cofinsSitTrib = cofinsSitTrib; }
    public BigDecimal getCofinsBaseCalc() { return cofinsBaseCalc; } public void setCofinsBaseCalc(BigDecimal cofinsBaseCalc) { this.cofinsBaseCalc = cofinsBaseCalc; }
    public BigDecimal getCofinsAliquota() { return cofinsAliquota; } public void setCofinsAliquota(BigDecimal cofinsAliquota) { this.cofinsAliquota = cofinsAliquota; }
    public BigDecimal getCofinsValorUnidTrib() { return cofinsValorUnidTrib; } public void setCofinsValorUnidTrib(BigDecimal cofinsValorUnidTrib) { this.cofinsValorUnidTrib = cofinsValorUnidTrib; }
    public BigDecimal getCofinsValor() { return cofinsValor; } public void setCofinsValor(BigDecimal cofinsValor) { this.cofinsValor = cofinsValor; }
    public String getCofinsTipoCalculoSt() { return cofinsTipoCalculoSt; } public void setCofinsTipoCalculoSt(String cofinsTipoCalculoSt) { this.cofinsTipoCalculoSt = cofinsTipoCalculoSt; }
    public BigDecimal getCofinsAliquotaSt() { return cofinsAliquotaSt; } public void setCofinsAliquotaSt(BigDecimal cofinsAliquotaSt) { this.cofinsAliquotaSt = cofinsAliquotaSt; }
    public BigDecimal getCofinsValorUnidTribSt() { return cofinsValorUnidTribSt; } public void setCofinsValorUnidTribSt(BigDecimal cofinsValorUnidTribSt) { this.cofinsValorUnidTribSt = cofinsValorUnidTribSt; }

    public BigDecimal getImportValorBc() { return importValorBc; } public void setImportValorBc(BigDecimal importValorBc) { this.importValorBc = importValorBc; }
    public BigDecimal getImportValorDespAduaneiras() { return importValorDespAduaneiras; } public void setImportValorDespAduaneiras(BigDecimal importValorDespAduaneiras) { this.importValorDespAduaneiras = importValorDespAduaneiras; }
    public BigDecimal getImportValorImposto() { return importValorImposto; } public void setImportValorImposto(BigDecimal importValorImposto) { this.importValorImposto = importValorImposto; }
    public BigDecimal getImportValorIof() { return importValorIof; } public void setImportValorIof(BigDecimal importValorIof) { this.importValorIof = importValorIof; }

    public String getCombCodAnp() { return combCodAnp; } public void setCombCodAnp(String combCodAnp) { this.combCodAnp = combCodAnp; }
    public BigDecimal getCombPercGlp() { return combPercGlp; } public void setCombPercGlp(BigDecimal combPercGlp) { this.combPercGlp = combPercGlp; }
    public String getCombCodAutorizacaoCodif() { return combCodAutorizacaoCodif; } public void setCombCodAutorizacaoCodif(String combCodAutorizacaoCodif) { this.combCodAutorizacaoCodif = combCodAutorizacaoCodif; }
    public String getCombUfConsumo() { return combUfConsumo; } public void setCombUfConsumo(String combUfConsumo) { this.combUfConsumo = combUfConsumo; }
    public BigDecimal getCombBcCide() { return combBcCide; } public void setCombBcCide(BigDecimal combBcCide) { this.combBcCide = combBcCide; }
    public BigDecimal getCombAliquotaCide() { return combAliquotaCide; } public void setCombAliquotaCide(BigDecimal combAliquotaCide) { this.combAliquotaCide = combAliquotaCide; }
    public BigDecimal getCombValorCide() { return combValorCide; } public void setCombValorCide(BigDecimal combValorCide) { this.combValorCide = combValorCide; }
    public String getCombDescAnp() { return combDescAnp; } public void setCombDescAnp(String combDescAnp) { this.combDescAnp = combDescAnp; }
    public BigDecimal getCombPercGlpDerivPetroleo() { return combPercGlpDerivPetroleo; } public void setCombPercGlpDerivPetroleo(BigDecimal combPercGlpDerivPetroleo) { this.combPercGlpDerivPetroleo = combPercGlpDerivPetroleo; }
    public BigDecimal getCombPercGasNac() { return combPercGasNac; } public void setCombPercGasNac(BigDecimal combPercGasNac) { this.combPercGasNac = combPercGasNac; }
    public BigDecimal getCombPercGasImp() { return combPercGasImp; } public void setCombPercGasImp(BigDecimal combPercGasImp) { this.combPercGasImp = combPercGasImp; }
    public BigDecimal getCombValorPartida() { return combValorPartida; } public void setCombValorPartida(BigDecimal combValorPartida) { this.combValorPartida = combValorPartida; }

    public String getIbsSitTrib() { return ibsSitTrib; } public void setIbsSitTrib(String ibsSitTrib) { this.ibsSitTrib = ibsSitTrib; }
    public String getIbsClassificacaoTrib() { return ibsClassificacaoTrib; } public void setIbsClassificacaoTrib(String ibsClassificacaoTrib) { this.ibsClassificacaoTrib = ibsClassificacaoTrib; }
    public BigDecimal getIbsAliquotaUf() { return ibsAliquotaUf; } public void setIbsAliquotaUf(BigDecimal ibsAliquotaUf) { this.ibsAliquotaUf = ibsAliquotaUf; }
    public BigDecimal getIbsPercDiferimentoUf() { return ibsPercDiferimentoUf; } public void setIbsPercDiferimentoUf(BigDecimal ibsPercDiferimentoUf) { this.ibsPercDiferimentoUf = ibsPercDiferimentoUf; }
    public BigDecimal getIbsPercRedAliquotaUf() { return ibsPercRedAliquotaUf; } public void setIbsPercRedAliquotaUf(BigDecimal ibsPercRedAliquotaUf) { this.ibsPercRedAliquotaUf = ibsPercRedAliquotaUf; }
    public BigDecimal getIbsAliquotaMunicipio() { return ibsAliquotaMunicipio; } public void setIbsAliquotaMunicipio(BigDecimal ibsAliquotaMunicipio) { this.ibsAliquotaMunicipio = ibsAliquotaMunicipio; }
    public BigDecimal getCbsAliquota() { return cbsAliquota; } public void setCbsAliquota(BigDecimal cbsAliquota) { this.cbsAliquota = cbsAliquota; }
    public BigDecimal getCbsPercDiferimento() { return cbsPercDiferimento; } public void setCbsPercDiferimento(BigDecimal cbsPercDiferimento) { this.cbsPercDiferimento = cbsPercDiferimento; }
    public BigDecimal getCbsPercRedAliquota() { return cbsPercRedAliquota; } public void setCbsPercRedAliquota(BigDecimal cbsPercRedAliquota) { this.cbsPercRedAliquota = cbsPercRedAliquota; }
}
