package com.neritech.saas.agendamento.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/** Reserva de OS em recursos da Agenda; todas as mutações serializam na linha do recurso. */
@Service
public class ReservaRecursoOsService {
    private final JdbcTemplate jdbc;
    private final OrdemServicoRepository orders;
    private final UsuarioRepository users;
    private final ObjectMapper json;

    public ReservaRecursoOsService(JdbcTemplate jdbc, OrdemServicoRepository orders,
            UsuarioRepository users, ObjectMapper json) {
        this.jdbc = jdbc;
        this.orders = orders;
        this.users = users;
        this.json = json;
    }

    @Transactional(readOnly = true)
    public ReservationBoard list(LocalDate day) {
        Long tenant = TenantAccess.requireCurrent();
        if (day == null) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data obrigatória");
        List<Reservation> reservations = jdbc.query("""
                select r.id,r.recurso_id,r.ordem_servico_id,o.numero_os,r.inicio,r.fim,r.estado,r.versao
                from agd_reservas_recursos_os r
                join ordens_servico o on o.id=r.ordem_servico_id and o.empresa_id=r.empresa_id
                where r.empresa_id=? and r.estado in ('RESERVADO','OCUPADO')
                  and (r.estado='OCUPADO' or r.inicio<? and r.fim>?)
                order by r.inicio,r.id
                """, (rs, row) -> reservation(rs), tenant, day.plusDays(1).atStartOfDay(), day.atStartOfDay());
        Boolean unknown = jdbc.queryForObject("""
                select exists (select 1 from agd_agendamentos a
                where a.empresa_id=? and a.data_agendamento=? and a.status in ('AGENDADO','CONFIRMADO','EM_ANDAMENTO')
                  and nullif(trim(a.recursos_necessarios),'') is not null
                  and trim(a.recursos_necessarios) not in ('[]','{}'))
                """, Boolean.class, tenant, day);
        return new ReservationBoard(reservations, Boolean.TRUE.equals(unknown), day);
    }

    @Transactional
    public Reservation reserve(Long resourceId, Long osId, LocalDateTime start, LocalDateTime end, UUID key) {
        Long tenant = TenantAccess.requireCurrent();
        if (start == null || end == null || !end.isAfter(start) || start.isBefore(LocalDateTime.now())
                || !start.toLocalDate().equals(end.toLocalDate())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Reserva deve ter horário futuro e terminar no mesmo dia");
        }
        Long actor = actor(tenant);
        // Agenda and Pátio use the same tenant lock before checking either kind of booking.
        lockTenantCalendar(tenant);
        Resource resource = lockResource(tenant, resourceId);
        if (!resource.available || resource.capacity < 1) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Recurso indisponível");
        }
        List<Reservation> replay = jdbc.query("""
                select r.id,r.recurso_id,r.ordem_servico_id,o.numero_os,r.inicio,r.fim,r.estado,r.versao
                from agd_reservas_recursos_os r
                join ordens_servico o on o.id=r.ordem_servico_id and o.empresa_id=r.empresa_id
                where r.empresa_id=? and r.chave=?::uuid
                """, (rs, row) -> reservation(rs), tenant, key.toString());
        if (!replay.isEmpty()) {
            Reservation existing = replay.get(0);
            Long creator = jdbc.queryForObject("""
                    select criado_por from agd_reservas_recursos_os where id=? and empresa_id=?
                    """, Long.class, existing.id(), tenant);
            if (!Objects.equals(existing.recursoId(), resourceId) || !Objects.equals(existing.osId(), osId)
                    || !existing.inicio().equals(start) || !existing.fim().equals(end)
                    || !Objects.equals(creator, actor)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Chave usada para outra reserva");
            }
            return existing;
        }
        var order = orders.lockForPatio(osId, tenant)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OS não encontrada"));
        if (order.getTipoOS() == TipoOS.ORCAMENTO || order.getTipoOS() == TipoOS.VENDA_PRODUTO
                || order.getStatus() != null && (Boolean.TRUE.equals(order.getStatus().getFinalizaOS())
                    || Boolean.TRUE.equals(order.getStatus().getCancelaOS()))) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "OS não aceita reserva de Pátio");
        }
        if (legacyConflict(tenant, start, end)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Agendamento legado com recursos não normalizados neste horário; confira na Agenda");
        }
        Long duplicate = jdbc.queryForObject("""
                select count(*) from agd_reservas_recursos_os
                where empresa_id=? and recurso_id=? and ordem_servico_id=?
                  and estado in ('RESERVADO','OCUPADO') and (estado='OCUPADO' or inicio<? and fim>?)
                """, Long.class, tenant, resourceId, osId, end, start);
        if (duplicate != null && duplicate > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "OS já ocupa ou reservou este recurso");
        }
        assertCapacity(tenant, resource, start, end, null);
        Long id;
        try {
            id = jdbc.queryForObject("""
                    insert into agd_reservas_recursos_os
                    (empresa_id,recurso_id,ordem_servico_id,chave,inicio,fim,estado,criado_por,atualizado_por)
                    values (?,?,?,?::uuid,?,?,'RESERVADO',?,?) returning id
                    """, Long.class, tenant, resourceId, osId, key.toString(), start, end, actor, actor);
        } catch (DuplicateKeyException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chave de reserva já utilizada", ex);
        }
        audit(tenant, id, actor, "INSERT", "NOVO", "RESERVADO", "Reserva de recurso da OS");
        return new Reservation(id, resourceId, osId, order.getNumeroOS(), start, end, "RESERVADO", 0);
    }

    @Transactional
    public Reservation transition(Long reservationId, String action, Integer expectedVersion) {
        Long tenant = TenantAccess.requireCurrent();
        if (!List.of("occupy", "release", "cancel").contains(action)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Ação de recurso inválida");
        }
        Long actor = actor(tenant);
        List<Long> resourceIds = jdbc.query("""
                select recurso_id from agd_reservas_recursos_os where id=? and empresa_id=?
                """, (rs, row) -> rs.getLong("recurso_id"), reservationId, tenant);
        if (resourceIds.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada");
        Long resourceId = resourceIds.get(0);
        Resource resource = lockResource(tenant, resourceId);
        List<Reservation> locked = jdbc.query("""
                select r.id,r.recurso_id,r.ordem_servico_id,o.numero_os,r.inicio,r.fim,r.estado,r.versao
                from agd_reservas_recursos_os r
                join ordens_servico o on o.id=r.ordem_servico_id and o.empresa_id=r.empresa_id
                where r.id=? and r.empresa_id=? for update of r
                """, (rs, row) -> reservation(rs), reservationId, tenant);
        if (locked.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada");
        Reservation before = locked.get(0);
        if (!Objects.equals(before.versao(), expectedVersion)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Reserva alterada por outro operador");
        }
        String next = switch (action) {
            case "occupy" -> "OCUPADO";
            case "release" -> "LIBERADO";
            default -> "CANCELADO";
        };
        if (!(before.estado().equals("RESERVADO") && List.of("OCUPADO", "CANCELADO").contains(next)
                || before.estado().equals("OCUPADO") && next.equals("LIBERADO"))) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Transição de reserva inválida");
        }
        if (next.equals("OCUPADO")) {
            var order = orders.lockForPatio(before.osId(), tenant)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OS não encontrada"));
            if (order.getStatus() != null && (Boolean.TRUE.equals(order.getStatus().getFinalizaOS())
                    || Boolean.TRUE.equals(order.getStatus().getCancelaOS()))) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "OS encerrada");
            }
            if (!resource.available || LocalDateTime.now().isBefore(before.inicio())
                    || !LocalDateTime.now().isBefore(before.fim())) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Recurso fora do intervalo disponível");
            }
            assertCapacity(tenant, resource, before.inicio(), before.fim(), before.id());
        }
        int changed = jdbc.update("""
                update agd_reservas_recursos_os set estado=?,versao=versao+1,
                    atualizado_por=?,atualizado_em=current_timestamp
                where id=? and empresa_id=? and versao=?
                """, next, actor, reservationId, tenant, expectedVersion);
        if (changed != 1) throw new ResponseStatusException(HttpStatus.CONFLICT, "Reserva alterada por outro operador");
        audit(tenant, reservationId, actor, "UPDATE", before.estado(), next,
                "Recurso " + action + " na OS " + before.numeroOS());
        return new Reservation(before.id(), before.recursoId(), before.osId(), before.numeroOS(),
                before.inicio(), before.fim(), next, before.versao() + 1);
    }

    private Resource lockResource(Long tenant, Long resourceId) {
        List<Resource> result = jdbc.query("""
                select id,capacidade_simultanea,disponivel from agd_recursos_agenda
                where id=? and empresa_id=? and tipo_recurso in ('BAIA','ELEVADOR') for update
                """, (rs, row) -> new Resource(rs.getLong("id"), rs.getInt("capacidade_simultanea"),
                rs.getBoolean("disponivel")), resourceId, tenant);
        if (result.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recurso não encontrado");
        return result.get(0);
    }

    private void lockTenantCalendar(Long tenant) {
        jdbc.queryForObject("select id from empresa where id=? for update", Long.class, tenant);
    }

    private boolean legacyConflict(Long tenant, LocalDateTime start, LocalDateTime end) {
        Boolean unknown = jdbc.queryForObject("""
                select exists (select 1 from agd_agendamentos a
                where a.empresa_id=? and a.data_agendamento=?
                  and a.hora_inicio<? and a.hora_fim>?
                  and a.status in ('AGENDADO','CONFIRMADO','EM_ANDAMENTO')
                  and nullif(trim(a.recursos_necessarios),'') is not null
                  and trim(a.recursos_necessarios) not in ('[]','{}'))
                """, Boolean.class, tenant, start.toLocalDate(), end.toLocalTime(), start.toLocalTime());
        return Boolean.TRUE.equals(unknown);
    }

    private void assertCapacity(Long tenant, Resource resource, LocalDateTime start,
            LocalDateTime end, Long excludedId) {
        Long used = jdbc.queryForObject("""
                select count(*) from agd_reservas_recursos_os
                where empresa_id=? and recurso_id=? and estado in ('RESERVADO','OCUPADO')
                  and (estado='OCUPADO' or inicio<? and fim>?) and id<>?
                """, Long.class, tenant, resource.id, end, start, excludedId == null ? -1L : excludedId);
        if (used != null && used >= resource.capacity) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Capacidade do recurso esgotada");
        }
    }

    private Long actor(Long tenant) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth == null ? java.util.Optional.<com.neritech.saas.gestaoUsuarios.domain.Usuario>empty()
                : users.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), tenant))
                .orElseThrow(() -> new AccessDeniedException("Autor inválido")).getId();
    }

    private void audit(Long tenant, Long id, Long actor, String operation, String before,
            String after, String reason) {
        try {
            jdbc.update("""
                    insert into logs_alteracoes (empresa_id,tabela_afetada,registro_id,operacao,
                    campos_alterados,valores_antigos,valores_novos,usuario_responsavel,
                    motivo_alteracao,contexto_operacao,data_alteracao)
                    values (?,'agd_reservas_recursos_os',?,?,?,?,?,?,?,'PATIO-001',current_timestamp)
                    """, tenant, id, operation, "[\"estado\"]",
                    json.writeValueAsString(Map.of("estado", before)),
                    json.writeValueAsString(Map.of("estado", after)), actor, reason);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Falha ao auditar reserva", ex);
        }
    }

    private static Reservation reservation(ResultSet rs) throws SQLException {
        return new Reservation(rs.getLong("id"), rs.getLong("recurso_id"), rs.getLong("ordem_servico_id"),
                rs.getString("numero_os"), rs.getTimestamp("inicio").toLocalDateTime(),
                rs.getTimestamp("fim").toLocalDateTime(), rs.getString("estado"), rs.getInt("versao"));
    }

    private record Resource(Long id, int capacity, boolean available) {}
    public record Reservation(Long id, Long recursoId, Long osId, String numeroOS,
            LocalDateTime inicio, LocalDateTime fim, String estado, Integer versao) {}
    public record ReservationBoard(List<Reservation> reservas, boolean legadoIndeterminado, LocalDate data) {}
}
