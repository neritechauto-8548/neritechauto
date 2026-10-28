package com.neritech.saas.agendamento;

import com.neritech.saas.agendamento.domain.enums.CanalAgendamento;
import com.neritech.saas.agendamento.domain.enums.StatusAgendamento;
import com.neritech.saas.agendamento.dto.AgendamentoRequest;
import com.neritech.saas.agendamento.mapper.AgendamentoMapper;
import com.neritech.saas.agendamento.repository.AgendamentoRepository;
import com.neritech.saas.agendamento.repository.TipoAgendamentoRepository;
import com.neritech.saas.agendamento.service.AgendamentoService;
import com.neritech.saas.common.tenancy.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoPatioConflitoTest {
    @Mock AgendamentoRepository repository;
    @Mock TipoAgendamentoRepository types;
    @Mock AgendamentoMapper mapper;
    @Mock JdbcTemplate jdbc;
    @InjectMocks AgendamentoService service;

    @AfterEach void cleanup() { TenantContext.clear(); }

    @Test void naoAceitaEmpresaInformadaForaDoToken() {
        TenantContext.setCurrentTenant(7L);
        assertThatThrownBy(() -> service.criar(request(8L, "[1]")))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN));
        verifyNoInteractions(repository, jdbc);
    }

    @Test void agendamentoLegadoNaoPodeSobreporReservaPatio() {
        TenantContext.setCurrentTenant(7L);
        when(jdbc.queryForObject(eq("select id from empresa where id=? for update"),
                eq(Long.class), eq(7L))).thenReturn(7L);
        when(jdbc.queryForObject(contains("from cliente"), eq(Boolean.class),
                eq(11L), eq(7L))).thenReturn(true);
        when(jdbc.queryForObject(contains("from agd_reservas_recursos_os"), eq(Boolean.class),
                eq(7L), any(), any())).thenReturn(true);
        assertThatThrownBy(() -> service.criar(request(7L, "[1]")))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT));
        verify(repository, never()).save(any());
    }

    @Test void clienteDeOutraEmpresaNaoPodeSerReferenciado() {
        TenantContext.setCurrentTenant(7L);
        when(jdbc.queryForObject(eq("select id from empresa where id=? for update"),
                eq(Long.class), eq(7L))).thenReturn(7L);
        when(jdbc.queryForObject(contains("from cliente"), eq(Boolean.class),
                eq(11L), eq(7L))).thenReturn(false);

        assertThatThrownBy(() -> service.criar(request(7L, null)))
                .isInstanceOf(EntityNotFoundException.class);
        verify(repository, never()).save(any());
    }

    private AgendamentoRequest request(Long tenant, String resources) {
        return new AgendamentoRequest(tenant, null, 11L, null, null,
                LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(11, 0),
                60, null, null, null, null, null, null, resources,
                StatusAgendamento.AGENDADO, false, null, null, null, CanalAgendamento.TELEFONE);
    }
}
