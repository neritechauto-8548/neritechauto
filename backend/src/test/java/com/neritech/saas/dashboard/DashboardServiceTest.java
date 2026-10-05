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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock ClienteRepository clienteRepository;
    @Mock OrdemServicoRepository ordemServicoRepository;
    @Mock ContasReceberRepository contasReceberRepository;
    @Mock ContasPagarRepository contasPagarRepository;

    @InjectMocks DashboardService dashboardService;

    @Test
    void shouldResolveCustomPeriodAndExposeEmptyDataState() {
        DashboardDTO result = dashboardService.getDashboardData(
                10L,
                "custom",
                "previous",
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 30));

        assertEquals("custom", result.periodo());
        assertEquals(LocalDate.of(2026, 9, 1), result.inicio());
        assertEquals(LocalDate.of(2026, 9, 30), result.fim());
        assertEquals("previous", result.comparacao());
        assertFalse(result.dadosDisponiveis());
        assertFalse(result.dadosParciais());
        assertTrue(result.comparacaoDisponivel());
    }

    @Test
    void shouldRejectInvalidCustomPeriod() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dashboardService.getDashboardData(
                        10L,
                        "custom",
                        "previous",
                        LocalDate.of(2026, 10, 10),
                        LocalDate.of(2026, 10, 1)));
    }

    @Test
    void shouldRejectUnknownPeriod() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dashboardService.getDashboardData(
                        10L,
                        "quarter",
                        "previous",
                        null,
                        null));
    }
}
