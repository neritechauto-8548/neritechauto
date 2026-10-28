package com.neritech.saas.dashboard.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
        return getDashboardData(empresaId, now.with(TemporalAdjusters.firstDayOfMonth()), now.with(TemporalAdjusters.lastDayOfMonth()));
    }

    public DashboardDTO getDashboardData(Long empresaId, LocalDate startOfMonth, LocalDate endOfMonth) {
        LocalDate now = LocalDate.now();
        long periodDays = ChronoUnit.DAYS.between(startOfMonth, endOfMonth) + 1;
        LocalDate previousEnd = startOfMonth.minusDays(1);
        LocalDate previousStart = previousEnd.minusDays(periodDays - 1);

        Long totalClientes = clienteRepository.countByStatusAndEmpresaId(
                com.neritech.saas.cliente.domain.enums.StatusCliente.ATIVO, empresaId);

        long osAbertas = ordemServicoRepository.countAtivas(empresaId);
        long osConcluidas = ordemServicoRepository.countConcluidas(empresaId);
        long osCanceladas = ordemServicoRepository.countCanceladas(empresaId);
        long osEmAndamento = osAbertas;

        BigDecimal faturamentoMes = zeroIfNull(
                contasReceberRepository.calculateFaturamentoMes(empresaId, startOfMonth, endOfMonth));
        BigDecimal despesasMes = zeroIfNull(
                contasPagarRepository.calculateDespesasMes(empresaId, startOfMonth, endOfMonth));
        BigDecimal lucroMes = faturamentoMes.subtract(despesasMes);
        BigDecimal faturamentoPeriodoAnterior = zeroIfNull(
                contasReceberRepository.calculateFaturamentoMes(empresaId, previousStart, previousEnd));
        BigDecimal despesasPeriodoAnterior = zeroIfNull(
                contasPagarRepository.calculateDespesasMes(empresaId, previousStart, previousEnd));
        BigDecimal lucroPeriodoAnterior = faturamentoPeriodoAnterior.subtract(despesasPeriodoAnterior);

        BigDecimal ticketMedio = zeroIfNull(ordemServicoRepository.calculateTicketMedio(empresaId));
        BigDecimal contasReceber = zeroIfNull(contasReceberRepository.calculateTotalPendentes(empresaId));
        BigDecimal contasPagar = zeroIfNull(contasPagarRepository.calculateTotalPendentes(empresaId));
        BigDecimal valoresVencidos = zeroIfNull(
                contasReceberRepository.calculateTotalVencidos(empresaId, now));

        long veiculosEmAtraso = ordemServicoRepository.countAtrasadas(empresaId);

        var startOfMonthDateTime = startOfMonth.atStartOfDay();
        var endOfMonthDateTime = endOfMonth.atTime(23, 59, 59, 999999999);

        List<String> codigosAberto = List.of("ABERTA", "DIAGNOSTICO", "AGUARDANDO_APROVACAO");
        List<String> codigosAutorizado = List.of("APROVADA", "EM_EXECUCAO", "AGUARDANDO_PECAS");

        long abertosMesVal = ordemServicoRepository.countByStatusCodesAndPeriod(
                empresaId, codigosAberto, startOfMonthDateTime, endOfMonthDateTime);
        long abertosTotalVal = ordemServicoRepository.countByStatusCodes(empresaId, codigosAberto);

        long autorizadosMesVal = ordemServicoRepository.countByStatusCodesAndPeriod(
                empresaId, codigosAutorizado, startOfMonthDateTime, endOfMonthDateTime);
        long autorizadosTotalVal = ordemServicoRepository.countByStatusCodes(empresaId, codigosAutorizado);

        long canceladosMesVal = ordemServicoRepository.countByCancelaOSAndPeriod(
                empresaId, true, startOfMonthDateTime, endOfMonthDateTime);
        long canceladosTotalVal = ordemServicoRepository.countByCancelaOS(empresaId, true);

        long fechadosMesVal = ordemServicoRepository.countByFinalizaOSAndPeriod(
                empresaId, true, startOfMonthDateTime, endOfMonthDateTime);
        var previousStartDateTime = previousStart.atStartOfDay();
        var previousEndDateTime = previousEnd.atTime(23, 59, 59, 999999999);
        long abertosPeriodoAnterior = ordemServicoRepository.countByStatusCodesAndPeriod(
                empresaId, codigosAberto, previousStartDateTime, previousEndDateTime);
        long autorizadosPeriodoAnterior = ordemServicoRepository.countByStatusCodesAndPeriod(
                empresaId, codigosAutorizado, previousStartDateTime, previousEndDateTime);
        long canceladosPeriodoAnterior = ordemServicoRepository.countByCancelaOSAndPeriod(
                empresaId, true, previousStartDateTime, previousEndDateTime);
        long fechadosPeriodoAnterior = ordemServicoRepository.countByFinalizaOSAndPeriod(
                empresaId, true, previousStartDateTime, previousEndDateTime);
        long fechadosTotalVal = ordemServicoRepository.countByFinalizaOS(empresaId, true);

        long entradasVeiculosMesVal = ordemServicoRepository.countByPeriod(
                empresaId, startOfMonthDateTime, endOfMonthDateTime);
        long saidasVeiculosMesVal = ordemServicoRepository.countSaidasByPeriod(
                empresaId, startOfMonthDateTime, endOfMonthDateTime);

        // Histórico real dos últimos seis meses: faturamento recebido x despesas pagas.
        List<BigDecimal> historicoFaturamento = new ArrayList<>();
        List<BigDecimal> historicoDespesas = new ArrayList<>();
        List<String> historicoMeses = new ArrayList<>();

        YearMonth firstMonth = YearMonth.from(startOfMonth);
        YearMonth lastMonth = YearMonth.from(endOfMonth);
        for (YearMonth month = firstMonth; !month.isAfter(lastMonth); month = month.plusMonths(1)) {
            LocalDate inicio = month.atDay(1).isBefore(startOfMonth) ? startOfMonth : month.atDay(1);
            LocalDate fim = month.atEndOfMonth().isAfter(endOfMonth) ? endOfMonth : month.atEndOfMonth();

            historicoFaturamento.add(zeroIfNull(
                    contasReceberRepository.calculateFaturamentoMes(empresaId, inicio, fim)));
            historicoDespesas.add(zeroIfNull(
                    contasPagarRepository.calculateDespesasMes(empresaId, inicio, fim)));
            historicoMeses.add(formatMonth(month));
        }

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
                historicoDespesas,
                historicoMeses,
                abertosMesVal,
                abertosTotalVal,
                autorizadosMesVal,
                autorizadosTotalVal,
                canceladosMesVal,
                canceladosTotalVal,
                fechadosMesVal,
                fechadosTotalVal,
                entradasVeiculosMesVal,
                saidasVeiculosMesVal,
                startOfMonth,
                endOfMonth,
                faturamentoPeriodoAnterior,
                despesasPeriodoAnterior,
                lucroPeriodoAnterior,
                abertosPeriodoAnterior,
                autorizadosPeriodoAnterior,
                canceladosPeriodoAnterior,
                fechadosPeriodoAnterior);
    }

    private static BigDecimal zeroIfNull(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private static String formatMonth(YearMonth month) {
        String[] labels = {"JAN", "FEV", "MAR", "ABR", "MAI", "JUN",
                "JUL", "AGO", "SET", "OUT", "NOV", "DEZ"};
        return labels[month.getMonthValue() - 1] + "/" + month.getYear();
    }
}
