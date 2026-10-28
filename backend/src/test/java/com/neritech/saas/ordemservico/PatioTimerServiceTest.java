package com.neritech.saas.ordemservico;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.StatusOS;
import com.neritech.saas.ordemservico.domain.enums.StatusExecucao;
import com.neritech.saas.ordemservico.repository.ItemOSServicoRepository;
import com.neritech.saas.ordemservico.service.PatioTimerService;
import com.neritech.saas.rh.domain.Funcionario;
import com.neritech.saas.rh.domain.enums.StatusFuncionario;
import com.neritech.saas.rh.repository.FuncionarioRepository;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatioTimerServiceTest {
    @Mock JdbcTemplate jdbc;
    @Mock ItemOSServicoRepository items;
    @Mock FuncionarioRepository employees;
    @Mock UsuarioRepository users;
    private PatioTimerService service;
    private ItemOSServico item;

    @BeforeEach void setup() {
        service = new PatioTimerService(jdbc, items, employees, users,
                new ObjectMapper().registerModule(new JavaTimeModule()));
        TenantContext.setCurrentTenant(7L);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("tecnico@example.test", "n/a"));
        Usuario actor = new Usuario(); actor.setId(9L);
        Funcionario employee = new Funcionario(); employee.setId(4L); employee.setStatus(StatusFuncionario.ATIVO);
        lenient().when(users.findByEmailIgnoreCaseAndEmpresaId("tecnico@example.test", 7L))
                .thenReturn(Optional.of(actor));
        lenient().when(employees.findByEmpresaIdAndUsuarioId(7L, 9L)).thenReturn(Optional.of(employee));
        OrdemServico order = new OrdemServico(); order.setId(30L); order.setAprovadoCliente(true);
        StatusOS status = new StatusOS(); status.setCodigo("EM_EXECUCAO"); order.setStatus(status);
        item = new ItemOSServico(); item.setId(22L); item.setOrdemServico(order);
    }

    @AfterEach void cleanup() { TenantContext.clear(); SecurityContextHolder.clearContext(); }

    @Test void rejeitaComandoInvalidoAntesDePersistir() {
        assertThatThrownBy(() -> service.command(22L, "reset", UUID.randomUUID()))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY));
        verifyNoInteractions(jdbc, items);
    }

    @Test void funcionarioNaoAtribuidoNaoIniciaApontamento() {
        item.setMecanicoExecutorId(5L);
        when(jdbc.update(contains("insert into patio_timer_comandos"), any(), any(), any(), any())).thenReturn(1);
        when(items.lockForPatio(22L, 7L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> service.command(22L, "start", UUID.randomUUID()))
                .isInstanceOf(AccessDeniedException.class);
        verify(jdbc, never()).queryForObject(contains("insert into tempos_execucao"), eq(Long.class), any());
    }

    @Test void iniciaSomenteServicoAprovadoEAtribuidoNoTenant() {
        item.setMecanicoExecutorId(4L);
        when(jdbc.update(contains("insert into patio_timer_comandos"), any(), any(), any(), any())).thenReturn(1);
        when(items.lockForPatio(22L, 7L)).thenReturn(Optional.of(item));
        when(jdbc.query(contains("for update"), any(RowMapper.class), eq(7L), eq(22L), eq(4L)))
                .thenReturn(List.of());
        when(jdbc.queryForObject(contains("select exists"), eq(Boolean.class), eq(7L), eq(4L)))
                .thenReturn(false);
        when(jdbc.queryForObject(contains("insert into tempos_execucao"), eq(Long.class),
                eq(7L), eq(30L), eq(22L), eq(4L), any(), any())).thenReturn(99L);

        var result = service.command(22L, "start", UUID.randomUUID());

        assertThat(result.id()).isEqualTo(99L);
        assertThat(result.estado()).isEqualTo("EM_EXECUCAO");
        assertThat(item.getStatusExecucao()).isEqualTo(StatusExecucao.EM_EXECUCAO);
        verify(items).saveAndFlush(item);
        verify(jdbc).update(contains("update patio_timer_comandos set resultado"), anyString(), eq(7L), anyString());
    }

    @Test void tecnicoComOutroServicoEmExecucaoRecebeConflito() {
        item.setMecanicoExecutorId(4L);
        when(jdbc.update(contains("insert into patio_timer_comandos"), any(), any(), any(), any())).thenReturn(1);
        when(items.lockForPatio(22L, 7L)).thenReturn(Optional.of(item));
        when(jdbc.query(contains("for update"), any(RowMapper.class), eq(7L), eq(22L), eq(4L)))
                .thenReturn(List.of());
        when(jdbc.queryForObject(contains("select exists"), eq(Boolean.class), eq(7L), eq(4L)))
                .thenReturn(true);

        assertThatThrownBy(() -> service.command(22L, "start", UUID.randomUUID()))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT));
        verify(jdbc, never()).queryForObject(contains("insert into tempos_execucao"), eq(Long.class), any());
    }

    @Test void corridaEntreDispositivosRetornaConflitoSemConfirmarApontamento() {
        item.setMecanicoExecutorId(4L);
        when(jdbc.update(contains("insert into patio_timer_comandos"), any(), any(), any(), any())).thenReturn(1);
        when(items.lockForPatio(22L, 7L)).thenReturn(Optional.of(item));
        when(jdbc.query(contains("for update"), any(RowMapper.class), eq(7L), eq(22L), eq(4L)))
                .thenReturn(List.of());
        when(jdbc.queryForObject(contains("select exists"), eq(Boolean.class), eq(7L), eq(4L)))
                .thenReturn(false);
        when(jdbc.queryForObject(contains("insert into tempos_execucao"), eq(Long.class),
                eq(7L), eq(30L), eq(22L), eq(4L), any(), any()))
                .thenThrow(new DuplicateKeyException("execução simultânea"));

        assertThatThrownBy(() -> service.command(22L, "start", UUID.randomUUID()))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT));
        verify(items, never()).saveAndFlush(any());
        verify(jdbc, never()).update(contains("update patio_timer_comandos set resultado"), any(), any(), any());
    }
}
