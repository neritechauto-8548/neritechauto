package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.financeiro.domain.enums.StatusPagamento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fin_parcelas_pagamento")
@Getter
@Setter
public class ParcelaPagamento extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagamento_id", nullable = false)
    private Pagamento pagamento;

    @Column(name = "numero_parcela", nullable = false)
    private Integer numeroParcela;

    @Column(name = "valor_parcela", nullable = false)
    private BigDecimal valorParcela;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "valor_pago")
    private BigDecimal valorPago;

    @Column(name = "juros_aplicados")
    private BigDecimal jurosAplicados = BigDecimal.ZERO;

    @Column(name = "multa_aplicada")
    private BigDecimal multaAplicada = BigDecimal.ZERO;

    @Column(name = "desconto_aplicado")
    private BigDecimal descontoAplicado = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private StatusPagamento status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forma_pagamento_id")
    private FormaPagamento formaPagamento;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    // Manual setter for @ManyToOne field (Lombok workaround)
    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}
