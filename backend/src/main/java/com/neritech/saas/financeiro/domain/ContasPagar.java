package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.financeiro.domain.enums.StatusTitulo;
import com.neritech.saas.financeiro.domain.enums.TipoTitulo;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "fin_contas_pagar")
@Getter
@Setter
public class ContasPagar extends TenantEntity {

    @Column(name = "numero_titulo", length = 30)
    private String numeroTitulo;

    @Column(name = "fornecedor_id")
    private Long fornecedorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_titulo", length = 30)
    private TipoTitulo tipoTitulo;

    @Column(name = "categoria_despesa", length = 100)
    private String categoriaDespesa;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "numero_documento", length = 50)
    private String numeroDocumento;

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

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forma_pagamento_id")
    private FormaPagamento formaPagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_bancaria_id")
    private ContaBancaria contaBancaria;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private StatusTitulo status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_custo_id")
    private CentroCusto centroCusto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_contas_id")
    private PlanoConta planoContas;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rateio_centros_custo")
    private String rateioCentrosCusto;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "anexos_comprobatorios")
    private String anexosComprobatorios;

    @Column(name = "aprovado_por")
    private Long aprovadoPor;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "pago_por")
    private Long pagoPor;

    // Manual setters for @ManyToOne fields (Lombok workaround)
    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public void setCentroCusto(CentroCusto centroCusto) {
        this.centroCusto = centroCusto;
    }

    public void setPlanoContas(PlanoConta planoContas) {
        this.planoContas = planoContas;
    }
}
