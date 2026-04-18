package com.neritech.saas.produtoServico.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.produtoServico.domain.enums.TipoPromocao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promocoes")
public class Promocao extends TenantEntity {

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_promocao", length = 30)
    private TipoPromocao tipoPromocao;

    @Column(name = "valor_desconto", precision = 10, scale = 2)
    private BigDecimal valorDesconto;

    @Column(name = "percentual_desconto", precision = 5, scale = 2)
    private BigDecimal percentualDesconto;

    @Column(name = "quantidade_leve")
    private Integer quantidadeLeve;

    @Column(name = "quantidade_pague")
    private Integer quantidadePague;

    @Column(name = "valor_minimo_pedido", precision = 10, scale = 2)
    private BigDecimal valorMinimoPedido;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    @Column(name = "limite_uso_total")
    private Integer limiteUsoTotal;

    @Column(name = "limite_uso_cliente")
    private Integer limiteUsoCliente;

    @Column(name = "vezes_utilizada")
    private Integer vezesUtilizada;

    @Column(name = "codigo_cupom", length = 20)
    private String codigoCupom;

    @Column(name = "aplicacao_automatica")
    private Boolean aplicacaoAutomatica = false;

    @Column(name = "categorias_aplicaveis")
    private String categoriasAplicaveis;

    @Column(name = "produtos_aplicaveis")
    private String produtosAplicaveis;

    @Column(name = "clientes_aplicaveis")
    private String clientesAplicaveis;

    @Column(name = "canais_venda")
    private String canaisVenda;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "ativo")
    private Boolean ativo = true;
   
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

    public TipoPromocao getTipoPromocao() {
        return tipoPromocao;
    }

    public void setTipoPromocao(TipoPromocao tipoPromocao) {
        this.tipoPromocao = tipoPromocao;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(BigDecimal percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    public Integer getQuantidadeLeve() {
        return quantidadeLeve;
    }

    public void setQuantidadeLeve(Integer quantidadeLeve) {
        this.quantidadeLeve = quantidadeLeve;
    }

    public Integer getQuantidadePague() {
        return quantidadePague;
    }

    public void setQuantidadePague(Integer quantidadePague) {
        this.quantidadePague = quantidadePague;
    }

    public BigDecimal getValorMinimoPedido() {
        return valorMinimoPedido;
    }

    public void setValorMinimoPedido(BigDecimal valorMinimoPedido) {
        this.valorMinimoPedido = valorMinimoPedido;
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

    public Integer getLimiteUsoTotal() {
        return limiteUsoTotal;
    }

    public void setLimiteUsoTotal(Integer limiteUsoTotal) {
        this.limiteUsoTotal = limiteUsoTotal;
    }

    public Integer getLimiteUsoCliente() {
        return limiteUsoCliente;
    }

    public void setLimiteUsoCliente(Integer limiteUsoCliente) {
        this.limiteUsoCliente = limiteUsoCliente;
    }

    public Integer getVezesUtilizada() {
        return vezesUtilizada;
    }

    public void setVezesUtilizada(Integer vezesUtilizada) {
        this.vezesUtilizada = vezesUtilizada;
    }

    public String getCodigoCupom() {
        return codigoCupom;
    }

    public void setCodigoCupom(String codigoCupom) {
        this.codigoCupom = codigoCupom;
    }

    public Boolean getAplicacaoAutomatica() {
        return aplicacaoAutomatica;
    }

    public void setAplicacaoAutomatica(Boolean aplicacaoAutomatica) {
        this.aplicacaoAutomatica = aplicacaoAutomatica;
    }

    public String getCategoriasAplicaveis() {
        return categoriasAplicaveis;
    }

    public void setCategoriasAplicaveis(String categoriasAplicaveis) {
        this.categoriasAplicaveis = categoriasAplicaveis;
    }

    public String getProdutosAplicaveis() {
        return produtosAplicaveis;
    }

    public void setProdutosAplicaveis(String produtosAplicaveis) {
        this.produtosAplicaveis = produtosAplicaveis;
    }

    public String getClientesAplicaveis() {
        return clientesAplicaveis;
    }

    public void setClientesAplicaveis(String clientesAplicaveis) {
        this.clientesAplicaveis = clientesAplicaveis;
    }

    public String getCanaisVenda() {
        return canaisVenda;
    }

    public void setCanaisVenda(String canaisVenda) {
        this.canaisVenda = canaisVenda;
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
}
