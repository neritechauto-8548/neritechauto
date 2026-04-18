package com.neritech.saas.dashboard.dto;

import java.math.BigDecimal;

public record DashboardDTO(
        Long totalClientes,
        Long osAbertas,
        Long osEmAndamento,
        BigDecimal faturamentoMes,
        BigDecimal despesasMes,
        BigDecimal lucroMes) {
}
