package com.neritech.saas.agendamento;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.agendamento.service.ReservaRecursoOsService;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaRecursoOsServiceTest {
    @Mock JdbcTemplate jdbc;
    @Mock OrdemServicoRepository orders;
    @Mock UsuarioRepository users;
    private ReservaRecursoOsService service;

    @BeforeEach void setup() {
        service = new ReservaRecursoOsService(jdbc, orders, users, new ObjectMapper());
        TenantContext.setCurrentTenant(7L);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("gestor@example.test", "n/a"));
        Usuario actor = new Usuario(); actor.setId(4L);
        lenient().when(users.findByEmailIgnoreCaseAndEmpresaId("gestor@example.test", 7L))
                .thenReturn(Optional.of(actor));
    }

    @AfterEach void cleanup() { TenantContext.clear(); SecurityContextHolder.clearContext(); }

    @Test void recusaReservaRetroativaAntesDePersistir() {
        LocalDateTime start = LocalDateTime.now().minusHours(2);
        assertThatThrownBy(() -> service.reserve(5L, 30L, start, start.plusHours(1), UUID.randomUUID()))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY));
        verifyNoInteractions(jdbc, orders);
    }

    @Test void recursoDeOutraEmpresaRetornaNaoEncontrado() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10);
        when(jdbc.query(contains("from agd_recursos_agenda"), any(RowMapper.class), eq(5L), eq(7L)))
                .thenReturn(List.of());
        assertThatThrownBy(() -> service.reserve(5L, 30L, start, start.plusHours(1), UUID.randomUUID()))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
        verifyNoInteractions(orders);
    }

    @Test void recursoIndisponivelNaoRecebeReserva() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10);
        when(jdbc.query(contains("from agd_recursos_agenda"), any(RowMapper.class), eq(5L), eq(7L)))
                .thenAnswer(invocation -> {
                    RowMapper<?> mapper = invocation.getArgument(1);
                    ResultSet rs = mock(ResultSet.class);
                    when(rs.getLong("id")).thenReturn(5L);
                    when(rs.getInt("capacidade_simultanea")).thenReturn(1);
                    when(rs.getBoolean("disponivel")).thenReturn(false);
                    return List.of(mapper.mapRow(rs, 0));
                });
        assertThatThrownBy(() -> service.reserve(5L, 30L, start, start.plusHours(1), UUID.randomUUID()))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY));
        verifyNoInteractions(orders);
    }
}
