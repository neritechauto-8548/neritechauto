package com.neritech.saas.estoque.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.estoque.domain.enums.StatusItemInventario;
import com.neritech.saas.produtoServico.domain.Produto;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "itens_inventario")
public class ItemInventario extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_id", nullable = false)
    private Inventario inventario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localizacao_id")
    private LocalizacaoEstoque localizacao;

    @Column(name = "lote_numero", length = 50)
    private String loteNumero;

    @Column(name = "quantidade_sistema", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeSistema;

    @Column(name = "quantidade_contada", precision = 10, scale = 2)
    private BigDecimal quantidadeContada;

    @Column(name = "diferenca", precision = 10, scale = 2)
    private BigDecimal diferenca;

    @Column(name = "valor_unitario", precision = 10, scale = 4)
    private BigDecimal valorUnitario;

    @Column(name = "valor_total_sistema", precision = 15, scale = 2)
    private BigDecimal valorTotalSistema;

    @Column(name = "valor_total_contado", precision = 15, scale = 2)
    private BigDecimal valorTotalContado;

    @Column(name = "diferenca_valor", precision = 15, scale = 2)
    private BigDecimal diferencaValor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusItemInventario status = StatusItemInventario.PENDENTE;

    @Column(name = "motivo_diferenca", columnDefinition = "TEXT")
    private String motivoDiferenca;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "usuario_contagem")
    private Long usuarioContagem;

    @Column(name = "data_contagem")
    private LocalDateTime dataContagem;

    @Column(name = "usuario_conferencia")
    private Long usuarioConferencia;

    @Column(name = "data_conferencia")
    private LocalDateTime dataConferencia;

    @Column(name = "foto_comprovante_url", length = 500)
    private String fotoComprovanteUrl;

    // Getters and Setters
    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public LocalizacaoEstoque getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(LocalizacaoEstoque localizacao) {
        this.localizacao = localizacao;
    }

    public String getLoteNumero() {
        return loteNumero;
    }

    public void setLoteNumero(String loteNumero) {
        this.loteNumero = loteNumero;
    }

    public BigDecimal getQuantidadeSistema() {
        return quantidadeSistema;
    }

    public void setQuantidadeSistema(BigDecimal quantidadeSistema) {
        this.quantidadeSistema = quantidadeSistema;
    }

    public BigDecimal getQuantidadeContada() {
        return quantidadeContada;
    }

    public void setQuantidadeContada(BigDecimal quantidadeContada) {
        this.quantidadeContada = quantidadeContada;
    }

    public BigDecimal getDiferenca() {
        return diferenca;
    }

    public void setDiferenca(BigDecimal diferenca) {
        this.diferenca = diferenca;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorTotalSistema() {
        return valorTotalSistema;
    }

    public void setValorTotalSistema(BigDecimal valorTotalSistema) {
        this.valorTotalSistema = valorTotalSistema;
    }

    public BigDecimal getValorTotalContado() {
        return valorTotalContado;
    }

    public void setValorTotalContado(BigDecimal valorTotalContado) {
        this.valorTotalContado = valorTotalContado;
    }

    public BigDecimal getDiferencaValor() {
        return diferencaValor;
    }

    public void setDiferencaValor(BigDecimal diferencaValor) {
        this.diferencaValor = diferencaValor;
    }

    public StatusItemInventario getStatus() {
        return status;
    }

    public void setStatus(StatusItemInventario status) {
        this.status = status;
    }

    public String getMotivoDiferenca() {
        return motivoDiferenca;
    }

    public void setMotivoDiferenca(String motivoDiferenca) {
        this.motivoDiferenca = motivoDiferenca;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getUsuarioContagem() {
        return usuarioContagem;
    }

    public void setUsuarioContagem(Long usuarioContagem) {
        this.usuarioContagem = usuarioContagem;
    }

    public LocalDateTime getDataContagem() {
        return dataContagem;
    }

    public void setDataContagem(LocalDateTime dataContagem) {
        this.dataContagem = dataContagem;
    }

    public Long getUsuarioConferencia() {
        return usuarioConferencia;
    }

    public void setUsuarioConferencia(Long usuarioConferencia) {
        this.usuarioConferencia = usuarioConferencia;
    }

    public LocalDateTime getDataConferencia() {
        return dataConferencia;
    }

    public void setDataConferencia(LocalDateTime dataConferencia) {
        this.dataConferencia = dataConferencia;
    }

    public String getFotoComprovanteUrl() {
        return fotoComprovanteUrl;
    }

    public void setFotoComprovanteUrl(String fotoComprovanteUrl) {
        this.fotoComprovanteUrl = fotoComprovanteUrl;
    }
}
