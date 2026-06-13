package com.neritech.saas.financeiro.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class RecebimentoTituloDTO {
    private Long id;
    private LocalDate dataRecebimento;
    private BigDecimal valorRecebido;
    private BigDecimal valorJuros;
    private BigDecimal valorMulta;
    private BigDecimal valorDesconto;
    private Long formaPagamentoId;
    private String formaPagamentoNome;
    private Long contaBancariaId;
    private String contaBancariaNome;
    private String observacoes;
}
