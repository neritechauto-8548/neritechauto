package com.neritech.saas.produtoServico.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.produtoServico.domain.enums.OrigemProduto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private CategoriaProduto categoria;

    @NotBlank
    @Size(max = 50)
    @Column(name = "codigo_interno", nullable = false, length = 50, unique = true)
    private String codigoInterno;

    @Size(max = 50)
    @Column(name = "codigo_barras", length = 50)
    private String codigoBarras;

    @Size(max = 50)
    @Column(name = "codigo_fabricante", length = 50)
    private String codigoFabricante;

    @NotBlank
    @Size(max = 255)
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "descricao_detalhada")
    private String descricaoDetalhada;

    @Size(max = 100)
    @Column(name = "marca", length = 100)
    private String marca;

    @Size(max = 100)
    @Column(name = "modelo", length = 100)
    private String modelo;

    @Column(name = "aplicacao", columnDefinition = "TEXT")
    private String aplicacao;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "especificacoes_tecnicas")
    private String especificacoesTecnicas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_medida_id")
    private UnidadeMedida unidadeMedida;

    @Column(name = "peso_liquido", precision = 8, scale = 3)
    private BigDecimal pesoLiquido;

    @Column(name = "peso_bruto", precision = 8, scale = 3)
    private BigDecimal pesoBruto;

    @Column(name = "dimensoes_comprimento", precision = 8, scale = 2)
    private BigDecimal dimensoesComprimento;

    @Column(name = "dimensoes_largura", precision = 8, scale = 2)
    private BigDecimal dimensoesLargura;

    @Column(name = "dimensoes_altura", precision = 8, scale = 2)
    private BigDecimal dimensoesAltura;

    @Column(name = "preco_custo", precision = 10, scale = 4, nullable = false)
    private BigDecimal precoCusto;

    @Column(name = "preco_venda", precision = 10, scale = 2, nullable = false)
    private BigDecimal precoVenda;

    @Column(name = "margem_lucro_percentual", precision = 5, scale = 2)
    private BigDecimal margemLucroPercentual;

    @Column(name = "estoque_minimo", precision = 10, scale = 2)
    private BigDecimal estoqueMinimo;

    @Column(name = "estoque_maximo", precision = 10, scale = 2)
    private BigDecimal estoqueMaximo;

    @Column(name = "ponto_reposicao", precision = 10, scale = 2)
    private BigDecimal pontoReposicao;

    @Column(name = "controla_lote")
    private Boolean controlaLote = false;

    @Column(name = "controla_validade")
    private Boolean controlaValidade = false;

    @Column(name = "dias_validade")
    private Integer diasValidade;

    @Column(name = "ncm", length = 8)
    private String ncm;

    @Column(name = "cest", length = 7)
    private String cest;

    @Column(name = "origem_produto", length = 1)
    private String origemProduto;

    @Column(name = "foto_principal_url", length = 500)
    private String fotoPrincipalUrl;

    @Column(name = "garantia_meses")
    private Integer garantiaMeses;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "destaque")
    private Boolean destaque = false;

    @Column(name = "promocional")
    private Boolean promocional = false;

    @Column(name = "pontos_fidelidade")
    private Integer pontosFidelidade;

    @Column(name = "comissao_venda_percentual", precision = 5, scale = 2)
    private BigDecimal comissaoVendaPercentual;

    @Formula("(SELECT COALESCE(SUM(e.quantidade_atual), 0) FROM estoques e WHERE e.produto_id = id AND e.empresa_id = empresa_id)")
    private BigDecimal quantidadeEstoque;

    public CategoriaProduto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProduto categoria) {
        this.categoria = categoria;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getCodigoFabricante() {
        return codigoFabricante;
    }

    public void setCodigoFabricante(String codigoFabricante) {
        this.codigoFabricante = codigoFabricante;
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

    public String getDescricaoDetalhada() {
        return descricaoDetalhada;
    }

    public void setDescricaoDetalhada(String descricaoDetalhada) {
        this.descricaoDetalhada = descricaoDetalhada;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAplicacao() {
        return aplicacao;
    }

    public void setAplicacao(String aplicacao) {
        this.aplicacao = aplicacao;
    }

    public String getEspecificacoesTecnicas() {
        return especificacoesTecnicas;
    }

    public void setEspecificacoesTecnicas(String especificacoesTecnicas) {
        this.especificacoesTecnicas = especificacoesTecnicas;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public BigDecimal getPesoLiquido() {
        return pesoLiquido;
    }

    public void setPesoLiquido(BigDecimal pesoLiquido) {
        this.pesoLiquido = pesoLiquido;
    }

    public BigDecimal getPesoBruto() {
        return pesoBruto;
    }

    public void setPesoBruto(BigDecimal pesoBruto) {
        this.pesoBruto = pesoBruto;
    }

    public BigDecimal getDimensoesComprimento() {
        return dimensoesComprimento;
    }

    public void setDimensoesComprimento(BigDecimal dimensoesComprimento) {
        this.dimensoesComprimento = dimensoesComprimento;
    }

    public BigDecimal getDimensoesLargura() {
        return dimensoesLargura;
    }

    public void setDimensoesLargura(BigDecimal dimensoesLargura) {
        this.dimensoesLargura = dimensoesLargura;
    }

    public BigDecimal getDimensoesAltura() {
        return dimensoesAltura;
    }

    public void setDimensoesAltura(BigDecimal dimensoesAltura) {
        this.dimensoesAltura = dimensoesAltura;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public BigDecimal getMargemLucroPercentual() {
        return margemLucroPercentual;
    }

    public void setMargemLucroPercentual(BigDecimal margemLucroPercentual) {
        this.margemLucroPercentual = margemLucroPercentual;
    }

    public BigDecimal getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(BigDecimal estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public BigDecimal getEstoqueMaximo() {
        return estoqueMaximo;
    }

    public void setEstoqueMaximo(BigDecimal estoqueMaximo) {
        this.estoqueMaximo = estoqueMaximo;
    }

    public BigDecimal getPontoReposicao() {
        return pontoReposicao;
    }

    public void setPontoReposicao(BigDecimal pontoReposicao) {
        this.pontoReposicao = pontoReposicao;
    }

    public Boolean getControlaLote() {
        return controlaLote;
    }

    public void setControlaLote(Boolean controlaLote) {
        this.controlaLote = controlaLote;
    }

    public Boolean getControlaValidade() {
        return controlaValidade;
    }

    public void setControlaValidade(Boolean controlaValidade) {
        this.controlaValidade = controlaValidade;
    }

    public Integer getDiasValidade() {
        return diasValidade;
    }

    public void setDiasValidade(Integer diasValidade) {
        this.diasValidade = diasValidade;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public String getCest() {
        return cest;
    }

    public void setCest(String cest) {
        this.cest = cest;
    }

    public String getOrigemProduto() {
        return origemProduto;
    }

    public void setOrigemProduto(String origemProduto) {
        this.origemProduto = origemProduto;
    }

    public String getFotoPrincipalUrl() {
        return fotoPrincipalUrl;
    }

    public void setFotoPrincipalUrl(String fotoPrincipalUrl) {
        this.fotoPrincipalUrl = fotoPrincipalUrl;
    }

    public Integer getGarantiaMeses() {
        return garantiaMeses;
    }

    public void setGarantiaMeses(Integer garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getDestaque() {
        return destaque;
    }

    public void setDestaque(Boolean destaque) {
        this.destaque = destaque;
    }

    public Boolean getPromocional() {
        return promocional;
    }

    public void setPromocional(Boolean promocional) {
        this.promocional = promocional;
    }

    public Integer getPontosFidelidade() {
        return pontosFidelidade;
    }

    public void setPontosFidelidade(Integer pontosFidelidade) {
        this.pontosFidelidade = pontosFidelidade;
    }

    public BigDecimal getComissaoVendaPercentual() {
        return comissaoVendaPercentual;
    }

    public void setComissaoVendaPercentual(BigDecimal comissaoVendaPercentual) {
        this.comissaoVendaPercentual = comissaoVendaPercentual;
    }

    public BigDecimal getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(BigDecimal quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }
}
