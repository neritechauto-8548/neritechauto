package com.neritech.saas.veiculo.service;

import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.veiculo.dto.VeiculoRequest;
import com.neritech.saas.veiculo.mapper.VeiculoMapper;
import com.neritech.saas.veiculo.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class VeiculoServiceTenantTest {
    private final VeiculoRepository vehicles = mock(VeiculoRepository.class);
    private final ClienteRepository customers = mock(ClienteRepository.class);
    private final VeiculoService service = new VeiculoService(vehicles, customers,
        mock(MarcaVeiculoRepository.class), mock(ModeloVeiculoRepository.class),
        mock(AnoModeloRepository.class), mock(TipoCombustivelRepository.class),
        mock(VehicleExternalLookupService.class), mock(VeiculoMapper.class));

    @AfterEach void clear() { TenantContext.clear(); }

    private VeiculoRequest request(Long clienteId) {
        return new VeiculoRequest(clienteId, null, null, null, null,
            "ABC1D23", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test void noTenantCannotEnumerateVehicles() {
        assertThatThrownBy(service::findAll).isInstanceOf(ResponseStatusException.class);
        verifyNoInteractions(vehicles);
    }

    @Test void cannotCreateVehicleForCustomerOfAnotherCompany() {
        TenantContext.setCurrentTenant(7L);
        assertThatThrownBy(() -> service.create(request(33L)))
                .isInstanceOf(EntityNotFoundException.class);
        verify(customers).findByIdScoped(33L);
        verifyNoInteractions(vehicles);
    }

    @Test void cannotReadOrDeleteVehicleOutsideCompany() {
        TenantContext.setCurrentTenant(7L);
        when(vehicles.findByIdAndEmpresaId(99L, 7L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(99L)).isInstanceOf(EntityNotFoundException.class);
        assertThatThrownBy(() -> service.delete(99L)).isInstanceOf(EntityNotFoundException.class);
        verify(vehicles, never()).delete(any(com.neritech.saas.veiculo.domain.Veiculo.class));
    }

    @Test void cannotUpdateVehicleOfAnotherCompany() {
        TenantContext.setCurrentTenant(7L);
        assertThatThrownBy(() -> service.update(99L, request(33L)))
                .isInstanceOf(EntityNotFoundException.class);
        verify(vehicles).findByIdAndEmpresaId(99L, 7L);
        verifyNoInteractions(customers);
    }
}
