package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.financeiro.domain.enums.TipoCentroCusto;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fin_centros_custo")
@Getter
@Setter
public class CentroCusto extends TenantEntity {

    @Column(name = "codigo", nullable = false, length = 20)
    private String codigo;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_custo_pai_id")
    private CentroCusto centroCustoPai;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 20)
    private TipoCentroCusto tipo;

    @Column(name = "responsavel_id")
    private Long responsavelId;

    @Column(name = "orcamento_mensal")
    private BigDecimal orcamentoMensal;

    @Column(name = "orcamento_anual")
    private BigDecimal orcamentoAnual;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "criado_por")
    private Long criadoPor;

    public String getCodigo() {
        return this.codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getNome() {
        return this.nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescricao() {
        return this.descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public CentroCusto getCentroCustoPai() {
        return this.centroCustoPai;
    }
    public void setCentroCustoPai(CentroCusto centroCustoPai) {
        this.centroCustoPai = centroCustoPai;
    }
    public TipoCentroCusto getTipo() {
        return this.tipo;
    }
    public void setTipo(TipoCentroCusto tipo) {
        this.tipo = tipo;
    }
    public Long getResponsavelId() {
        return this.responsavelId;
    }
    public void setResponsavelId(Long responsavelId) {
        this.responsavelId = responsavelId;
    }
    public BigDecimal getOrcamentoMensal() {
        return this.orcamentoMensal;
    }
    public void setOrcamentoMensal(BigDecimal orcamentoMensal) {
        this.orcamentoMensal = orcamentoMensal;
    }
    public BigDecimal getOrcamentoAnual() {
        return this.orcamentoAnual;
    }
    public void setOrcamentoAnual(BigDecimal orcamentoAnual) {
        this.orcamentoAnual = orcamentoAnual;
    }
    public Long getCriadoPor() {
        return this.criadoPor;
    }
    public void setCriadoPor(Long criadoPor) {
        this.criadoPor = criadoPor;
    }
}
