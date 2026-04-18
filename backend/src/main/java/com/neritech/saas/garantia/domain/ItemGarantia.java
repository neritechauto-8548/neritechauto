package com.neritech.saas.garantia.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * Entidade que representa os itens cobertos por uma garantia
 */
@Entity
@Table(name = "gar_itens_garantia")
@Getter
@Setter
public class ItemGarantia extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garantia_id", nullable = false)
    private Garantia garantia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_item", nullable = false, length = 30)
    private TipoItemGarantia tipoItem;

    @Column(name = "servico_id")
    private Long servicoId;

    @Column(name = "produto_id")
    private Long produtoId;

    @Column(name = "descricao_item", columnDefinition = "TEXT", nullable = false)
    private String descricaoItem;

    @Column(name = "quantidade_original", precision = 10, scale = 2, nullable = false)
    private BigDecimal quantidadeOriginal;

    @Column(name = "valor_unitario_original", precision = 10, scale = 4, nullable = false)
    private BigDecimal valorUnitarioOriginal;

    @Column(name = "valor_total_original", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotalOriginal;

    @Column(name = "percentual_cobertura", precision = 5, scale = 2)
    private BigDecimal percentualCobertura = BigDecimal.valueOf(100.00);

    @Column(name = "valor_cobertura", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorCobertura;

    @Column(name = "condicoes_especificas", columnDefinition = "TEXT")
    private String condicoesEspecificas;

    @Column(name = "defeito_coberto", columnDefinition = "TEXT")
    private String defeitoCoberto;

    @Column(name = "defeito_nao_coberto", columnDefinition = "TEXT")
    private String defeitoNaoCoberto;

    @Column(name = "prazo_acionamento_dias")
    private Integer prazoAcionamentoDias;

    @Column(name = "quantidade_utilizada", precision = 10, scale = 2)
    private BigDecimal quantidadeUtilizada = BigDecimal.ZERO;

    @Column(name = "valor_utilizado", precision = 10, scale = 2)
    private BigDecimal valorUtilizado = BigDecimal.ZERO;

    @Column(name = "saldo_disponivel", precision = 10, scale = 2)
    private BigDecimal saldoDisponivel;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "ativo")
    private Boolean ativo = true;

    public Garantia getGarantia() {
        return this.garantia;
    }
    public void setGarantia(Garantia garantia) {
        this.garantia = garantia;
    }
    public TipoItemGarantia getTipoItem() {
        return this.tipoItem;
    }
    public void setTipoItem(TipoItemGarantia tipoItem) {
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
    public String getDescricaoItem() {
        return this.descricaoItem;
    }
    public void setDescricaoItem(String descricaoItem) {
        this.descricaoItem = descricaoItem;
    }
    public BigDecimal getQuantidadeOriginal() {
        return this.quantidadeOriginal;
    }
    public void setQuantidadeOriginal(BigDecimal quantidadeOriginal) {
        this.quantidadeOriginal = quantidadeOriginal;
    }
    public BigDecimal getValorUnitarioOriginal() {
        return this.valorUnitarioOriginal;
    }
    public void setValorUnitarioOriginal(BigDecimal valorUnitarioOriginal) {
        this.valorUnitarioOriginal = valorUnitarioOriginal;
    }
    public BigDecimal getValorTotalOriginal() {
        return this.valorTotalOriginal;
    }
    public void setValorTotalOriginal(BigDecimal valorTotalOriginal) {
        this.valorTotalOriginal = valorTotalOriginal;
    }
    public BigDecimal getValorCobertura() {
        return this.valorCobertura;
    }
    public void setValorCobertura(BigDecimal valorCobertura) {
        this.valorCobertura = valorCobertura;
    }
    public String getCondicoesEspecificas() {
        return this.condicoesEspecificas;
    }
    public void setCondicoesEspecificas(String condicoesEspecificas) {
        this.condicoesEspecificas = condicoesEspecificas;
    }
    public String getDefeitoCoberto() {
        return this.defeitoCoberto;
    }
    public void setDefeitoCoberto(String defeitoCoberto) {
        this.defeitoCoberto = defeitoCoberto;
    }
    public String getDefeitoNaoCoberto() {
        return this.defeitoNaoCoberto;
    }
    public void setDefeitoNaoCoberto(String defeitoNaoCoberto) {
        this.defeitoNaoCoberto = defeitoNaoCoberto;
    }
    public Integer getPrazoAcionamentoDias() {
        return this.prazoAcionamentoDias;
    }
    public void setPrazoAcionamentoDias(Integer prazoAcionamentoDias) {
        this.prazoAcionamentoDias = prazoAcionamentoDias;
    }
    public BigDecimal getSaldoDisponivel() {
        return this.saldoDisponivel;
    }
    public void setSaldoDisponivel(BigDecimal saldoDisponivel) {
        this.saldoDisponivel = saldoDisponivel;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
