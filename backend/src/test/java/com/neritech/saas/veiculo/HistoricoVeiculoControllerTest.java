package com.neritech.saas.veiculo;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.veiculo.controller.HistoricoVeiculoController;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.ordemservico.repository.ItemOSServicoRepository;
import com.neritech.saas.ordemservico.repository.ItemOSProdutoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

class HistoricoVeiculoControllerTest {
    private final VeiculoRepository veiculos = mock(VeiculoRepository.class);
    private final OrdemServicoRepository ordens = mock(OrdemServicoRepository.class);
    private final ItemOSServicoRepository servicos = mock(ItemOSServicoRepository.class);
    private final ItemOSProdutoRepository produtos = mock(ItemOSProdutoRepository.class);
    private final HistoricoVeiculoController controller =
        new HistoricoVeiculoController(veiculos, ordens, servicos, produtos);

    @AfterEach void cleanup() { TenantContext.clear(); }

    @Test void mustHaveCompanyInAuthenticatedContext() {
        assertThatThrownBy(() -> controller.listar("", "AZ", 0, 10))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("403");
        verifyNoInteractions(veiculos, ordens, servicos, produtos);
    }

    @Test void mustNotReadForeignVehicleHistory() {
        TenantContext.setCurrentTenant(20L);
        when(veiculos.findByIdAndEmpresaId(123L,20L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> controller.ordens(123L,false,false,"","OS",0,10))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("404");
        verify(veiculos).findByIdAndEmpresaId(123L,20L);
        verifyNoInteractions(ordens, servicos, produtos);
    }

    @Test void paginationBoundsRejectUnboundedSearch() {
        TenantContext.setCurrentTenant(20L);
        assertThatThrownBy(() -> controller.listar("","AZ",0,101))
            .isInstanceOf(ResponseStatusException.class).hasMessageContaining("400");
        assertThatThrownBy(() -> controller.listar("","AZ",-1,10))
            .isInstanceOf(ResponseStatusException.class).hasMessageContaining("400");
        verifyNoInteractions(veiculos, ordens, servicos, produtos);
    }

    @SuppressWarnings("unchecked")
    @Test void emptyListComesFromScopedRepositoryQuery() {
        TenantContext.setCurrentTenant(20L);
        when(veiculos.findAll(any(Specification.class),any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of()));
        var result = controller.listar("GOL","AZ",0,10);
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
        verify(veiculos).findAll(any(Specification.class),any(Pageable.class));
        verifyNoInteractions(ordens, servicos, produtos);
    }
}
