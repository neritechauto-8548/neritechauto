package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fin_recebimentos_titulos")
@Getter
@Setter
public class RecebimentoTitulo extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_receber_id", nullable = false)
    private ContasReceber contaReceber;

    @Column(name = "data_recebimento", nullable = false)
    private LocalDate dataRecebimento;

    @Column(name = "valor_recebido", nullable = false)
    private BigDecimal valorRecebido;

    @Column(name = "valor_juros")
    private BigDecimal valorJuros = BigDecimal.ZERO;

    @Column(name = "valor_multa")
    private BigDecimal valorMulta = BigDecimal.ZERO;

    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forma_pagamento_id")
    private FormaPagamento formaPagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_bancaria_id")
    private ContaBancaria contaBancaria;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
}
