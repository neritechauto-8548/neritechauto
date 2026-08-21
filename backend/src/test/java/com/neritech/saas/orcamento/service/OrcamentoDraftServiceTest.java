package com.neritech.saas.orcamento.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.orcamento.dto.OrcamentoDraftRequest;
import com.neritech.saas.orcamento.dto.OrcamentoDraftResponse;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.veiculo.domain.Veiculo;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrcamentoDraftServiceTest {

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

    private OrcamentoDraftService service() {
        return new OrcamentoDraftService(ordemServicoRepository, clienteRepository, veiculoRepository);
    }

    @Test
    void shouldFailClosedWhenAuthenticatedTenantIsMissing() {
        OrcamentoDraftRequest request = new OrcamentoDraftRequest(10L, null, null, null, null, null, null);

        assertThrows(IllegalStateException.class, () -> service().create(request));
        verifyNoInteractions(clienteRepository, veiculoRepository, ordemServicoRepository);
    }

    @Test
    void shouldRejectCustomerOutsideAuthenticatedTenant() {
        TenantContext.setCurrentTenant(7L);
        when(clienteRepository.findByIdScoped(10L)).thenReturn(Optional.empty());

        OrcamentoDraftRequest request = new OrcamentoDraftRequest(10L, null, null, null, null, null, null);

        assertThrows(BusinessException.class, () -> service().create(request));
        verify(clienteRepository).findByIdScoped(10L);
        verifyNoInteractions(veiculoRepository);
        verify(ordemServicoRepository, never()).save(any());
    }

    @Test
    void shouldRejectInactiveCustomer() {
        TenantContext.setCurrentTenant(7L);
        Cliente cliente = new Cliente();
        cliente.setId(10L);
        cliente.setStatus(StatusCliente.INATIVO);
        when(clienteRepository.findByIdScoped(10L)).thenReturn(Optional.of(cliente));

        OrcamentoDraftRequest request = new OrcamentoDraftRequest(10L, null, null, null, null, null, null);

        assertThrows(BusinessException.class, () -> service().create(request));
        verify(ordemServicoRepository, never()).save(any());
    }

    @Test
    void shouldRejectVehicleOutsideAuthenticatedTenant() {
        TenantContext.setCurrentTenant(7L);
        Cliente cliente = activeCustomer(10L);
        when(clienteRepository.findByIdScoped(10L)).thenReturn(Optional.of(cliente));
        when(veiculoRepository.findByIdAndEmpresaId(20L, 7L)).thenReturn(Optional.empty());

        OrcamentoDraftRequest request = new OrcamentoDraftRequest(10L, 20L, 1000, null, null, null, null);

        assertThrows(BusinessException.class, () -> service().create(request));
        verify(veiculoRepository).findByIdAndEmpresaId(20L, 7L);
        verify(ordemServicoRepository, never()).save(any());
    }

    @Test
    void shouldRejectVehicleOwnedByAnotherCustomer() {
        TenantContext.setCurrentTenant(7L);
        Cliente cliente = activeCustomer(10L);
        Cliente outroCliente = activeCustomer(11L);
        Veiculo veiculo = new Veiculo();
        veiculo.setId(20L);
        veiculo.setCliente(outroCliente);

        when(clienteRepository.findByIdScoped(10L)).thenReturn(Optional.of(cliente));
        when(veiculoRepository.findByIdAndEmpresaId(20L, 7L)).thenReturn(Optional.of(veiculo));

        OrcamentoDraftRequest request = new OrcamentoDraftRequest(10L, 20L, 1000, null, null, null, null);

        assertThrows(BusinessException.class, () -> service().create(request));
        verify(ordemServicoRepository, never()).save(any());
    }

    @Test
    void shouldCreateDraftUsingOnlyAuthenticatedTenantAndServerGeneratedNumber() {
        TenantContext.setCurrentTenant(7L);
        Cliente cliente = activeCustomer(10L);
        Veiculo veiculo = new Veiculo();
        veiculo.setId(20L);
        veiculo.setCliente(cliente);

        when(clienteRepository.findByIdScoped(10L)).thenReturn(Optional.of(cliente));
        when(veiculoRepository.findByIdAndEmpresaId(20L, 7L)).thenReturn(Optional.of(veiculo));
        when(ordemServicoRepository.existsByEmpresaIdAndNumeroOS(org.mockito.ArgumentMatchers.eq(7L), anyString()))
                .thenReturn(false);
        when(ordemServicoRepository.save(any(OrdemServico.class))).thenAnswer(invocation -> {
            OrdemServico entity = invocation.getArgument(0);
            entity.setId(99L);
            return entity;
        });

        OrcamentoDraftRequest request = new OrcamentoDraftRequest(
                10L,
                20L,
                12345,
                30L,
                "  Ruído ao frear  ",
                "  conferir discos  ",
                "  Cliente aguarda contato  ");

        OrcamentoDraftResponse response = service().create(request);

        ArgumentCaptor<OrdemServico> captor = ArgumentCaptor.forClass(OrdemServico.class);
        verify(ordemServicoRepository).save(captor.capture());
        OrdemServico saved = captor.getValue();

        assertEquals(7L, saved.getEmpresaId());
        assertEquals(10L, saved.getClienteId());
        assertEquals(20L, saved.getVeiculoId());
        assertEquals(TipoOS.ORCAMENTO, saved.getTipoOS());
        assertEquals(12345, saved.getQuilometragemEntrada());
        assertEquals(30L, saved.getConsultorResponsavelId());
        assertEquals("Ruído ao frear", saved.getProblemaRelatado());
        assertEquals(BigDecimal.ZERO, saved.getValorTotal());
        assertNotNull(saved.getNumeroOS());
        assertTrue(saved.getNumeroOS().startsWith("ORC-"));
        assertTrue(saved.getNumeroOS().length() <= 20);

        assertEquals(99L, response.id());
        assertEquals(saved.getNumeroOS(), response.numeroOrcamento());
        assertEquals("RASCUNHO", response.status());
    }

    private Cliente activeCustomer(Long id) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setStatus(StatusCliente.ATIVO);
        return cliente;
    }
}
