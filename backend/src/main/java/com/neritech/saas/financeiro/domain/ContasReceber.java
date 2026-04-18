package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.financeiro.domain.enums.StatusTitulo;
import com.neritech.saas.financeiro.domain.enums.TipoTitulo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fin_contas_receber")
@Getter
@Setter
public class ContasReceber extends TenantEntity {

    @Column(name = "numero_titulo", length = 30)
    private String numeroTitulo;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fatura_id")
    private Fatura fatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forma_pagamento_id")
    private FormaPagamento formaPagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_bancaria_id")
    private ContaBancaria contaBancaria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_custo_id")
    private CentroCusto centroCusto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_contas_id")
    private PlanoConta planoContas;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_titulo", length = 30)
    private TipoTitulo tipoTitulo;

    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "valor_nominal", nullable = false)
    private BigDecimal valorNominal;

    @Column(name = "valor_juros")
    private BigDecimal valorJuros = BigDecimal.ZERO;

    @Column(name = "valor_multa")
    private BigDecimal valorMulta = BigDecimal.ZERO;

    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto = BigDecimal.ZERO;

    @Column(name = "valor_pago")
    private BigDecimal valorPago = BigDecimal.ZERO;

    @Column(name = "valor_pendente")
    private BigDecimal valorPendente;

    @Column(name = "percentual_juros_dia")
    private BigDecimal percentualJurosDia = BigDecimal.ZERO;

    @Column(name = "percentual_multa")
    private BigDecimal percentualMulta = BigDecimal.ZERO;

    @Column(name = "dias_carencia_juros")
    private Integer diasCarenciaJuros = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private StatusTitulo status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "historico_negociacao")
    private String historicoNegociacao;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    // Manual setter for @ManyToOne field (Lombok workaround)
    public void setPlanoContas(PlanoConta planoContas) {
        this.planoContas = planoContas;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public void setCentroCusto(CentroCusto centroCusto) {
        this.centroCusto = centroCusto;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}
