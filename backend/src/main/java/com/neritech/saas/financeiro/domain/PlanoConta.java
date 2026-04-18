package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.financeiro.domain.enums.NaturezaSaldo;
import com.neritech.saas.financeiro.domain.enums.TipoConta;
import com.neritech.saas.financeiro.domain.enums.TipoContaContabil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fin_plano_contas")
@Getter
@Setter
public class PlanoConta extends TenantEntity {

    @Column(name = "codigo", nullable = false, length = 20)
    private String codigo;

    @Column(name = "nome", nullable = false)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_pai_id")
    private PlanoConta contaPai;

    @Column(name = "nivel", nullable = false)
    private Integer nivel;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conta", length = 30)
    private TipoContaContabil tipoConta;

    @Enumerated(EnumType.STRING)
    @Column(name = "natureza_saldo", length = 20)
    private NaturezaSaldo naturezaSaldo;

    @Column(name = "aceita_lancamento")
    private Boolean aceitaLancamento = true;

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
    public PlanoConta getContaPai() {
        return this.contaPai;
    }
    public void setContaPai(PlanoConta contaPai) {
        this.contaPai = contaPai;
    }
    public Integer getNivel() {
        return this.nivel;
    }
    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }
    public TipoContaContabil getTipoConta() {
        return this.tipoConta;
    }
    public void setTipoConta(TipoContaContabil tipoConta) {
        this.tipoConta = tipoConta;
    }
    public NaturezaSaldo getNaturezaSaldo() {
        return this.naturezaSaldo;
    }
    public void setNaturezaSaldo(NaturezaSaldo naturezaSaldo) {
        this.naturezaSaldo = naturezaSaldo;
    }
    public Long getCriadoPor() {
        return this.criadoPor;
    }
    public void setCriadoPor(Long criadoPor) {
        this.criadoPor = criadoPor;
    }
}
