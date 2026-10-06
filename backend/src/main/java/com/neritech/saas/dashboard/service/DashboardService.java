package com.neritech.saas.dashboard.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
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
        LocalDate startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());

        Long totalClientes = clienteRepository.countByStatus(
                com.neritech.saas.cliente.domain.enums.StatusCliente.ATIVO);

        long osAbertas = ordemServicoRepository.countAtivas(empresaId);
        long osConcluidas = ordemServicoRepository.countConcluidas(empresaId);
        long osCanceladas = ordemServicoRepository.countCanceladas(empresaId);
        long osEmAndamento = osAbertas;

        BigDecimal faturamentoMes = zeroIfNull(
                contasReceberRepository.calculateFaturamentoMes(empresaId, startOfMonth, endOfMonth));
        BigDecimal despesasMes = zeroIfNull(
                contasPagarRepository.calculateDespesasMes(empresaId, startOfMonth, endOfMonth));
        BigDecimal lucroMes = faturamentoMes.subtract(despesasMes);

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
        long fechadosTotalVal = ordemServicoRepository.countByFinalizaOS(empresaId, true);

        long entradasVeiculosMesVal = ordemServicoRepository.countByPeriod(
                empresaId, startOfMonthDateTime, endOfMonthDateTime);
        long saidasVeiculosMesVal = ordemServicoRepository.countSaidasByPeriod(
                empresaId, startOfMonthDateTime, endOfMonthDateTime);

        // Histórico real dos últimos seis meses: faturamento recebido x despesas pagas.
        List<BigDecimal> historicoFaturamento = new ArrayList<>();
        List<BigDecimal> historicoDespesas = new ArrayList<>();
        List<String> historicoMeses = new ArrayList<>();

        YearMonth firstMonth = YearMonth.from(now).minusMonths(5);
        for (int i = 0; i < 6; i++) {
            YearMonth month = firstMonth.plusMonths(i);
            LocalDate inicio = month.atDay(1);
            LocalDate fim = month.atEndOfMonth();

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
                saidasVeiculosMesVal);
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
