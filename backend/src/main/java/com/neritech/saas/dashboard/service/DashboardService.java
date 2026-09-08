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

        Long totalClientes = clienteRepository.countByStatus(com.neritech.saas.cliente.domain.enums.StatusCliente.ATIVO);

        long osAbertas = ordemServicoRepository.countAtivas(empresaId);
        long osConcluidas = ordemServicoRepository.countConcluidas(empresaId);
        long osCanceladas = ordemServicoRepository.countCanceladas(empresaId);
        long osEmAndamento = osAbertas;

        BigDecimal faturamentoMes = contasReceberRepository.calculateFaturamentoMes(empresaId, startOfMonth, endOfMonth);
        if (faturamentoMes == null) faturamentoMes = BigDecimal.ZERO;

        BigDecimal despesasMes = contasPagarRepository.calculateDespesasMes(empresaId, startOfMonth, endOfMonth);
        if (despesasMes == null) despesasMes = BigDecimal.ZERO;

        BigDecimal lucroMes = faturamentoMes.subtract(despesasMes);

        BigDecimal ticketMedio = ordemServicoRepository.calculateTicketMedio(empresaId);
        if (ticketMedio == null) ticketMedio = BigDecimal.ZERO;

        BigDecimal contasReceber = contasReceberRepository.calculateTotalPendentes(empresaId);
        if (contasReceber == null) contasReceber = BigDecimal.ZERO;

        BigDecimal contasPagar = contasPagarRepository.calculateTotalPendentes(empresaId);
        if (contasPagar == null) contasPagar = BigDecimal.ZERO;

        BigDecimal valoresVencidos = contasReceberRepository.calculateTotalVencidos(empresaId, now);
        if (valoresVencidos == null) valoresVencidos = BigDecimal.ZERO;

        long veiculosEmAtraso = ordemServicoRepository.countAtrasadas(empresaId);

        java.time.LocalDateTime startOfMonthDateTime = startOfMonth.atStartOfDay();
        java.time.LocalDateTime endOfMonthDateTime = endOfMonth.atTime(23, 59, 59, 999999999);
        List<String> codigosAberto = List.of("ABERTA", "DIAGNOSTICO", "AGUARDANDO_APROVACAO");
        List<String> codigosAutorizado = List.of("APROVADA", "EM_EXECUCAO", "AGUARDANDO_PECAS");

        long abertosMesVal = ordemServicoRepository.countByStatusCodesAndPeriod(empresaId, codigosAberto, startOfMonthDateTime, endOfMonthDateTime);
        long abertosTotalVal = ordemServicoRepository.countByStatusCodes(empresaId, codigosAberto);

        long autorizadosMesVal = ordemServicoRepository.countByStatusCodesAndPeriod(empresaId, codigosAutorizado, startOfMonthDateTime, endOfMonthDateTime);
        long autorizadosTotalVal = ordemServicoRepository.countByStatusCodes(empresaId, codigosAutorizado);

        long canceladosMesVal = ordemServicoRepository.countByCancelaOSAndPeriod(empresaId, true, startOfMonthDateTime, endOfMonthDateTime);
        long canceladosTotalVal = ordemServicoRepository.countByCancelaOS(empresaId, true);

        long fechadosMesVal = ordemServicoRepository.countByFinalizaOSAndPeriod(empresaId, true, startOfMonthDateTime, endOfMonthDateTime);
        long fechadosTotalVal = ordemServicoRepository.countByFinalizaOS(empresaId, true);

        long entradasVeiculosMesVal = ordemServicoRepository.countByPeriod(empresaId, startOfMonthDateTime, endOfMonthDateTime);
        long saidasVeiculosMesVal = ordemServicoRepository.countSaidasByPeriod(empresaId, startOfMonthDateTime, endOfMonthDateTime);

        // Historico temporal ainda nao possui read model autoritativo. O backend nao fabrica dados.
        List<BigDecimal> historicoFaturamento = List.of();
        List<BigDecimal> historicoServicos = List.of();
        List<String> historicoMeses = List.of();

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
}
