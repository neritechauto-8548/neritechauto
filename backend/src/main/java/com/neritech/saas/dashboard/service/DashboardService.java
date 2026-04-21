package com.neritech.saas.dashboard.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.stereotype.Service;

import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.dashboard.dto.DashboardDTO;
import com.neritech.saas.financeiro.repository.ContasPagarRepository;
import com.neritech.saas.financeiro.repository.ContasReceberRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ClienteRepository clienteRepository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final ContasReceberRepository contasReceberRepository;
    private final ContasPagarRepository contasPagarRepository;

    public DashboardDTO getDashboardData(Long empresaId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());

        // Clientes Ativos
        Long totalClientes = clienteRepository.countByStatus(com.neritech.saas.cliente.domain.enums.StatusCliente.ATIVO);

        // Ordens de Serviço
        long osAbertas = ordemServicoRepository.countAtivas(empresaId);
        long osConcluidas = ordemServicoRepository.countConcluidas(empresaId);
        long osCanceladas = ordemServicoRepository.countCanceladas(empresaId);
        long osEmAndamento = osAbertas; // No momento tratamos ativas como em andamento/abertas

        // Financeiro - Faturamento e Despesas
        BigDecimal faturamentoMes = contasReceberRepository.calculateFaturamentoMes(empresaId, startOfMonth, endOfMonth);
        if (faturamentoMes == null) faturamentoMes = BigDecimal.ZERO;

        BigDecimal despesasMes = contasPagarRepository.calculateDespesasMes(empresaId, startOfMonth, endOfMonth);
        if (despesasMes == null) despesasMes = BigDecimal.ZERO;

        BigDecimal lucroMes = faturamentoMes.subtract(despesasMes);

        // Métricas Pro
        BigDecimal ticketMedio = ordemServicoRepository.calculateTicketMedio(empresaId);
        if (ticketMedio == null) ticketMedio = BigDecimal.ZERO;

        BigDecimal contasReceber = contasReceberRepository.calculateTotalPendentes(empresaId);
        if (contasReceber == null) contasReceber = BigDecimal.ZERO;

        BigDecimal contasPagar = contasPagarRepository.calculateTotalPendentes(empresaId);
        if (contasPagar == null) contasPagar = BigDecimal.ZERO;

        BigDecimal valoresVencidos = contasReceberRepository.calculateTotalVencidos(empresaId, now);
        if (valoresVencidos == null) valoresVencidos = BigDecimal.ZERO;

        long veiculosEmAtraso = ordemServicoRepository.countAtrasadas(empresaId);

        // Dados do Gráfico (Mock estruturado para evoluir para real)
        List<BigDecimal> historicoFaturamento = List.of(
                new BigDecimal("2800"), new BigDecimal("2900"), new BigDecimal("3300"),
                new BigDecimal("3600"), new BigDecimal("4200"), faturamentoMes);
        List<BigDecimal> historicoServicos = List.of(
                new BigDecimal("4500"), new BigDecimal("5200"), new BigDecimal("5300"),
                new BigDecimal("4900"), new BigDecimal("6200"), faturamentoMes.multiply(new BigDecimal("0.6")));
        List<String> historicoMeses = List.of("Jan", "Fev", "Mar", "Abr", "Mai", "Jun");

        return new DashboardDTO(
                totalClientes != null ? totalClientes : 0L,
                osAbertas,
                osEmAndamento,
                osConcluidas,
                osCanceladas,
                faturamentoMes,
                despesasMes,
                lucroMes,
                ticketMedio,
                contasReceber,
                contasPagar,
                valoresVencidos,
                veiculosEmAtraso,
                historicoFaturamento,
                historicoServicos,
                historicoMeses);
    }
}
