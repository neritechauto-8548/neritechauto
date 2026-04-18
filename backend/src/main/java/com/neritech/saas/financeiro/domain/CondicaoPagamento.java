package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.financeiro.domain.enums.TipoCondicaoPagamento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fin_condicoes_pagamento")
@Getter
@Setter
public class CondicaoPagamento extends TenantEntity {

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 30)
    private TipoCondicaoPagamento tipo;

    @Column(name = "numero_parcelas")
    private Integer numeroParcelas = 1;

    @Column(name = "intervalo_dias")
    private Integer intervaloDias = 30;

    @Column(name = "valor_entrada_percentual")
    private BigDecimal valorEntradaPercentual = BigDecimal.ZERO;

    @Column(name = "desconto_a_vista_percentual")
    private BigDecimal descontoAVistaPercentual = BigDecimal.ZERO;

    @Column(name = "juros_parcelamento_percentual")
    private BigDecimal jurosParcelamentoPercentual = BigDecimal.ZERO;

    @Column(name = "vencimento_primeira_parcela_dias")
    private Integer vencimentoPrimeiraParcelaDias = 30;

    @Column(name = "formas_pagamento_aceitas", columnDefinition = "jsonb")
    private String formasPagamentoAceitas;

    @Column(name = "padrao")
    private Boolean padrao = false;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "criado_por")
    private Long criadoPor;

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
    public TipoCondicaoPagamento getTipo() {
        return this.tipo;
    }
    public void setTipo(TipoCondicaoPagamento tipo) {
        this.tipo = tipo;
    }
    public String getFormasPagamentoAceitas() {
        return this.formasPagamentoAceitas;
    }
    public void setFormasPagamentoAceitas(String formasPagamentoAceitas) {
        this.formasPagamentoAceitas = formasPagamentoAceitas;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    public Long getCriadoPor() {
        return this.criadoPor;
    }
    public void setCriadoPor(Long criadoPor) {
        this.criadoPor = criadoPor;
    }
}
