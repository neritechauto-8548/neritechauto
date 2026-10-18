package com.neritech.saas.ordemservico;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.domain.StatusOS;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.ordemservico.repository.StatusOSRepository;
import com.neritech.saas.ordemservico.service.PatioService;
import com.neritech.saas.rh.domain.Funcionario;
import com.neritech.saas.rh.domain.enums.StatusFuncionario;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatioServiceTest {
    @Mock JdbcTemplate jdbc;
    @Mock OrdemServicoRepository orders;
    @Mock StatusOSRepository statuses;
    @Mock UsuarioRepository users;
    @Mock com.neritech.saas.ordemservico.repository.ItemOSServicoRepository items;
    @Mock com.neritech.saas.rh.repository.FuncionarioRepository employees;
    private PatioService service;

    @BeforeEach void setup() {
        service = new PatioService(jdbc, orders, statuses, users, items, employees, new ObjectMapper());
        TenantContext.setCurrentTenant(7L);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("gestor@example.test", "n/a"));
    }

    @AfterEach void cleanup() {
        TenantContext.clear();
        SecurityContextHolder.clearContext();
    }

    @Test void outraEmpresaNaoConsegueMoverOs() {
        when(orders.lockForPatio(22L, 7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.move(22L, 3L, 0, null))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
        verify(orders, never()).saveAndFlush(any());
        verifyNoInteractions(jdbc);
    }

    @Test void versaoAntigaRetornaConflitoSemAlterarOs() {
        OrdemServico order = new OrdemServico();
        order.setVersao(4);
        when(orders.lockForPatio(22L, 7L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> service.move(22L, 3L, 3, null))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT));
        verifyNoInteractions(statuses, jdbc);
        verify(orders, never()).saveAndFlush(any());
    }

    @Test void naoPermitePularAprovacaoNemFinalizarOs() {
        OrdemServico order = new OrdemServico();
        order.setVersao(1);
        StatusOS opened = status(1L, "ABERTA");
        order.setStatus(opened);
        when(orders.lockForPatio(22L, 7L)).thenReturn(Optional.of(order));
        when(statuses.findByIdAndEmpresaId(5L, 7L)).thenReturn(Optional.of(status(5L, "EM_EXECUCAO")));

        assertThatThrownBy(() -> service.move(22L, 5L, 1, null))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY));
        assertThat(order.getStatus()).isSameAs(opened);
        verify(orders, never()).saveAndFlush(any());
    }

    @Test void transicaoPermitidaAuditaAtorNoTenant() {
        OrdemServico order = new OrdemServico();
        order.setVersao(2);
        order.setStatus(status(1L, "ABERTA"));
        StatusOS target = status(2L, "DIAGNOSTICO");
        Usuario actor = new Usuario();
        actor.setId(13L);
        when(orders.lockForPatio(22L, 7L)).thenReturn(Optional.of(order));
        when(statuses.findByIdAndEmpresaId(2L, 7L)).thenReturn(Optional.of(target));
        when(users.findByEmailIgnoreCaseAndEmpresaId("gestor@example.test", 7L)).thenReturn(Optional.of(actor));

        var result = service.move(22L, 2L, 2, "Diagnóstico iniciado");

        assertThat(result.statusId()).isEqualTo(2L);
        assertThat(order.getStatus()).isSameAs(target);
        verify(orders).saveAndFlush(order);
        verify(jdbc).update(contains("insert into logs_alteracoes"), eq(7L), eq("ordens_servico"), eq(22L), anyString(),
                anyString(), anyString(), eq(13L), eq("Diagnóstico iniciado"));
    }

    @Test void naoTrocaResponsavelComCronometroEmAberto() {
        OrdemServico order = new OrdemServico();
        order.setStatus(status(5L, "EM_EXECUCAO"));
        ItemOSServico item = new ItemOSServico();
        item.setId(33L);
        item.setVersao(1);
        item.setOrdemServico(order);
        item.setMecanicoExecutorId(8L);
        Funcionario target = new Funcionario();
        target.setStatus(StatusFuncionario.ATIVO);
        when(items.lockForPatio(33L, 7L)).thenReturn(Optional.of(item));
        when(employees.findByIdAndEmpresaId(9L, 7L)).thenReturn(Optional.of(target));
        when(jdbc.queryForObject(contains("from tempos_execucao"), eq(Boolean.class), eq(7L), eq(33L)))
                .thenReturn(true);

        assertThatThrownBy(() -> service.assign(33L, 9L, 1))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT));
        assertThat(item.getMecanicoExecutorId()).isEqualTo(8L);
        verify(items, never()).saveAndFlush(any());
    }

    @Test void perfilSemLeituraDeClienteNaoPesquisaNomeNemRecebeNomeNoCartao() {
        service.board("Maria", null, null, null, null, 0, 20);

        verify(jdbc).queryForObject(argThat(sql -> sql.contains("lower(o.numero_os)")
                        && !sql.contains("lower(coalesce(c.nome_completo")),
                eq(Long.class), eq(7L), eq("%maria%"), eq("%maria%"));
        verify(jdbc).query(argThat(sql -> sql.contains("case when ? then c.nome_completo else null end")),
                any(org.springframework.jdbc.core.RowMapper.class),
                eq(false), eq(7L), eq("%maria%"), eq("%maria%"), eq(20), eq(0L));
    }

    @Test void aberturaSemFiltrosSeparaWhereDeOrderBy() {
        service.board("", null, null, null, null, 0, 20);

        verify(jdbc).query(argThat(sql -> sql.contains("coalesce(s.cancela_os,false)=false\norder by")
                        && !sql.contains("falseorder by")),
                any(org.springframework.jdbc.core.RowMapper.class),
                eq(false), eq(7L), eq(20), eq(0L));
    }

    private static StatusOS status(Long id, String code) {
        StatusOS status = new StatusOS();
        status.setId(id);
        status.setCodigo(code);
        status.setAtivo(true);
        status.setFinalizaOS(false);
        status.setCancelaOS(false);
        return status;
    }
}
