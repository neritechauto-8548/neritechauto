package com.neritech.saas.estoque.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.estoque.domain.enums.StatusEstoque;
import com.neritech.saas.produtoServico.domain.Fornecedor;
import com.neritech.saas.produtoServico.domain.Produto;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "estoques")
public class Estoque extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "quantidade_atual", precision = 10, scale = 2)
    private BigDecimal quantidadeAtual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @Column(name = "preco_custo_lote", precision = 10, scale = 4)
    private BigDecimal precoCustoLote;

    @Column(name = "nota_fiscal_numero", length = 50)
    private String notaFiscalNumero;

    @Column(name = "certificado_qualidade_url", length = 500)
    private String certificadoQualidadeUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusEstoque status = StatusEstoque.ATIVO;

    @Column(name = "motivo_bloqueio", columnDefinition = "TEXT")
    private String motivoBloqueio;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "usuario_cadastro")
    private Long usuarioCadastro;

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(BigDecimal quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public BigDecimal getPrecoCustoLote() {
        return precoCustoLote;
    }

    public void setPrecoCustoLote(BigDecimal precoCustoLote) {
        this.precoCustoLote = precoCustoLote;
    }

    public String getNotaFiscalNumero() {
        return notaFiscalNumero;
    }

    public void setNotaFiscalNumero(String notaFiscalNumero) {
        this.notaFiscalNumero = notaFiscalNumero;
    }

    public String getCertificadoQualidadeUrl() {
        return certificadoQualidadeUrl;
    }

    public void setCertificadoQualidadeUrl(String certificadoQualidadeUrl) {
        this.certificadoQualidadeUrl = certificadoQualidadeUrl;
    }

    public StatusEstoque getStatus() {
        return status;
    }

    public void setStatus(StatusEstoque status) {
        this.status = status;
    }

    public String getMotivoBloqueio() {
        return motivoBloqueio;
    }

    public void setMotivoBloqueio(String motivoBloqueio) {
        this.motivoBloqueio = motivoBloqueio;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getUsuarioCadastro() {
        return usuarioCadastro;
    }

    public void setUsuarioCadastro(Long usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }
}
