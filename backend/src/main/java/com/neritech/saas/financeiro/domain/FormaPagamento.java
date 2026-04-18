package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.financeiro.domain.enums.TipoFormaPagamento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fin_formas_pagamento")
@Getter
@Setter
public class FormaPagamento extends TenantEntity {

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 30)
    private TipoFormaPagamento tipo;

    @Column(name = "aceita_parcelamento")
    private Boolean aceitaParcelamento = false;

    @Column(name = "parcelas_maximas")
    private Integer parcelasMaximas = 1;

    @Column(name = "taxa_administracao")
    private BigDecimal taxaAdministracao = BigDecimal.ZERO;

    @Column(name = "prazo_recebimento_dias")
    private Integer prazoRecebimentoDias = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_bancaria_id")
    private ContaBancaria contaBancaria;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "padrao")
    private Boolean padrao = false;

    @Column(name = "exige_autorizacao")
    private Boolean exigeAutorizacao = false;

    @Column(name = "limite_diario")
    private BigDecimal limiteDiario;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    public String getNome() {
        return this.nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public TipoFormaPagamento getTipo() {
        return this.tipo;
    }
    public void setTipo(TipoFormaPagamento tipo) {
        this.tipo = tipo;
    }
    public ContaBancaria getContaBancaria() {
        return this.contaBancaria;
    }
    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }
    public BigDecimal getLimiteDiario() {
        return this.limiteDiario;
    }
    public void setLimiteDiario(BigDecimal limiteDiario) {
        this.limiteDiario = limiteDiario;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
