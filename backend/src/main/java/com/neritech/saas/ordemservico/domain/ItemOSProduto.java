package com.neritech.saas.ordemservico.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.estoque.domain.enums.MotivoDevolucao;
import com.neritech.saas.produtoServico.domain.Fornecedor;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "itens_os_produtos")
public class ItemOSProduto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;

    @Column(name = "produto_id")
    private Long produtoId;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "quantidade", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;

    @Column(name = "valor_unitario", nullable = false, precision = 10, scale = 4)
    private BigDecimal valorUnitario;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "desconto_percentual", precision = 5, scale = 2)
    private BigDecimal descontoPercentual = BigDecimal.ZERO;

    @Column(name = "desconto_valor", precision = 10, scale = 2)
    private BigDecimal descontoValor = BigDecimal.ZERO;

    @Column(name = "valor_final", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorFinal;

    @Column(name = "lote_numero", length = 50)
    private String loteNumero;

    @Column(name = "localizacao_estoque_id")
    private Long localizacaoEstoqueId;

    @Column(name = "quantidade_reservada", precision = 10, scale = 2)
    private BigDecimal quantidadeReservada;

    @Column(name = "quantidade_utilizada", precision = 10, scale = 2)
    private BigDecimal quantidadeUtilizada;

    @Column(name = "data_reserva")
    private LocalDateTime dataReserva;

    @Column(name = "data_utilizacao")
    private LocalDateTime dataUtilizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @Column(name = "preco_custo", precision = 10, scale = 4)
    private BigDecimal precoCusto;

    @Column(name = "margem_lucro_realizada", precision = 5, scale = 2)
    private BigDecimal margemLucroRealizada;

    @Column(name = "garantia_meses")
    private Integer garantiaMeses = 0;

    @Column(name = "numero_serie", length = 100)
    private String numeroSerie;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "aprovado_cliente")
    private Boolean aprovadoCliente = false;

    @Column(name = "data_aprovacao_cliente")
    private LocalDateTime dataAprovacaoCliente;

    @Column(name = "devolvido")
    private Boolean devolvido = false;

    @Column(name = "quantidade_devolvida", precision = 10, scale = 2)
    private BigDecimal quantidadeDevolvida = BigDecimal.ZERO;

    @Column(name = "motivo_devolucao", columnDefinition = "TEXT")
    private String motivoDevolucao;

    // Getters and Setters
    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getDescontoPercentual() {
        return descontoPercentual;
    }

    public void setDescontoPercentual(BigDecimal descontoPercentual) {
        this.descontoPercentual = descontoPercentual;
    }

    public BigDecimal getDescontoValor() {
        return descontoValor;
    }

    public void setDescontoValor(BigDecimal descontoValor) {
        this.descontoValor = descontoValor;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getLoteNumero() {
        return loteNumero;
    }

    public void setLoteNumero(String loteNumero) {
        this.loteNumero = loteNumero;
    }

    public Long getLocalizacaoEstoqueId() {
        return localizacaoEstoqueId;
    }

    public void setLocalizacaoEstoqueId(Long localizacaoEstoqueId) {
        this.localizacaoEstoqueId = localizacaoEstoqueId;
    }

    public BigDecimal getQuantidadeReservada() {
        return quantidadeReservada;
    }

    public void setQuantidadeReservada(BigDecimal quantidadeReservada) {
        this.quantidadeReservada = quantidadeReservada;
    }

    public BigDecimal getQuantidadeUtilizada() {
        return quantidadeUtilizada;
    }

    public void setQuantidadeUtilizada(BigDecimal quantidadeUtilizada) {
        this.quantidadeUtilizada = quantidadeUtilizada;
    }

    public LocalDateTime getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDateTime dataReserva) {
        this.dataReserva = dataReserva;
    }

    public LocalDateTime getDataUtilizacao() {
        return dataUtilizacao;
    }

    public void setDataUtilizacao(LocalDateTime dataUtilizacao) {
        this.dataUtilizacao = dataUtilizacao;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public BigDecimal getMargemLucroRealizada() {
        return margemLucroRealizada;
    }

    public void setMargemLucroRealizada(BigDecimal margemLucroRealizada) {
        this.margemLucroRealizada = margemLucroRealizada;
    }

    public Integer getGarantiaMeses() {
        return garantiaMeses;
    }

    public void setGarantiaMeses(Integer garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getAprovadoCliente() {
        return aprovadoCliente;
    }

    public void setAprovadoCliente(Boolean aprovadoCliente) {
        this.aprovadoCliente = aprovadoCliente;
    }

    public LocalDateTime getDataAprovacaoCliente() {
        return dataAprovacaoCliente;
    }

    public void setDataAprovacaoCliente(LocalDateTime dataAprovacaoCliente) {
        this.dataAprovacaoCliente = dataAprovacaoCliente;
    }

    public Boolean getDevolvido() {
        return devolvido;
    }

    public void setDevolvido(Boolean devolvido) {
        this.devolvido = devolvido;
    }

    public BigDecimal getQuantidadeDevolvida() {
        return quantidadeDevolvida;
    }

    public void setQuantidadeDevolvida(BigDecimal quantidadeDevolvida) {
        this.quantidadeDevolvida = quantidadeDevolvida;
    }

    public String getMotivoDevolucao() {
        return motivoDevolucao;
    }

    public void setMotivoDevolucao(String motivoDevolucao) {
        this.motivoDevolucao = motivoDevolucao;
    }
}
