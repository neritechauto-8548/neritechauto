package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fin_fechamento_caixa")
@Getter
@Setter
public class FechamentoCaixa extends TenantEntity {

    @Column(name = "situacao", nullable = false, length = 20)
    private String situacao; // ABERTO, FECHADO

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;

    @Column(name = "saldo_inicial", nullable = false)
    private BigDecimal saldoInicial;

    @Column(name = "saldo_final")
    private BigDecimal saldoFinal;

    @Column(name = "total_entradas")
    private BigDecimal totalEntradas;

    @Column(name = "total_saidas")
    private BigDecimal totalSaidas;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "usuario_responsavel", nullable = false)
    private Long usuarioResponsavel;

}
