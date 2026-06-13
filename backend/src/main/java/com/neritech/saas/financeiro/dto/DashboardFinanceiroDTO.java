package com.neritech.saas.financeiro.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardFinanceiroDTO {
    private BigDecimal totalAReceber;
    private BigDecimal recebidoHoje;
    private BigDecimal atrasados;
    private BigDecimal ticketMedio;
    private BigDecimal recebimentosPrevistosMes;
    private BigDecimal inadimplencia;
    private Integer contasVencendoHoje;
}
