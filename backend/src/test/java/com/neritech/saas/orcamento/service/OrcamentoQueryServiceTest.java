package com.neritech.saas.orcamento.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.orcamento.dto.OrcamentoListResponse;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.StatusOS;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.veiculo.domain.MarcaVeiculo;
import com.neritech.saas.veiculo.domain.ModeloVeiculo;
import com.neritech.saas.veiculo.domain.Veiculo;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrcamentoQueryServiceTest {

    @Mock
    private OrdemServicoRepository ordemServicoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private VeiculoRepository veiculoRepository;

    @AfterEach
    void clearTenant() {
        TenantContext.clear();
    }

    private OrcamentoQueryService service() {
        return new OrcamentoQueryService(ordemServicoRepository, clienteRepository, veiculoRepository);
    }

    @Test
    void shouldFailClosedWhenAuthenticatedTenantIsMissing() {
        assertThrows(IllegalStateException.class, () -> service().list(null, null, 0, 25, null));
        verifyNoInteractions(ordemServicoRepository, clienteRepository, veiculoRepository);
    }

    @Test
    void shouldRejectUnknownStatusBeforeQueryingPersistence() {
        TenantContext.setCurrentTenant(7L);

        assertThrows(IllegalArgumentException.class,
                () -> service().list(null, "STATUS_INVENTADO", 0, 25, null));
        verifyNoInteractions(ordemServicoRepository, clienteRepository, veiculoRepository);
    }

    @Test
    void shouldUseAuthenticatedTenantAndMinimizedScopedReadModels() {
        TenantContext.setCurrentTenant(7L);
        OrdemServico budget = budget();
        PageRequest request = PageRequest.of(0, 25);
        when(ordemServicoRepository.searchBudgets(
                eq(7L), eq(TipoOS.ORCAMENTO), eq("RASCUNHO"), eq("ORC-001"), eq("orc001"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(budget), request, 1));

        Cliente customer = new Cliente();
        customer.setId(10L);
        customer.setTipoCliente(TipoCliente.PESSOA_FISICA);
        customer.setNomeCompleto("João Silva");
        customer.setCpf("12345678900");
        customer.setEmail("joao@example.com");
        when(clienteRepository.findAllByEmpresaIdAndIdIn(eq(7L), any())).thenReturn(List.of(customer));

        Veiculo vehicle = new Veiculo();
        vehicle.setId(20L);
        vehicle.setPlaca("ABC1D23");
        vehicle.setMarca(new MarcaVeiculo(1L, "Chevrolet"));
        ModeloVeiculo model = new ModeloVeiculo();
        model.setNome("Onix");
        vehicle.setModelo(model);
        when(veiculoRepository.findSummariesByEmpresaIdAndIdIn(eq(7L), any())).thenReturn(List.of(vehicle));

        OrcamentoListResponse response = service().list(" ORC-001 ", "rascunho", 0, 250, "total,asc");

        ArgumentCaptor<Pageable> pageable = ArgumentCaptor.forClass(Pageable.class);
        verify(ordemServicoRepository).searchBudgets(
                eq(7L), eq(TipoOS.ORCAMENTO), eq("RASCUNHO"), eq("ORC-001"), eq("orc001"), pageable.capture());
        assertEquals(100, pageable.getValue().getPageSize());
        assertTrue(pageable.getValue().getSort().getOrderFor("valorTotal").isAscending());

        assertEquals(1, response.items().size());
        assertEquals("ORC-001", response.items().getFirst().numero());
        assertEquals("João Silva", response.items().getFirst().cliente().nome());
        assertEquals("Chevrolet Onix", response.items().getFirst().veiculo().descricao());
        assertEquals("ABC1D23", response.items().getFirst().veiculo().placa());
        assertEquals(new BigDecimal("1250.50"), response.items().getFirst().total().amount());
        assertEquals("BRL", response.items().getFirst().total().currency());
        assertEquals(List.of("OPEN", "CONTINUE_EDIT"), response.items().getFirst().allowedActions());
        assertNull(response.items().getFirst().validadeEm());
        assertNull(response.items().getFirst().comunicacaoStatus());
        assertFalse(response.summaryAvailable());
        assertEquals("INDICADORES_AGREGADOS_NAO_DISPONIVEIS", response.summaryUnavailableReason());
    }

    @Test
    void shouldNotLoadCustomerOrVehicleRepositoriesForEmptyPage() {
        TenantContext.setCurrentTenant(7L);
        when(ordemServicoRepository.searchBudgets(eq(7L), eq(TipoOS.ORCAMENTO), eq(null), eq(null), eq(null), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        OrcamentoListResponse response = service().list(null, null, -1, 0, "unknown,desc");

        assertTrue(response.items().isEmpty());
        verifyNoInteractions(clienteRepository, veiculoRepository);
    }

    @Test
    void shouldScopeDetailLookupToAuthenticatedTenant() {
        TenantContext.setCurrentTenant(7L);
        when(ordemServicoRepository.findByIdAndEmpresaId(90L, 7L)).thenReturn(Optional.empty());

        assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> service().findById(90L));

        verify(ordemServicoRepository).findByIdAndEmpresaId(90L, 7L);
        verifyNoInteractions(clienteRepository, veiculoRepository);
    }

    @Test
    void shouldRejectNonBudgetEntityOnCanonicalDetailEndpoint() {
        TenantContext.setCurrentTenant(7L);
        OrdemServico workOrder = budget();
        workOrder.setTipoOS(TipoOS.REPARO);
        when(ordemServicoRepository.findByIdAndEmpresaId(90L, 7L)).thenReturn(Optional.of(workOrder));

        assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> service().findById(90L));
        verifyNoInteractions(clienteRepository, veiculoRepository);
    }

    private OrdemServico budget() {
        OrdemServico budget = new OrdemServico();
        budget.setId(90L);
        budget.setEmpresaId(7L);
        budget.setNumeroOS("ORC-001");
        budget.setTipoOS(TipoOS.ORCAMENTO);
        budget.setClienteId(10L);
        budget.setVeiculoId(20L);
        budget.setValorTotal(new BigDecimal("1250.50"));
        budget.setVersao(2);
        budget.setDataCadastro(LocalDateTime.of(2026, 8, 22, 10, 0));
        budget.setDataAtualizacao(LocalDateTime.of(2026, 8, 22, 11, 0));
        StatusOS status = new StatusOS();
        status.setCodigo("RASCUNHO");
        budget.setStatus(status);
        return budget;
    }
}
