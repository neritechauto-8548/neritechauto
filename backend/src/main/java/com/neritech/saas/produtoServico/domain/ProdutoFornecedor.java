package com.neritech.saas.produtoServico.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "produtos_fornecedores")
public class ProdutoFornecedor extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @Column(name = "codigo_fornecedor", length = 50)
    private String codigoFornecedor;

    @Column(name = "descricao_fornecedor", length = 255)
    private String descricaoFornecedor;

    @Column(name = "preco_custo", precision = 10, scale = 4, nullable = false)
    private BigDecimal precoCusto;

    @Column(name = "preco_custo_anterior", precision = 10, scale = 4)
    private BigDecimal precoCustoAnterior;

    @Column(name = "data_ultimo_preco")
    private LocalDate dataUltimoPreco;

    @Column(name = "prazo_entrega_dias")
    private Integer prazoEntregaDias;

    @Column(name = "quantidade_minima", precision = 10, scale = 2)
    private BigDecimal quantidadeMinima;

    @Column(name = "desconto_quantidade")
    private String descontoQuantidade;

    @Column(name = "moeda", length = 3)
    private String moeda;

    @Column(name = "principal")
    private Boolean principal = false;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getCodigoFornecedor() {
        return codigoFornecedor;
    }

    public void setCodigoFornecedor(String codigoFornecedor) {
        this.codigoFornecedor = codigoFornecedor;
    }

    public String getDescricaoFornecedor() {
        return descricaoFornecedor;
    }

    public void setDescricaoFornecedor(String descricaoFornecedor) {
        this.descricaoFornecedor = descricaoFornecedor;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public BigDecimal getPrecoCustoAnterior() {
        return precoCustoAnterior;
    }

    public void setPrecoCustoAnterior(BigDecimal precoCustoAnterior) {
        this.precoCustoAnterior = precoCustoAnterior;
    }

    public LocalDate getDataUltimoPreco() {
        return dataUltimoPreco;
    }

    public void setDataUltimoPreco(LocalDate dataUltimoPreco) {
        this.dataUltimoPreco = dataUltimoPreco;
    }

    public Integer getPrazoEntregaDias() {
        return prazoEntregaDias;
    }

    public void setPrazoEntregaDias(Integer prazoEntregaDias) {
        this.prazoEntregaDias = prazoEntregaDias;
    }

    public BigDecimal getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(BigDecimal quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    public String getDescontoQuantidade() {
        return descontoQuantidade;
    }

    public void setDescontoQuantidade(String descontoQuantidade) {
        this.descontoQuantidade = descontoQuantidade;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
