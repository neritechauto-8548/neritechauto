package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/** Alerta é estado sobre um fato da OS, nunca uma cópia mutável da OS. */
@Service
public class PatioAlertService {
    private final JdbcTemplate jdbc;
    private final UsuarioRepository users;

    public PatioAlertService(JdbcTemplate jdbc, UsuarioRepository users) {
        this.jdbc = jdbc;
        this.users = users;
    }

    @Transactional
    public AlertBoard list(int page, int size) {
        Long tenant = TenantAccess.requireCurrent();
        if (page < 0 || !List.of(20, 50, 100).contains(size)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        }
        reconcile(tenant);
        Long total = jdbc.queryForObject("""
                select count(*) from patio_alertas where empresa_id=? and estado<>'RESOLVED'
                """, Long.class, tenant);
        List<AlertView> alerts = jdbc.query("""
                select a.id,a.ordem_servico_id,o.numero_os,f.nome_completo as responsavel,
                       a.tipo,a.severidade,a.estado,a.motivo,
                       a.detectado_em,a.atualizado_em,a.adiado_ate,a.versao
                from patio_alertas a join ordens_servico o on o.id=a.ordem_servico_id and o.empresa_id=a.empresa_id
                left join funcionarios f on f.id=o.mecanico_responsavel_id and f.empresa_id=a.empresa_id
                where a.empresa_id=? and a.estado<>'RESOLVED'
                order by case a.severidade when 'CRITICA' then 0 when 'ALTA' then 1
                    when 'MEDIA' then 2 else 3 end, a.detectado_em, a.id
                limit ? offset ?
                """, (rs, row) -> alertView(rs), tenant, size, (long) page * size);
        return new AlertBoard(alerts, total == null ? 0 : total, page, size, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<AlertEvent> history(Long alertId) {
        Long tenant = TenantAccess.requireCurrent();
        Long count = jdbc.queryForObject("select count(*) from patio_alertas where id=? and empresa_id=?",
                Long.class, alertId, tenant);
        if (count == null || count == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alerta não encontrado");
        return jdbc.query("""
                select id,estado_antes,estado_depois,usuario_responsavel,motivo,criado_em
                from patio_alerta_eventos where empresa_id=? and alerta_id=? order by id desc
                """, (rs, row) -> new AlertEvent(rs.getLong("id"), rs.getString("estado_antes"),
                rs.getString("estado_depois"), rs.getObject("usuario_responsavel", Long.class),
                rs.getString("motivo"), rs.getTimestamp("criado_em").toLocalDateTime()), tenant, alertId);
    }

    @Transactional
    public AlertView transition(Long alertId, String action, Integer expectedVersion,
            String reason, LocalDateTime snoozeUntil) {
        Long tenant = TenantAccess.requireCurrent();
        Long actor = actor(tenant);
        List<AlertRow> locked = jdbc.query("""
                select id,ordem_servico_id,tipo,dedupe_key,severidade,estado,motivo,
                       detectado_em,atualizado_em,adiado_ate,versao
                from patio_alertas where id=? and empresa_id=? for update
                """, (rs, row) -> alertRow(rs), alertId, tenant);
        if (locked.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alerta não encontrado");
        AlertRow before = locked.get(0);
        if (!java.util.Objects.equals(before.version, expectedVersion)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Alerta alterado por outro operador");
        }
        if (before.state.equals("RESOLVED")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Alerta já resolvido");
        }
        String next = switch (action) {
            case "acknowledge" -> "ACKNOWLEDGED";
            case "snooze" -> "SNOOZED";
            case "resolve" -> "RESOLVED";
            default -> throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Ação de alerta inválida");
        };
        String note = reason == null ? "" : reason.strip();
        if (next.equals("ACKNOWLEDGED") && !before.state.equals("OPEN")) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Alerta não está aberto");
        }
        if (next.equals("SNOOZED")) {
            if (before.state.equals("SNOOZED") || before.severity.equals("CRITICA")
                    || note.length() < 10 || note.length() > 500 || snoozeUntil == null
                    || !snoozeUntil.isAfter(LocalDateTime.now())
                    || snoozeUntil.isAfter(LocalDateTime.now().plusDays(7))) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Adiamento não permitido");
            }
        }
        // Resolver só altera o alerta. Se a origem persistir, a próxima reconciliação o reabre.
        jdbc.update("""
                update patio_alertas set estado=?,adiado_ate=?,resolvido_em=?,
                    atualizado_em=current_timestamp,versao=versao+1 where id=? and empresa_id=?
                """, next, next.equals("SNOOZED") ? snoozeUntil : null,
                next.equals("RESOLVED") ? LocalDateTime.now() : null, alertId, tenant);
        event(tenant, alertId, before.state, next, actor,
                note.isEmpty() ? (next.equals("RESOLVED") ? "Condição conferida e resolvida" :
                        "Operador reconheceu alerta") : note);
        return get(tenant, alertId);
    }

    private void reconcile(Long tenant) {
        // Serializa descoberta e deduplicação inclusive quando ainda não há alertas para bloquear.
        jdbc.queryForObject("select id from empresa where id=? for update", Long.class, tenant);
        List<Condition> facts = conditions(tenant);
        List<AlertRow> all = jdbc.query("""
                select id,ordem_servico_id,tipo,dedupe_key,severidade,estado,motivo,
                       detectado_em,atualizado_em,adiado_ate,versao
                from patio_alertas where empresa_id=? for update
                """, (rs, row) -> alertRow(rs), tenant);
        Map<String, AlertRow> stored = new HashMap<>();
        all.forEach(row -> stored.put(row.key, row));
        Set<String> activeKeys = new HashSet<>();
        LocalDateTime now = LocalDateTime.now();
        for (Condition fact : facts) {
            activeKeys.add(fact.key());
            AlertRow current = stored.get(fact.key());
            if (current == null) {
                Long id = jdbc.queryForObject("""
                        insert into patio_alertas (empresa_id,ordem_servico_id,tipo,dedupe_key,
                        severidade,estado,motivo) values (?,?,?,?,?,'OPEN',?) returning id
                        """, Long.class, tenant, fact.osId, fact.type, fact.key(), fact.severity, fact.reason);
                event(tenant, id, null, "OPEN", null, "Condição detectada na OS");
            } else if (current.state.equals("RESOLVED") || current.state.equals("SNOOZED")
                    && current.snoozeUntil != null && !current.snoozeUntil.isAfter(now)) {
                jdbc.update("""
                        update patio_alertas set estado='OPEN',severidade=?,motivo=?,adiado_ate=null,
                        resolvido_em=null,detectado_em=?,atualizado_em=?,versao=versao+1
                        where id=? and empresa_id=?
                        """, fact.severity, fact.reason, now, now, current.id, tenant);
                event(tenant, current.id, current.state, "OPEN", null,
                        current.state.equals("RESOLVED") ? "Condição retornou" : "Adiamento expirou");
            } else if (current.state.equals("SNOOZED") && fact.severity.equals("CRITICA")) {
                jdbc.update("""
                        update patio_alertas set estado='OPEN',severidade=?,motivo=?,adiado_ate=null,
                        atualizado_em=?,versao=versao+1 where id=? and empresa_id=?
                        """, fact.severity, fact.reason, now, current.id, tenant);
                event(tenant, current.id, "SNOOZED", "OPEN", null, "Severidade crítica encerrou adiamento");
            } else if (!current.severity.equals(fact.severity) || !current.reason.equals(fact.reason)) {
                jdbc.update("""
                        update patio_alertas set severidade=?,motivo=?,atualizado_em=?,versao=versao+1
                        where id=? and empresa_id=?
                        """, fact.severity, fact.reason, now, current.id, tenant);
                event(tenant, current.id, current.state, current.state, null, "Severidade ou condição atualizada");
            }
        }
        for (AlertRow current : all) {
            if (!activeKeys.contains(current.key) && !current.state.equals("RESOLVED")) {
                jdbc.update("""
                        update patio_alertas set estado='RESOLVED',adiado_ate=null,resolvido_em=?,
                        atualizado_em=?,versao=versao+1 where id=? and empresa_id=?
                        """, now, now, current.id, tenant);
                event(tenant, current.id, current.state, "RESOLVED", null, "Condição de origem deixou de existir");
            }
        }
    }

    private List<Condition> conditions(Long tenant) {
        return jdbc.query("""
                with os_ativas as (
                    select o.id,o.data_promessa,o.mecanico_responsavel_id,s.codigo
                    from ordens_servico o left join status_os s on s.id=o.status_id and s.empresa_id=o.empresa_id
                    where o.empresa_id=? and o.tipo_os not in ('ORCAMENTO','VENDA_PRODUTO')
                      and coalesce(s.finaliza_os,false)=false and coalesce(s.cancela_os,false)=false
                )
                select id,'ATRASO' as tipo,
                    case when data_promessa < current_timestamp - interval '1 day' then 'CRITICA' else 'ALTA' end as severidade,
                    'Prazo de entrega ultrapassado' as motivo from os_ativas where data_promessa<current_timestamp
                union all select id,'AGUARDANDO_APROVACAO','MEDIA','Aguardando aprovação do cliente'
                    from os_ativas where codigo='AGUARDANDO_APROVACAO'
                union all select id,'AGUARDANDO_PECAS','MEDIA','Aguardando peças'
                    from os_ativas where codigo='AGUARDANDO_PECAS'
                union all select id,'SEM_RESPONSAVEL','ALTA','OS em execução sem responsável'
                    from os_ativas where codigo='EM_EXECUCAO' and mecanico_responsavel_id is null
                """, (rs, row) -> new Condition(rs.getLong("id"), rs.getString("tipo"),
                rs.getString("severidade"), rs.getString("motivo")), tenant);
    }

    private AlertView get(Long tenant, Long id) {
        List<AlertView> result = jdbc.query("""
                select a.id,a.ordem_servico_id,o.numero_os,f.nome_completo as responsavel,
                       a.tipo,a.severidade,a.estado,a.motivo,
                       a.detectado_em,a.atualizado_em,a.adiado_ate,a.versao
                from patio_alertas a join ordens_servico o on o.id=a.ordem_servico_id and o.empresa_id=a.empresa_id
                left join funcionarios f on f.id=o.mecanico_responsavel_id and f.empresa_id=a.empresa_id
                where a.id=? and a.empresa_id=?
                """, (rs, row) -> alertView(rs), id, tenant);
        if (result.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alerta não encontrado");
        return result.get(0);
    }

    private Long actor(Long tenant) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth == null ? java.util.Optional.<com.neritech.saas.gestaoUsuarios.domain.Usuario>empty()
                : users.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), tenant))
                .orElseThrow(() -> new AccessDeniedException("Autor inválido")).getId();
    }

    private void event(Long tenant, Long id, String before, String after, Long actor, String reason) {
        jdbc.update("""
                insert into patio_alerta_eventos
                (empresa_id,alerta_id,estado_antes,estado_depois,usuario_responsavel,motivo)
                values (?,?,?,?,?,?)
                """, tenant, id, before, after, actor, reason);
    }

    private static AlertRow alertRow(ResultSet rs) throws SQLException {
        var snooze = rs.getTimestamp("adiado_ate");
        return new AlertRow(rs.getLong("id"), rs.getLong("ordem_servico_id"), rs.getString("tipo"),
                rs.getString("dedupe_key"), rs.getString("severidade"), rs.getString("estado"),
                rs.getString("motivo"), snooze == null ? null : snooze.toLocalDateTime(), rs.getInt("versao"));
    }

    private static AlertView alertView(ResultSet rs) throws SQLException {
        var snooze = rs.getTimestamp("adiado_ate");
        return new AlertView(rs.getLong("id"), rs.getLong("ordem_servico_id"), rs.getString("numero_os"),
                rs.getString("responsavel"),
                rs.getString("tipo"), rs.getString("severidade"), rs.getString("estado"),
                rs.getString("motivo"), rs.getTimestamp("detectado_em").toLocalDateTime(),
                rs.getTimestamp("atualizado_em").toLocalDateTime(),
                snooze == null ? null : snooze.toLocalDateTime(), rs.getInt("versao"));
    }

    private record Condition(Long osId, String type, String severity, String reason) {
        String key() { return osId + ":" + type; }
    }
    private record AlertRow(Long id, Long osId, String type, String key, String severity,
            String state, String reason, LocalDateTime snoozeUntil, Integer version) {}
    public record AlertView(Long id, Long osId, String numeroOS, String responsavel,
            String tipo, String severidade,
            String estado, String motivo, LocalDateTime detectadoEm, LocalDateTime atualizadoEm,
            LocalDateTime adiadoAte, Integer versao) {}
    public record AlertEvent(Long id, String estadoAntes, String estadoDepois, Long usuarioId,
            String motivo, LocalDateTime criadoEm) {}
    public record AlertBoard(List<AlertView> alertas, long count, int page, int size,
            LocalDateTime updatedAt) {}
}
