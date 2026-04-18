package com.neritech.saas.financeiro.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FechamentoCaixaRequest {
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private BigDecimal saldoInicial;
    private BigDecimal saldoFinal;
    private BigDecimal totalEntradas;
    private BigDecimal totalSaidas;
    private String observacoes;
    private Long usuarioResponsavel;
    private String situacao;
}
