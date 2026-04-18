package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.financeiro.domain.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fin_fluxo_caixa")
@Getter
@Setter
public class FluxoCaixa extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_bancaria_id")
    private ContaBancaria contaBancaria;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimentacao", length = 30)
    private TipoMovimentacao tipoMovimentacao;

    @Column(name = "categoria", nullable = false, length = 100)
    private String categoria;

    @Column(name = "subcategoria", length = 100)
    private String subcategoria;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "data_movimentacao", nullable = false)
    private LocalDate dataMovimentacao;

    @Column(name = "data_competencia")
    private LocalDate dataCompetencia;

    @Column(name = "documento_tipo", length = 50)
    private String documentoTipo;

    @Column(name = "documento_numero", length = 50)
    private String documentoNumero;

    @Column(name = "documento_id")
    private Long documentoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_custo_id")
    private CentroCusto centroCusto;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "fornecedor_id")
    private Long fornecedorId;

    @Column(name = "funcionario_id")
    private Long funcionarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forma_pagamento_id")
    private FormaPagamento formaPagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_destino_id")
    private ContaBancaria contaDestino;

    @Column(name = "conciliado")
    private Boolean conciliado = false;

    @Column(name = "data_conciliacao")
    private LocalDateTime dataConciliacao;

    @Column(name = "saldo_anterior")
    private BigDecimal saldoAnterior;

    @Column(name = "saldo_atual")
    private BigDecimal saldoAtual;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "anexos", columnDefinition = "jsonb")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    private String anexos;

    @Column(name = "usuario_responsavel", nullable = false)
    private Long usuarioResponsavel;

    // Manual setters for @ManyToOne and specific fields (Lombok workaround)
    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public void setCentroCusto(CentroCusto centroCusto) {
        this.centroCusto = centroCusto;
    }

    public void setDocumentoId(Long documentoId) {
        this.documentoId = documentoId;
    }
}
