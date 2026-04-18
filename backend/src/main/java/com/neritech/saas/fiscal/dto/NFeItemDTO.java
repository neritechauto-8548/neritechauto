package com.neritech.saas.fiscal.dto;

import java.math.BigDecimal;

public class NFeItemDTO {
    private String descricao;
    private BigDecimal qtde;
    private String unidade;
    private BigDecimal valorUnit;
    private BigDecimal valorTotal;
    private String cfop;

    public NFeItemDTO() {}

    public NFeItemDTO(String descricao, BigDecimal qtde, String unidade, BigDecimal valorUnit, BigDecimal valorTotal, String cfop) {
        this.descricao = descricao;
        this.qtde = qtde;
        this.unidade = unidade;
        this.valorUnit = valorUnit;
        this.valorTotal = valorTotal;
        this.cfop = cfop;
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getQtde() { return qtde; }
    public void setQtde(BigDecimal qtde) { this.qtde = qtde; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    public BigDecimal getValorUnit() { return valorUnit; }
    public void setValorUnit(BigDecimal valorUnit) { this.valorUnit = valorUnit; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public String getCfop() { return cfop; }
    public void setCfop(String cfop) { this.cfop = cfop; }
}
