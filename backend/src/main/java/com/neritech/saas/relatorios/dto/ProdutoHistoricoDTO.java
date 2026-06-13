package com.neritech.saas.relatorios.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoHistoricoDTO {
    private BigDecimal estoque;
    private String variacao;
    private BigDecimal valorVenda;
    private BigDecimal valorCompra;
    private String observacao;
    private LocalDateTime dataAlteracao;
    private String colaborador;
}
