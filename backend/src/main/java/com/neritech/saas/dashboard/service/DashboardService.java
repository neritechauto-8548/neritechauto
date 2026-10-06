package com.neritech.saas.dashboard.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public DashboardDTO getDashboardData(Long empresaId, String period, String comparison, LocalDate requestedStart, LocalDate requestedEnd) {
        PeriodRange current = resolvePeriod(period, requestedStart, requestedEnd);
        PeriodRange compare = resolveComparison(current, comparison);
        long totalClientes = clienteRepository.countByStatus(com.neritech.saas.cliente.domain.enums.StatusCliente.ATIVO);
        LocalDateTime start = current.start().atStartOfDay();
        LocalDateTime end = current.end().atTime(23, 59, 59, 999_999_999);

        long osAbertas = ordemServicoRepository.countAtivas(empresaId);
        long osEmAndamento = ordemServicoRepository.countAtivasAndPeriod(empresaId, start, end);
        long osConcluidas = ordemServicoRepository.countByFinalizaOSAndPeriod(empresaId, true, start, end);
        long osCanceladas = ordemServicoRepository.countByCancelaOSAndPeriod(empresaId, true, start, end);

        BigDecimal faturamento = safe(contasReceberRepository.calculateFaturamentoMes(empresaId, current.start(), current.end()));
        BigDecimal despesas = safe(contasPagarRepository.calculateDespesasMes(empresaId, current.start(), current.end()));
        BigDecimal lucro = faturamento.subtract(despesas);
        BigDecimal ticketMedio = safe(ordemServicoRepository.calculateTicketMedioPeriod(empresaId, start, end));
        BigDecimal contasReceber = safe(contasReceberRepository.calculateTotalPendentes(empresaId));
        BigDecimal contasPagar = safe(contasPagarRepository.calculateTotalPendentes(empresaId));
        BigDecimal valoresVencidos = safe(contasReceberRepository.calculateTotalVencidos(empresaId, LocalDate.now()));
        long ordensEmAtraso = ordemServicoRepository.countAtrasadas(empresaId);

        List<String> abertos = List.of("ABERTA", "DIAGNOSTICO", "AGUARDANDO_APROVACAO");
        List<String> autorizados = List.of("APROVADA", "EM_EXECUCAO", "AGUARDANDO_PECAS");
        long abertosMes = ordemServicoRepository.countByStatusCodesAndPeriod(empresaId, abertos, start, end);
        long abertosTotal = ordemServicoRepository.countByStatusCodes(empresaId, abertos);
        long autorizadosMes = ordemServicoRepository.countByStatusCodesAndPeriod(empresaId, autorizados, start, end);
        long autorizadosTotal = ordemServicoRepository.countByStatusCodes(empresaId, autorizados);
        long canceladosMes = ordemServicoRepository.countByCancelaOSAndPeriod(empresaId, true, start, end);
        long canceladosTotal = ordemServicoRepository.countByCancelaOS(empresaId, true);
        long fechadosMes = ordemServicoRepository.countByFinalizaOSAndPeriod(empresaId, true, start, end);
        long fechadosTotal = ordemServicoRepository.countByFinalizaOS(empresaId, true);
        long entradas = ordemServicoRepository.countByPeriod(empresaId, start, end);
        long saidas = ordemServicoRepository.countSaidasByPeriod(empresaId, start, end);

        List<BigDecimal> historicoFaturamento = new ArrayList<>();
        List<BigDecimal> historicoServicos = new ArrayList<>();
        List<String> historicoMeses = new ArrayList<>();
        LocalDate firstMonth = LocalDate.now().withDayOfMonth(1).minusMonths(5);
        for (int i = 0; i < 6; i++) {
            LocalDate monthStart = firstMonth.plusMonths(i);
            LocalDate monthEnd = monthStart.with(TemporalAdjusters.lastDayOfMonth());
            historicoFaturamento.add(safe(contasReceberRepository.calculateFaturamentoMes(empresaId, monthStart, monthEnd)));
            historicoServicos.add(safe(ordemServicoRepository.calculateServicosPeriod(empresaId, monthStart.atStartOfDay(), monthEnd.atTime(23, 59, 59, 999_999_999))));
            historicoMeses.add(formatMonth(monthStart));
        }

        BigDecimal faturamentoComparacao = safe(contasReceberRepository.calculateFaturamentoMes(empresaId, compare.start(), compare.end()));
        long osConcluidasComparacao = ordemServicoRepository.countByFinalizaOSAndPeriod(empresaId, true, compare.start().atStartOfDay(), compare.end().atTime(23, 59, 59, 999_999_999));
        BigDecimal ticketMedioComparacao = safe(ordemServicoRepository.calculateTicketMedioPeriod(empresaId, compare.start().atStartOfDay(), compare.end().atTime(23, 59, 59, 999_999_999)));

        return new DashboardDTO(Math.toIntExact(totalClientes), Math.toIntExact(osAbertas), osEmAndamento, Math.toIntExact(osConcluidas), Math.toIntExact(osCanceladas),
                faturamento, despesas, lucro, ticketMedio, contasReceber, contasPagar, valoresVencidos, Math.toIntExact(ordensEmAtraso),
                historicoFaturamento, historicoServicos, historicoMeses, Math.toIntExact(abertosMes), Math.toIntExact(abertosTotal), Math.toIntExact(autorizadosMes), Math.toIntExact(autorizadosTotal),
                Math.toIntExact(canceladosMes), Math.toIntExact(canceladosTotal), Math.toIntExact(fechadosMes), Math.toIntExact(fechadosTotal), Math.toIntExact(entradas), Math.toIntExact(saidas),
                period, current.start(), current.end(), comparison, faturamentoComparacao, osConcluidasComparacao,
                ticketMedioComparacao, true, LocalDateTime.now(), false,
                totalClientes > 0 || osAbertas > 0 || osConcluidas > 0 || osCanceladas > 0
                        || faturamento.signum() > 0 || despesas.signum() > 0 || contasReceber.signum() > 0 || contasPagar.signum() > 0);
    }

    private PeriodRange resolvePeriod(String period, LocalDate requestedStart, LocalDate requestedEnd) {
        LocalDate today = LocalDate.now();
        String normalized = StringUtils.hasText(period) ? period.toLowerCase() : "month";
        return switch (normalized) {
            case "today" -> new PeriodRange(today, today);
            case "7d" -> new PeriodRange(today.minusDays(6), today);
            case "30d" -> new PeriodRange(today.minusDays(29), today);
            case "custom" -> {
                if (requestedStart == null || requestedEnd == null || requestedEnd.isBefore(requestedStart))
                    throw new IllegalArgumentException("Para o período personalizado, informe startDate e endDate válidos.");
                yield new PeriodRange(requestedStart, requestedEnd);
            }
            case "month" -> new PeriodRange(today.with(TemporalAdjusters.firstDayOfMonth()), today.with(TemporalAdjusters.lastDayOfMonth()));
            default -> throw new IllegalArgumentException("Período inválido: " + period);
        };
    }

    private PeriodRange resolveComparison(PeriodRange current, String comparison) {
        String normalized = StringUtils.hasText(comparison) ? comparison.toLowerCase() : "previous";
        long days = ChronoUnit.DAYS.between(current.start(), current.end()) + 1;
        if ("same".equals(normalized)) return new PeriodRange(current.start().minusYears(1), current.end().minusYears(1));
        if ("previous".equals(normalized)) return new PeriodRange(current.start().minusDays(days), current.end().minusDays(days));
        throw new IllegalArgumentException("Comparação inválida: " + comparison);
    }

    private String formatMonth(LocalDate date) {
        return switch (date.getMonthValue()) {
            case 1 -> "Jan"; case 2 -> "Fev"; case 3 -> "Mar"; case 4 -> "Abr"; case 5 -> "Mai"; case 6 -> "Jun";
            case 7 -> "Jul"; case 8 -> "Ago"; case 9 -> "Set"; case 10 -> "Out"; case 11 -> "Nov"; default -> "Dez";
        };
    }

    private BigDecimal safe(BigDecimal value) { return value == null ? BigDecimal.ZERO : value; }
    private record PeriodRange(LocalDate start, LocalDate end) {}
}
