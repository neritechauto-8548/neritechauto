package com.neritech.saas.ordemservico.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.ordemservico.repository.ItemOSServicoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PatioAdjustmentService {
    private final JdbcTemplate jdbc;
    private final ItemOSServicoRepository items;
    private final UsuarioRepository users;
    private final ObjectMapper json;

    public PatioAdjustmentService(JdbcTemplate jdbc, ItemOSServicoRepository items,
            UsuarioRepository users, ObjectMapper json) {
        this.jdbc = jdbc;
        this.items = items;
        this.users = users;
        this.json = json;
    }

    @Transactional(readOnly = true)
    public List<AdjustmentView> history(Long itemId) {
        Long tenant = TenantAccess.requireCurrent();
        items.findByIdAndOrdemServicoEmpresaId(itemId, tenant)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço da OS não encontrado"));
        return jdbc.query("""
                select id, item_os_servico_id, tempo_execucao_id, delta_minutos, motivo,
                       usuario_responsavel, criado_em
                from patio_ajustes_tempo where empresa_id=? and item_os_servico_id=?
                order by criado_em desc, id desc
                """, (rs, row) -> new AdjustmentView(rs.getLong("id"), rs.getLong("item_os_servico_id"),
                rs.getLong("tempo_execucao_id"), rs.getInt("delta_minutos"), rs.getString("motivo"),
                rs.getLong("usuario_responsavel"), rs.getTimestamp("criado_em").toLocalDateTime()), tenant, itemId);
    }

    @Transactional
    public AdjustmentView adjust(Long itemId, Long timeId, Integer deltaMinutes, String reason,
            Integer expectedVersion, UUID key) {
        Long tenant = TenantAccess.requireCurrent();
        if (deltaMinutes == null || deltaMinutes == 0 || deltaMinutes < -1440 || deltaMinutes > 1440
                || reason == null || reason.strip().length() < 10 || reason.length() > 500) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Ajuste e motivo inválidos");
        }
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Long actorId = (auth == null ? java.util.Optional.<com.neritech.saas.gestaoUsuarios.domain.Usuario>empty()
                : users.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), tenant))
                .orElseThrow(() -> new AccessDeniedException("Autor inválido")).getId();
        var item = items.lockForPatio(itemId, tenant)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço da OS não encontrado"));
        List<AdjustmentView> previous = jdbc.query("""
                select id, item_os_servico_id, tempo_execucao_id, delta_minutos, motivo,
                       usuario_responsavel, criado_em from patio_ajustes_tempo
                where empresa_id=? and chave=?::uuid
                """, (rs, row) -> new AdjustmentView(rs.getLong("id"), rs.getLong("item_os_servico_id"),
                rs.getLong("tempo_execucao_id"), rs.getInt("delta_minutos"), rs.getString("motivo"),
                rs.getLong("usuario_responsavel"), rs.getTimestamp("criado_em").toLocalDateTime()),
                tenant, key.toString());
        if (!previous.isEmpty()) {
            AdjustmentView existing = previous.get(0);
            if (!Objects.equals(existing.itemId(), itemId) || !Objects.equals(existing.tempoId(), timeId)
                    || !Objects.equals(existing.usuarioId(), actorId) || existing.deltaMinutos() != deltaMinutes
                    || !existing.motivo().equals(reason.strip())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Chave usada para outro ajuste");
            }
            return existing;
        }
        if (!Objects.equals(item.getVersao(), expectedVersion)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Serviço alterado por outro operador");
        }
        Boolean validEntry = jdbc.queryForObject("""
                select exists (select 1 from tempos_execucao
                where id=? and empresa_id=? and item_os_servico_id=? and estado='FINALIZADO')
                """, Boolean.class, timeId, tenant, itemId);
        if (!Boolean.TRUE.equals(validEntry)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Apontamento finalizado não encontrado");
        }
        int before = item.getTempoExecucaoReal() == null ? 0 : item.getTempoExecucaoReal();
        int after;
        try {
            after = Math.addExact(before, deltaMinutes);
        } catch (ArithmeticException ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Ajuste excede o limite de tempo do serviço", ex);
        }
        if (after < 0) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                "Ajuste tornaria o tempo do serviço negativo");
        Long id = jdbc.queryForObject("""
                insert into patio_ajustes_tempo (empresa_id,item_os_servico_id,tempo_execucao_id,
                chave,delta_minutos,motivo,usuario_responsavel,versao_item_antes)
                values (?,?,?,?::uuid,?,?,?,?) returning id
                """, Long.class, tenant, itemId, timeId, key.toString(), deltaMinutes,
                reason.strip(), actorId, item.getVersao());
        item.setTempoExecucaoReal(after);
        items.saveAndFlush(item);
        try {
            jdbc.update("""
                    insert into logs_alteracoes (empresa_id,tabela_afetada,registro_id,operacao,
                    campos_alterados,valores_antigos,valores_novos,usuario_responsavel,
                    motivo_alteracao,contexto_operacao,data_alteracao)
                    values (?,'itens_os_servicos',?,'UPDATE',?,?,?,?,?,'PATIO-001',current_timestamp)
                    """, tenant, itemId, "[\"tempo_execucao_real\"]",
                    json.writeValueAsString(Map.of("tempoExecucaoReal", before)),
                    json.writeValueAsString(Map.of("tempoExecucaoReal", after, "ajusteId", id)),
                    actorId, reason.strip());
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Falha ao auditar ajuste", ex);
        }
        return new AdjustmentView(id, itemId, timeId, deltaMinutes, reason.strip(), actorId, LocalDateTime.now());
    }

    public record AdjustmentView(Long id, Long itemId, Long tempoId, int deltaMinutos,
            String motivo, Long usuarioId, LocalDateTime criadoEm) {}
}
