package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.financeiro.domain.enums.TipoItemFatura;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fin_itens_fatura")
@Getter
@Setter
public class ItemFatura extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fatura_id", nullable = false)
    private Fatura fatura;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_item", length = 30)
    private TipoItemFatura tipoItem;

    @Column(name = "servico_id")
    private Long servicoId;

    @Column(name = "produto_id")
    private Long produtoId;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "quantidade", nullable = false)
    private BigDecimal quantidade;

    @Column(name = "valor_unitario", nullable = false)
    private BigDecimal valorUnitario;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "desconto_percentual")
    private BigDecimal descontoPercentual = BigDecimal.ZERO;

    @Column(name = "desconto_valor")
    private BigDecimal descontoValor = BigDecimal.ZERO;

    @Column(name = "valor_liquido", nullable = false)
    private BigDecimal valorLiquido;

    @Column(name = "ncm", length = 8)
    private String ncm;

    @Column(name = "cfop", length = 4)
    private String cfop;

    @Column(name = "cst_icms", length = 3)
    private String cstIcms;

    @Column(name = "cst_pis", length = 2)
    private String cstPis;

    @Column(name = "cst_cofins", length = 2)
    private String cstCofins;

    @Column(name = "aliquota_icms")
    private BigDecimal aliquotaIcms;

    @Column(name = "aliquota_pis")
    private BigDecimal aliquotaPis;

    @Column(name = "aliquota_cofins")
    private BigDecimal aliquotaCofins;

    @Column(name = "valor_icms")
    private BigDecimal valorIcms = BigDecimal.ZERO;

    @Column(name = "valor_pis")
    private BigDecimal valorPis = BigDecimal.ZERO;

    @Column(name = "valor_cofins")
    private BigDecimal valorCofins = BigDecimal.ZERO;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "ordem_item")
    private Integer ordemItem = 0;

    public Fatura getFatura() {
        return this.fatura;
    }
    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
    }
    public TipoItemFatura getTipoItem() {
        return this.tipoItem;
    }
    public void setTipoItem(TipoItemFatura tipoItem) {
        this.tipoItem = tipoItem;
    }
    public Long getServicoId() {
        return this.servicoId;
    }
    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }
    public Long getProdutoId() {
        return this.produtoId;
    }
    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }
    public String getDescricao() {
        return this.descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public BigDecimal getQuantidade() {
        return this.quantidade;
    }
    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }
    public BigDecimal getValorUnitario() {
        return this.valorUnitario;
    }
    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
    public BigDecimal getValorTotal() {
        return this.valorTotal;
    }
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    public BigDecimal getValorLiquido() {
        return this.valorLiquido;
    }
    public void setValorLiquido(BigDecimal valorLiquido) {
        this.valorLiquido = valorLiquido;
    }
    public String getNcm() {
        return this.ncm;
    }
    public void setNcm(String ncm) {
        this.ncm = ncm;
    }
    public String getCfop() {
        return this.cfop;
    }
    public void setCfop(String cfop) {
        this.cfop = cfop;
    }
    public String getCstIcms() {
        return this.cstIcms;
    }
    public void setCstIcms(String cstIcms) {
        this.cstIcms = cstIcms;
    }
    public String getCstPis() {
        return this.cstPis;
    }
    public void setCstPis(String cstPis) {
        this.cstPis = cstPis;
    }
    public String getCstCofins() {
        return this.cstCofins;
    }
    public void setCstCofins(String cstCofins) {
        this.cstCofins = cstCofins;
    }
    public BigDecimal getAliquotaIcms() {
        return this.aliquotaIcms;
    }
    public void setAliquotaIcms(BigDecimal aliquotaIcms) {
        this.aliquotaIcms = aliquotaIcms;
    }
    public BigDecimal getAliquotaPis() {
        return this.aliquotaPis;
    }
    public void setAliquotaPis(BigDecimal aliquotaPis) {
        this.aliquotaPis = aliquotaPis;
    }
    public BigDecimal getAliquotaCofins() {
        return this.aliquotaCofins;
    }
    public void setAliquotaCofins(BigDecimal aliquotaCofins) {
        this.aliquotaCofins = aliquotaCofins;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
