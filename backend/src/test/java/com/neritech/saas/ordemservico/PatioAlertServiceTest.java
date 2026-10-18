package com.neritech.saas.ordemservico;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.ordemservico.service.PatioAlertService;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
class PatioAlertServiceTest {
    @Mock JdbcTemplate jdbc;
    @Mock UsuarioRepository users;
    private PatioAlertService service;

    @BeforeEach void setup() {
        service = new PatioAlertService(jdbc, users);
        TenantContext.setCurrentTenant(7L);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("gestor@example.test", "n/a"));
        Usuario actor = new Usuario(); actor.setId(4L);
        lenient().when(users.findByEmailIgnoreCaseAndEmpresaId("gestor@example.test", 7L))
                .thenReturn(Optional.of(actor));
    }

    @AfterEach void cleanup() { TenantContext.clear(); SecurityContextHolder.clearContext(); }

    @Test void alertaDeOutraEmpresaNaoPodeSerAlterado() {
        when(jdbc.query(contains("from patio_alertas where id=?"), any(RowMapper.class), eq(15L), eq(7L)))
                .thenReturn(List.of());
        assertThatThrownBy(() -> service.transition(15L, "acknowledge", 1, null, null))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
        verify(jdbc, never()).update(contains("update patio_alertas"), any(Object[].class));
    }

    @Test void alertaCriticoNaoPodeSerAdiado() throws Exception {
        when(jdbc.query(contains("from patio_alertas where id=?"), any(RowMapper.class), eq(15L), eq(7L)))
                .thenAnswer(invocation -> {
                    RowMapper<?> mapper = invocation.getArgument(1);
                    ResultSet rs = mock(ResultSet.class);
                    when(rs.getLong("id")).thenReturn(15L);
                    when(rs.getLong("ordem_servico_id")).thenReturn(30L);
                    when(rs.getString("tipo")).thenReturn("ATRASO");
                    when(rs.getString("dedupe_key")).thenReturn("30:ATRASO");
                    when(rs.getString("severidade")).thenReturn("CRITICA");
                    when(rs.getString("estado")).thenReturn("OPEN");
                    when(rs.getString("motivo")).thenReturn("Prazo de entrega ultrapassado");
                    when(rs.getInt("versao")).thenReturn(1);
                    return List.of(mapper.mapRow(rs, 0));
                });
        assertThatThrownBy(() -> service.transition(15L, "snooze", 1,
                "Aguardar retorno do responsável", LocalDateTime.now().plusHours(1)))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY));
        verify(jdbc, never()).update(contains("update patio_alertas"), any(Object[].class));
    }
}
