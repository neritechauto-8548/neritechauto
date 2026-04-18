package com.neritech.saas.estoque.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.estoque.domain.enums.AcaoDevolucao;
import com.neritech.saas.estoque.domain.enums.CondicaoProduto;
import com.neritech.saas.estoque.domain.enums.MotivoDevolucao;
import com.neritech.saas.produtoServico.domain.Produto;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "devolucoes_produtos")
public class DevolucaoProduto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "ordem_servico_id")
    private Long ordemServicoId;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "quantidade_devolvida", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeDevolvida;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_devolucao", length = 30)
    private MotivoDevolucao motivoDevolucao;

    @Column(name = "descricao_motivo", columnDefinition = "TEXT")
    private String descricaoMotivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "condicao_produto", length = 20)
    private CondicaoProduto condicaoProduto;

    @Enumerated(EnumType.STRING)
    @Column(name = "acao_tomada", length = 30)
    private AcaoDevolucao acaoTomada;

    @Column(name = "valor_devolvido", precision = 10, scale = 2)
    private BigDecimal valorDevolvido;

    @Column(name = "aprovado_por")
    private Long aprovadoPor;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "fotos_produto_url", columnDefinition = "jsonb")
    private String fotosProdutoUrl;

    @Column(name = "data_devolucao")
    private LocalDateTime dataDevolucao;

    @Column(name = "usuario_responsavel")
    private Long usuarioResponsavel;

    // Getters and Setters
    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Long getOrdemServicoId() {
        return ordemServicoId;
    }

    public void setOrdemServicoId(Long ordemServicoId) {
        this.ordemServicoId = ordemServicoId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public BigDecimal getQuantidadeDevolvida() {
        return quantidadeDevolvida;
    }

    public void setQuantidadeDevolvida(BigDecimal quantidadeDevolvida) {
        this.quantidadeDevolvida = quantidadeDevolvida;
    }

    public MotivoDevolucao getMotivoDevolucao() {
        return motivoDevolucao;
    }

    public void setMotivoDevolucao(MotivoDevolucao motivoDevolucao) {
        this.motivoDevolucao = motivoDevolucao;
    }

    public String getDescricaoMotivo() {
        return descricaoMotivo;
    }

    public void setDescricaoMotivo(String descricaoMotivo) {
        this.descricaoMotivo = descricaoMotivo;
    }

    public CondicaoProduto getCondicaoProduto() {
        return condicaoProduto;
    }

    public void setCondicaoProduto(CondicaoProduto condicaoProduto) {
        this.condicaoProduto = condicaoProduto;
    }

    public AcaoDevolucao getAcaoTomada() {
        return acaoTomada;
    }

    public void setAcaoTomada(AcaoDevolucao acaoTomada) {
        this.acaoTomada = acaoTomada;
    }

    public BigDecimal getValorDevolvido() {
        return valorDevolvido;
    }

    public void setValorDevolvido(BigDecimal valorDevolvido) {
        this.valorDevolvido = valorDevolvido;
    }

    public Long getAprovadoPor() {
        return aprovadoPor;
    }

    public void setAprovadoPor(Long aprovadoPor) {
        this.aprovadoPor = aprovadoPor;
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

    public String getFotosProdutoUrl() {
        return fotosProdutoUrl;
    }

    public void setFotosProdutoUrl(String fotosProdutoUrl) {
        this.fotosProdutoUrl = fotosProdutoUrl;
    }

    public LocalDateTime getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDateTime dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Long getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(Long usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }
}
