package com.neritech.saas.dashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.dashboard.controller.DashboardConfiguracaoController;
import com.neritech.saas.dashboard.controller.DashboardConfiguracaoController.Configuracao;
import com.neritech.saas.dashboard.controller.DashboardConfiguracaoController.WidgetConfig;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioResponse;
import com.neritech.saas.gestaoUsuarios.service.UsuarioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DashboardConfiguracaoControllerTest {
    private final JdbcTemplate jdbc=mock(JdbcTemplate.class);
    private final UsuarioService usuarios=mock(UsuarioService.class);
    private final DashboardConfiguracaoController controller=
        new DashboardConfiguracaoController(jdbc,usuarios,new ObjectMapper());

    @AfterEach void clean(){TenantContext.clear();}

    @Test void nuncaConsultaLayoutSemTenant() {
        assertThatThrownBy(controller::consultar)
            .isInstanceOf(RuntimeException.class);
        verifyNoInteractions(jdbc,usuarios);
    }

    @Test void impedeLeituraEEscritaQuandoUsuarioNaoPertenceEmpresa() {
        TenantContext.setCurrentTenant(3L);
        when(usuarios.getCurrentUser()).thenReturn(
            UsuarioResponse.builder().id(50L).empresaId(4L).build());
        assertThatThrownBy(controller::consultar).isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("403");
        assertThatThrownBy(()->controller.salvar(new Configuracao(List.of())))
            .isInstanceOf(ResponseStatusException.class).hasMessageContaining("403");
        verifyNoInteractions(jdbc);
    }

    @Test void rejeitaIdsInventadosDuplicadosETiposInvalidosSemGravar() {
        TenantContext.setCurrentTenant(3L);
        when(usuarios.getCurrentUser()).thenReturn(
            UsuarioResponse.builder().id(50L).empresaId(3L).build());
        for(Configuracao invalid:List.of(
            new Configuracao(List.of(new WidgetConfig("inexistente","indicador",1))),
            new Configuracao(List.of(new WidgetConfig("ordens","invalido",1))),
            new Configuracao(List.of(new WidgetConfig("ordens","grafico",4))),
            new Configuracao(List.of(new WidgetConfig("ordens","indicador",1),
                new WidgetConfig("ordens","indicador",1)))
        )){
            assertThatThrownBy(()->controller.salvar(invalid))
                .isInstanceOf(ResponseStatusException.class).hasMessageContaining("422");
        }
        verifyNoInteractions(jdbc);
    }

    @Test void guardaSomenteUsuarioETenantAutenticados() {
        TenantContext.setCurrentTenant(3L);
        when(usuarios.getCurrentUser()).thenReturn(
            UsuarioResponse.builder().id(50L).empresaId(3L).build());
        Configuracao result=controller.salvar(new Configuracao(
            List.of(new WidgetConfig("ticket-medio","indicador",1))));
        assertThat(result.componentes()).hasSize(1);
        verify(jdbc).update(contains("INSERT INTO dashboard_componentes_usuario"),
            eq(3L),eq(50L),contains("ticket-medio"));
    }
}
