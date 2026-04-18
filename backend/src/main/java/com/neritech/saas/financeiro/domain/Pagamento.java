package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.financeiro.domain.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fin_pagamentos")
@Getter
@Setter
public class Pagamento extends TenantEntity {

    @Column(name = "numero_pagamento", nullable = false, length = 30)
    private String numeroPagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fatura_id")
    private Fatura fatura;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forma_pagamento_id", nullable = false)
    private FormaPagamento formaPagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condicao_pagamento_id")
    private CondicaoPagamento condicaoPagamento;

    @OneToMany(mappedBy = "pagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<ParcelaPagamento> parcelas = new java.util.ArrayList<>();

    @Column(name = "valor_pago", nullable = false)
    private BigDecimal valorPago;

    @Column(name = "valor_troco")
    private BigDecimal valorTroco = BigDecimal.ZERO;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento = LocalDateTime.now();

    @Column(name = "data_compensacao")
    private LocalDate dataCompensacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private StatusPagamento status;

    @Column(name = "numero_autorizacao", length = 100)
    private String numeroAutorizacao;

    @Column(name = "numero_transacao", length = 100)
    private String numeroTransacao;

    @Column(name = "bandeira_cartao", length = 50)
    private String bandeiraCartao;

    @Column(name = "ultimos_digitos_cartao", length = 4)
    private String ultimosDigitosCartao;

    @Column(name = "taxa_administracao")
    private BigDecimal taxaAdministracao;

    @Column(name = "valor_liquido")
    private BigDecimal valorLiquido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_bancaria_id")
    private ContaBancaria contaBancaria;

    @Column(name = "comprovante_url", length = 500)
    private String comprovanteUrl;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "processado_por", nullable = false)
    private Long processadoPor;

    @Column(name = "data_processamento")
    private LocalDateTime dataProcessamento = LocalDateTime.now();

    // Manual setters for @ManyToOne fields (Lombok workaround)
    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void setCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }
}
