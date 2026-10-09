package com.neritech.saas.dashboard;

import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.dashboard.dto.DashboardDTO;
import com.neritech.saas.dashboard.service.DashboardService;
import com.neritech.saas.financeiro.repository.ContasPagarRepository;
import com.neritech.saas.financeiro.repository.ContasReceberRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock ClienteRepository clienteRepository;
    @Mock OrdemServicoRepository ordemServicoRepository;
    @Mock ContasReceberRepository contasReceberRepository;
    @Mock ContasPagarRepository contasPagarRepository;

    @InjectMocks DashboardService dashboardService;

    @Test
    void shouldBuildDashboardDataForCompany() {
        DashboardDTO result = dashboardService.getDashboardData(10L);

        assertNotNull(result);
        assertEquals(0L, result.totalClientes());
        assertEquals(0L, result.osAbertas());
        assertEquals(0L, result.osEmAndamento());
        assertEquals(0L, result.osConcluidas());
        assertEquals(0L, result.osCanceladas());
        assertEquals(BigDecimal.ZERO, result.faturamentoMes());
        assertEquals(BigDecimal.ZERO, result.despesasMes());
        assertEquals(BigDecimal.ZERO, result.lucroMes());
        assertEquals(BigDecimal.ZERO, result.ticketMedio());
        assertEquals(BigDecimal.ZERO, result.contasReceber());
        assertEquals(BigDecimal.ZERO, result.contasPagar());
        assertEquals(BigDecimal.ZERO, result.valoresVencidos());
        assertEquals(0L, result.veiculosEmAtraso());
        assertEquals(6, result.historicoFaturamento().size());
        assertEquals(6, result.historicoDespesas().size());
        assertEquals(6, result.historicoMeses().size());
    }
}
