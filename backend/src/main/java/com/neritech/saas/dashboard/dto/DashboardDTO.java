package com.neritech.saas.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;

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
        List<BigDecimal> historicoFaturamento,
        List<BigDecimal> historicoServicos,
        List<String> historicoMeses,
        Long abertosMes,
        Long abertosTotal,
        Long autorizadosMes,
        Long autorizadosTotal,
        Long canceladosMes,
        Long canceladosTotal,
        Long fechadosMes,
        Long fechadosTotal,
        Long entradasVeiculosMes,
        Long saidasVeiculosMes) {
}
