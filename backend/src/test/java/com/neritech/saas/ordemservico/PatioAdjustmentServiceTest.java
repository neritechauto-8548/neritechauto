package com.neritech.saas.ordemservico;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.repository.ItemOSServicoRepository;
import com.neritech.saas.ordemservico.service.PatioAdjustmentService;
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
class PatioAdjustmentServiceTest {
    @Mock JdbcTemplate jdbc;
    @Mock ItemOSServicoRepository items;
    @Mock UsuarioRepository users;
    private PatioAdjustmentService service;

    @BeforeEach void setup() {
        service = new PatioAdjustmentService(jdbc, items, users, new ObjectMapper());
        TenantContext.setCurrentTenant(7L);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("gestor@example.test", "n/a"));
        Usuario user = new Usuario(); user.setId(3L);
        lenient().when(users.findByEmailIgnoreCaseAndEmpresaId("gestor@example.test", 7L))
                .thenReturn(Optional.of(user));
    }

    @AfterEach void cleanup() { TenantContext.clear(); SecurityContextHolder.clearContext(); }

    @Test void ajusteDeItemDeOutraEmpresaNaoEnumeraApontamento() {
        when(items.lockForPatio(12L, 7L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.adjust(12L, 80L, 5, "Correção justificada", 0, UUID.randomUUID()))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
        verifyNoInteractions(jdbc);
    }

    @Test void ajusteNaoPodeTornarTempoNegativo() {
        ItemOSServico item = item(5);
        when(items.lockForPatio(12L, 7L)).thenReturn(Optional.of(item));
        when(jdbc.query(contains("from patio_ajustes_tempo"), any(RowMapper.class), eq(7L), anyString()))
                .thenReturn(List.of());
        when(jdbc.queryForObject(contains("select exists"), eq(Boolean.class), eq(80L), eq(7L), eq(12L)))
                .thenReturn(true);

        assertThatThrownBy(() -> service.adjust(12L, 80L, -10, "Correção justificada", 2, UUID.randomUUID()))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY));
        verify(items, never()).saveAndFlush(any());
        verify(jdbc, never()).queryForObject(contains("insert into patio_ajustes_tempo"), eq(Long.class), any());
    }

    @Test void ajustePreservaOriginalEAuditaNovoTotal() {
        ItemOSServico item = item(30);
        when(items.lockForPatio(12L, 7L)).thenReturn(Optional.of(item));
        when(jdbc.query(contains("from patio_ajustes_tempo"), any(RowMapper.class), eq(7L), anyString()))
                .thenReturn(List.of());
        when(jdbc.queryForObject(contains("select exists"), eq(Boolean.class), eq(80L), eq(7L), eq(12L)))
                .thenReturn(true);
        when(jdbc.queryForObject(contains("insert into patio_ajustes_tempo"), eq(Long.class),
                eq(7L), eq(12L), eq(80L), anyString(), eq(5), anyString(), eq(3L), eq(2)))
                .thenReturn(91L);

        var result = service.adjust(12L, 80L, 5, "Correção justificada", 2, UUID.randomUUID());

        assertThat(result.id()).isEqualTo(91L);
        assertThat(item.getTempoExecucaoReal()).isEqualTo(35);
        verify(items).saveAndFlush(item);
        verify(jdbc).update(contains("insert into logs_alteracoes"), eq(7L), eq(12L),
                anyString(), anyString(), anyString(), eq(3L), eq("Correção justificada"));
    }

    private static ItemOSServico item(int minutes) {
        ItemOSServico item = new ItemOSServico();
        item.setId(12L); item.setVersao(2); item.setTempoExecucaoReal(minutes);
        return item;
    }
}
