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
        // Clientes Ativos
        Long totalClientes = clienteRepository
                .countByStatus(com.neritech.saas.cliente.domain.enums.StatusCliente.ATIVO);

        // OS - Preciso verificar os status IDs ou Enums. Assumindo counts simples por
        // enquanto ou countAll
        Long osAbertas = ordemServicoRepository.countByEmpresaId(empresaId); // TODO: Filtrar por status 'ABERTA' quando
                                                                             // souber o ID/Enum exato
        Long osEmAndamento = 0L; // Placeholder

        // Financeiro - Receitas
        // Assumindo que ContasReceber tem dataVencimento ou dataPagamento
        // Usarei um valor dummy se o método específico não existir no repository,
        // depois corrijo com o repository real
        BigDecimal faturamentoMes = BigDecimal.ZERO;

        // Financeiro - Despesas
        BigDecimal despesasMes = BigDecimal.ZERO;

        return new DashboardDTO(
                totalClientes != null ? totalClientes : 0L,
                osAbertas != null ? osAbertas : 0L,
                osEmAndamento,
                faturamentoMes,
                despesasMes,
                faturamentoMes.subtract(despesasMes));
    }
}
