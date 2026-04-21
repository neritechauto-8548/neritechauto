package com.neritech.saas.dashboard.dto;

import java.math.BigDecimal;

public record DashboardDTO(
        Long totalClientes,
        Long osAbertas,
        Long osEmAndamento,
        Long osConcluidas,
        Long osCanceladas,
        BigDecimal faturamentoMes,
        BigDecimal despesasMes,
        BigDecimal lucroMes,
        BigDecimal ticketMedio,
        BigDecimal contasReceber,
        BigDecimal contasPagar,
        BigDecimal valoresVencidos,
        Long veiculosEmAtraso,
        java.util.List<java.math.BigDecimal> historicoFaturamento,
        java.util.List<java.math.BigDecimal> historicoServicos,
        java.util.List<String> historicoMeses) {
}
