package com.neritech.saas.ordemservico.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.domain.StatusOS;
import com.neritech.saas.ordemservico.domain.enums.StatusExecucao;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import com.neritech.saas.ordemservico.repository.ItemOSServicoRepository;
import com.neritech.saas.rh.domain.enums.StatusFuncionario;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PatioTimerService {
    private final JdbcTemplate jdbc;
    private final ItemOSServicoRepository items;
    private final FuncionarioRepository employees;
    private final UsuarioRepository users;
    private final ObjectMapper json;

    public PatioTimerService(JdbcTemplate jdbc, ItemOSServicoRepository items,
            FuncionarioRepository employees, UsuarioRepository users, ObjectMapper json) {
        this.jdbc=jdbc; this.items=items; this.employees=employees; this.users=users; this.json=json;
    }

    @Transactional(readOnly = true)
    public EmployeeIdentity myEmployee() {
        Long tenant = TenantAccess.requireCurrent();
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario actor = (auth == null ? java.util.Optional.<Usuario>empty()
                : users.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), tenant))
                .orElseThrow(() -> new AccessDeniedException("Autor inválido"));
        var employee = employees.findByEmpresaIdAndUsuarioId(tenant, actor.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuário não está vinculado a funcionário"));
        return new EmployeeIdentity(employee.getId());
    }

    @Transactional(readOnly = true)
    public List<TimerView> history(Long itemId) {
        Long tenant = TenantAccess.requireCurrent();
        items.findByIdAndOrdemServicoEmpresaId(itemId, tenant)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço da OS não encontrado"));
        LocalDateTime now = LocalDateTime.now();
        return jdbc.query("""
                select id, item_os_servico_id, funcionario_id, estado, segundos_ativos,
                       trecho_inicio, data_inicio, data_fim
                from tempos_execucao where empresa_id=? and item_os_servico_id=?
                order by id desc
                """, (rs, row) -> view(row(rs), now), tenant, itemId);
    }

    @Transactional
    public TimerView command(Long itemId, String action, UUID key) {
        Long tenant = TenantAccess.requireCurrent();
        if (!List.of("start", "pause", "resume", "finish").contains(action)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Comando de cronômetro inválido");
        }
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario actor = (auth == null ? java.util.Optional.<Usuario>empty()
                : users.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), tenant))
                .orElseThrow(() -> new AccessDeniedException("Autor inválido"));
        var employee = employees.findByEmpresaIdAndUsuarioId(tenant, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Usuário sem funcionário vinculado"));
        if (employee.getStatus() != StatusFuncionario.ATIVO) throw new AccessDeniedException("Funcionário inativo");

        int reserved = jdbc.update("""
                insert into patio_timer_comandos (empresa_id,chave,acao,ator_id)
                values (?,?::uuid,?,?) on conflict do nothing
                """, tenant, key.toString(), action, actor.getId());
        if (reserved == 0) return replay(tenant, actor.getId(), itemId, action, key);

        ItemOSServico item = items.lockForPatio(itemId, tenant)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço da OS não encontrado"));
        if (item.getOrdemServico().getTipoOS() == TipoOS.ORCAMENTO ||
                item.getOrdemServico().getTipoOS() == TipoOS.VENDA_PRODUTO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Tipo de OS fora do Pátio");
        }
        if (!employee.getId().equals(item.getMecanicoExecutorId())) {
            throw new AccessDeniedException("Serviço não atribuído ao funcionário autenticado");
        }
        StatusOS osStatus = item.getOrdemServico().getStatus();
        if (osStatus != null && (Boolean.TRUE.equals(osStatus.getFinalizaOS()) || Boolean.TRUE.equals(osStatus.getCancelaOS()))) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "OS encerrada");
        }
        LocalDateTime now = LocalDateTime.now();
        List<TimerRow> active = jdbc.query("""
                select id, item_os_servico_id, funcionario_id, estado, segundos_ativos,
                       trecho_inicio, data_inicio, data_fim
                from tempos_execucao where empresa_id=? and item_os_servico_id=?
                  and funcionario_id=? and estado in ('EM_EXECUCAO','PAUSADO')
                for update
                """, (rs, row) -> row(rs), tenant, itemId, employee.getId());
        TimerRow before = active.isEmpty() ? null : active.get(0);
        TimerRow after;
        if (action.equals("start")) {
            if (before != null || item.getStatusExecucao() == StatusExecucao.CONCLUIDO
                    || item.getStatusExecucao() == StatusExecucao.CANCELADO
                    || osStatus == null || !"EM_EXECUCAO".equals(osStatus.getCodigo())
                    || !(Boolean.TRUE.equals(item.getAprovadoCliente())
                         || Boolean.TRUE.equals(item.getOrdemServico().getAprovadoCliente()))) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "Serviço não está aprovado e disponível para execução");
            }
            assertTechnicianAvailable(tenant, employee.getId());
            Long id;
            try {
                id = jdbc.queryForObject("""
                    insert into tempos_execucao (empresa_id,ordem_servico_id,item_os_servico_id,
                    funcionario_id,tipo_tempo,data_inicio,estado,segundos_ativos,trecho_inicio)
                    values (?,?,?,?, 'EXECUCAO',?,'EM_EXECUCAO',0,?) returning id
                    """, Long.class, tenant, item.getOrdemServico().getId(), itemId,
                    employee.getId(), now, now);
            } catch (DuplicateKeyException ex) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Técnico já possui apontamento em execução", ex);
            }
            after = new TimerRow(id, itemId, employee.getId(), "EM_EXECUCAO", 0, now, now, null);
            if (item.getDataInicioExecucao() == null) item.setDataInicioExecucao(now);
            item.setStatusExecucao(StatusExecucao.EM_EXECUCAO);
            items.saveAndFlush(item);
        } else {
            if (before == null) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Nenhum cronômetro ativo para este serviço");
            long elapsed = before.seconds + (before.segment == null ? 0 :
                    Math.max(0, Duration.between(before.segment, now).getSeconds()));
            switch (action) {
                case "pause" -> {
                    if (!before.state.equals("EM_EXECUCAO")) invalidState();
                    jdbc.update("""
                            update tempos_execucao set estado='PAUSADO',segundos_ativos=?,trecho_inicio=null
                            where id=? and empresa_id=?
                            """, elapsed, before.id, tenant);
                    after = new TimerRow(before.id, itemId, employee.getId(), "PAUSADO", elapsed, null, before.started, null);
                }
                case "resume" -> {
                    if (!before.state.equals("PAUSADO")) invalidState();
                    if (osStatus == null || !"EM_EXECUCAO".equals(osStatus.getCodigo())) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                                "OS não está em execução para retomar o apontamento");
                    }
                    assertTechnicianAvailable(tenant, employee.getId());
                    try {
                        jdbc.update("""
                            update tempos_execucao set estado='EM_EXECUCAO',trecho_inicio=?
                            where id=? and empresa_id=?
                            """, now, before.id, tenant);
                    } catch (DuplicateKeyException ex) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Técnico já possui apontamento em execução", ex);
                    }
                    after = new TimerRow(before.id, itemId, employee.getId(), "EM_EXECUCAO", before.seconds,
                            now, before.started, null);
                }
                case "finish" -> {
                    jdbc.update("""
                            update tempos_execucao set estado='FINALIZADO',segundos_ativos=?,trecho_inicio=null,
                            data_fim=?,duracao_minutos=? where id=? and empresa_id=?
                            """, elapsed, now, (int) (elapsed / 60), before.id, tenant);
                    after = new TimerRow(before.id, itemId, employee.getId(), "FINALIZADO", elapsed,
                            null, before.started, now);
                    item.setTempoExecucaoReal(Math.addExact(item.getTempoExecucaoReal() == null ? 0 :
                            item.getTempoExecucaoReal(), (int) (elapsed / 60)));
                    item.setDataFimExecucao(now);
                    item.setStatusExecucao(StatusExecucao.CONCLUIDO);
                    items.saveAndFlush(item);
                }
                default -> throw new IllegalStateException("Comando inválido");
            }
        }
        TimerView result = view(after, now);
        try {
            jdbc.update("update patio_timer_comandos set resultado=?::jsonb where empresa_id=? and chave=?::uuid",
                    json.writeValueAsString(result), tenant, key.toString());
            jdbc.update("""
                    insert into logs_alteracoes (empresa_id,tabela_afetada,registro_id,operacao,
                    campos_alterados,valores_antigos,valores_novos,usuario_responsavel,
                    motivo_alteracao,contexto_operacao,data_alteracao)
                    values (?,'tempos_execucao',?,?,?,?,?,?,?,'PATIO-001',current_timestamp)
                    """, tenant, after.id, action.equals("start") ? "INSERT" : "UPDATE",
                    "[\"estado\",\"segundos_ativos\"]",
                    json.writeValueAsString(Map.of("estado", before == null ? "NOVO" : before.state)),
                    json.writeValueAsString(Map.of("estado", after.state, "segundosAtivos", after.seconds)),
                    actor.getId(), action);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Falha ao persistir comando do cronômetro", ex);
        }
        return result;
    }

    private void assertTechnicianAvailable(Long tenant, Long employeeId) {
        Boolean busy = jdbc.queryForObject("""
                select exists (select 1 from tempos_execucao
                where empresa_id=? and funcionario_id=? and estado='EM_EXECUCAO')
                """, Boolean.class, tenant, employeeId);
        if (Boolean.TRUE.equals(busy)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Técnico já possui apontamento em execução");
        }
    }

    private TimerView replay(Long tenant, Long actorId, Long itemId, String action, UUID key) {
        return jdbc.queryForObject("""
                select acao,ator_id,resultado::text from patio_timer_comandos where empresa_id=? and chave=?::uuid
                """, (rs, row) -> {
                    if (!action.equals(rs.getString("acao")) || !actorId.equals(rs.getLong("ator_id"))) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Chave usada para outro comando");
                    }
                    try {
                        TimerView result = json.readValue(rs.getString("resultado"), TimerView.class);
                        if (!itemId.equals(result.itemId())) {
                            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chave usada para outro serviço");
                        }
                        return result;
                    }
                    catch (JsonProcessingException ex) { throw new IllegalStateException("Resposta de comando inválida", ex); }
                }, tenant, key.toString());
    }

    private static void invalidState() {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Estado do cronômetro alterado");
    }

    private static TimerRow row(ResultSet rs) throws SQLException {
        var segment = rs.getTimestamp("trecho_inicio");
        var ended = rs.getTimestamp("data_fim");
        return new TimerRow(rs.getLong("id"), rs.getLong("item_os_servico_id"),
                rs.getLong("funcionario_id"), rs.getString("estado"), rs.getLong("segundos_ativos"),
                segment == null ? null : segment.toLocalDateTime(), rs.getTimestamp("data_inicio").toLocalDateTime(),
                ended == null ? null : ended.toLocalDateTime());
    }

    private static TimerView view(TimerRow row, LocalDateTime now) {
        long elapsed = row.seconds + (row.segment == null ? 0 :
                Math.max(0, Duration.between(row.segment, now).getSeconds()));
        return new TimerView(row.id, row.itemId, row.employeeId, row.state, elapsed,
                row.started, row.ended, now);
    }

    private record TimerRow(Long id, Long itemId, Long employeeId, String state, long seconds,
            LocalDateTime segment, LocalDateTime started, LocalDateTime ended) {}
    public record TimerView(Long id, Long itemId, Long funcionarioId, String estado,
            long segundosAtivos, LocalDateTime inicio, LocalDateTime fim, LocalDateTime atualizadoEm) {}
    public record EmployeeIdentity(Long id) {}
}
