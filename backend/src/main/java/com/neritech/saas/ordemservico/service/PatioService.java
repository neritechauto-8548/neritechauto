package com.neritech.saas.ordemservico.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.StatusOS;
import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.domain.enums.StatusExecucao;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.ordemservico.repository.StatusOSRepository;
import com.neritech.saas.ordemservico.repository.ItemOSServicoRepository;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PatioService {
    private static final String FROM = """
            from ordens_servico o
            left join status_os s on s.id=o.status_id and s.empresa_id=o.empresa_id
            left join status_os initial_status on initial_status.empresa_id=o.empresa_id
                and initial_status.codigo='ABERTA' and o.status_id is null
            left join cliente c on c.id=o.cliente_id and c.empresa_id=o.empresa_id
            left join veiculos v on v.id=o.veiculo_id and v.empresa_id=o.empresa_id
            left join modelos_veiculos m on m.id=v.modelo_id and m.empresa_id=o.empresa_id
            left join funcionarios f on f.id=o.mecanico_responsavel_id and f.empresa_id=o.empresa_id
            left join lateral (select r.recurso_id, ar.nome_recurso
                from agd_reservas_recursos_os r
                join agd_recursos_agenda ar on ar.id=r.recurso_id and ar.empresa_id=r.empresa_id
                where r.empresa_id=o.empresa_id and r.ordem_servico_id=o.id and r.estado='OCUPADO'
                order by r.atualizado_em desc, r.id desc limit 1) occupied_box on true
            left join lateral (select count(*) as total,
                count(*) filter (where i.status_execucao='CONCLUIDO') as concluidos,
                sum(coalesce(i.tempo_execucao_previsto,0)) as previsto,
                sum(coalesce(i.tempo_execucao_real,0)) as trabalhado
                from itens_os_servicos i where i.ordem_servico_id=o.id) items on true
            """;
    private final JdbcTemplate jdbc;
    private final OrdemServicoRepository orders;
    private final StatusOSRepository statuses;
    private final UsuarioRepository users;
    private final ItemOSServicoRepository items;
    private final FuncionarioRepository employees;
    private final ObjectMapper json;

    public PatioService(JdbcTemplate jdbc, OrdemServicoRepository orders, StatusOSRepository statuses,
            UsuarioRepository users, ItemOSServicoRepository items, FuncionarioRepository employees,
            ObjectMapper json) {
        this.jdbc=jdbc; this.orders=orders; this.statuses=statuses; this.users=users;
        this.items=items; this.employees=employees; this.json=json;
    }

    @Transactional(readOnly = true)
    public Board board(String search, Long statusId, Long responsavelId, String prioridade,
            Boolean atrasado, int page, int size) {
        Long tenant = TenantAccess.requireCurrent();
        if (page < 0 || !List.of(20, 50, 100).contains(size) || search.length() > 100) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Filtros ou paginação inválidos");
        }
        String term = search.trim().toLowerCase(java.util.Locale.ROOT);
        String pattern = "%" + term + "%";
        var auth = SecurityContextHolder.getContext().getAuthentication();
        boolean canReadClient = auth != null && auth.getAuthorities().stream().anyMatch(a ->
                a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("GERAL_USUARIO"));
        String priority = prioridade == null ? "" : prioridade.trim().toUpperCase(java.util.Locale.ROOT);
        if (!priority.isEmpty() && !List.of("BAIXA", "NORMAL", "ALTA", "URGENTE").contains(priority)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Prioridade inválida");
        }
        StringBuilder where = new StringBuilder(" where o.empresa_id=? and o.tipo_os not in ('ORCAMENTO','VENDA_PRODUTO') " +
                "and coalesce(s.finaliza_os,false)=false and coalesce(s.cancela_os,false)=false");
        java.util.List<Object> args = new java.util.ArrayList<>();
        args.add(tenant);
        if (!term.isEmpty()) {
            where.append(" and (lower(o.numero_os) like ? or lower(coalesce(v.placa,'')) like ?");
            args.add(pattern); args.add(pattern);
            if (canReadClient) {
                where.append(" or lower(coalesce(c.nome_completo,'')) like ?");
                args.add(pattern);
            }
            where.append(')');
        }
        if (statusId != null) { where.append(" and coalesce(o.status_id,initial_status.id)=?"); args.add(statusId); }
        if (responsavelId != null) { where.append(" and o.mecanico_responsavel_id=?"); args.add(responsavelId); }
        if (!priority.isEmpty()) { where.append(" and o.prioridade=?"); args.add(priority); }
        if (atrasado != null) {
            where.append(atrasado ? " and o.data_promessa < current_timestamp" :
                    " and (o.data_promessa is null or o.data_promessa >= current_timestamp)");
        }
        Long total = jdbc.queryForObject("select count(*) " + FROM + where, Long.class, args.toArray());
        List<Card> cards = jdbc.query("""
                select o.id, o.numero_os, o.versao, coalesce(o.status_id,initial_status.id) as status_id,
                       coalesce(s.nome,initial_status.nome) as status_nome,
                       coalesce(s.codigo,initial_status.codigo) as status_codigo,
                       coalesce(s.cor_identificacao,initial_status.cor_identificacao) as status_cor,
                       v.placa, m.nome as modelo, case when ? then c.nome_completo else null end as cliente,
                       f.nome_completo as responsavel, o.mecanico_responsavel_id,
                       occupied_box.recurso_id as box_id, occupied_box.nome_recurso as box_nome,
                       o.prioridade, o.data_abertura, o.data_promessa, o.data_atualizacao,
                       coalesce(items.total,0) as servicos_total,
                       coalesce(items.concluidos,0) as servicos_concluidos,
                       coalesce(items.previsto,0) as minutos_previstos,
                       coalesce(items.trabalhado,0) as minutos_trabalhados
                """ + FROM + where + "\n" + """
                order by coalesce(o.data_atualizacao,o.data_abertura,o.data_cadastro) desc, o.id desc
                limit ? offset ?
                """, (rs, row) -> card(rs), append(new Object[]{canReadClient}, append(args.toArray(), size, (long) page * size)));
        List<Column> columns = new java.util.ArrayList<>(jdbc.query("""
                select id, nome, codigo, cor_identificacao, ordem_exibicao from status_os
                where empresa_id=? and (ativo=true or id in
                    (select distinct status_id from ordens_servico where empresa_id=?))
                  and coalesce(finaliza_os,false)=false
                  and coalesce(cancela_os,false)=false order by ordem_exibicao, id
                """, (rs, row) -> new Column(rs.getLong("id"), rs.getString("nome"),
                    rs.getString("codigo"), rs.getString("cor_identificacao")), tenant, tenant));
        if (cards.stream().anyMatch(card -> card.statusId() == null)) {
            columns.add(0, new Column(0L, "Sem etapa", "SEM_ETAPA", "#64748b"));
        }
        return new Board(cards, columns, total == null ? 0 : total, page, size, LocalDateTime.now());
    }

    private static Object[] append(Object[] args, Object... extra) {
        Object[] result = java.util.Arrays.copyOf(args, args.length + extra.length);
        System.arraycopy(extra, 0, result, args.length, extra.length);
        return result;
    }

    private static Card card(ResultSet rs) throws SQLException {
        var promise = rs.getTimestamp("data_promessa");
        return new Card(rs.getLong("id"), rs.getString("numero_os"), rs.getInt("versao"),
                rs.getObject("status_id", Long.class), rs.getString("status_nome"),
                rs.getString("status_codigo"), rs.getString("status_cor"), rs.getString("placa"),
                rs.getString("modelo"), rs.getString("cliente"), rs.getString("responsavel"),
                rs.getObject("mecanico_responsavel_id", Long.class),
                rs.getObject("box_id", Long.class), rs.getString("box_nome"), rs.getString("prioridade"),
                promise == null ? null : promise.toLocalDateTime(),
                promise != null && promise.toLocalDateTime().isBefore(LocalDateTime.now()),
                rs.getInt("servicos_total"), rs.getInt("servicos_concluidos"),
                rs.getInt("minutos_previstos"), rs.getInt("minutos_trabalhados"));
    }

    @Transactional(readOnly = true)
    public TvBoard tv() {
        Long tenant = TenantAccess.requireCurrent();
        List<StageCount> stages = jdbc.query("""
                select coalesce(s.nome,'Entrada') as etapa, count(*) as total,
                       count(*) filter (where o.data_promessa < current_timestamp) as atrasadas
                from ordens_servico o
                left join status_os s on s.id=o.status_id and s.empresa_id=o.empresa_id
                where o.empresa_id=? and o.tipo_os not in ('ORCAMENTO','VENDA_PRODUTO')
                  and coalesce(s.finaliza_os,false)=false and coalesce(s.cancela_os,false)=false
                group by coalesce(s.nome,'Entrada'), coalesce(s.ordem_exibicao,0)
                order by coalesce(s.ordem_exibicao,0)
                """, (rs, row) -> new StageCount(rs.getString("etapa"), rs.getLong("total"),
                    rs.getLong("atrasadas")), tenant);
        return new TvBoard(stages, stages.stream().mapToLong(StageCount::total).sum(), LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<Resource> resources() {
        Long tenant = TenantAccess.requireCurrent();
        return jdbc.query("""
                select id, nome_recurso, tipo_recurso, capacidade_simultanea, localizacao, disponivel
                from agd_recursos_agenda where empresa_id=? and tipo_recurso in ('BAIA','ELEVADOR')
                order by nome_recurso, id
                """, (rs, row) -> new Resource(rs.getLong("id"), rs.getString("nome_recurso"),
                    rs.getString("tipo_recurso"), rs.getInt("capacidade_simultanea"),
                    rs.getString("localizacao"), rs.getBoolean("disponivel")), tenant);
    }

    @Transactional(readOnly = true)
    public List<TeamMember> team() {
        Long tenant = TenantAccess.requireCurrent();
        return jdbc.query("""
                select id, nome_completo from funcionarios where empresa_id=? and status='ATIVO'
                order by nome_completo, id
                """, (rs, row) -> new TeamMember(rs.getLong("id"), rs.getString("nome_completo")), tenant);
    }

    @Transactional
    public Assignment assign(Long itemId, Long funcionarioId, Integer expectedVersion) {
        Long tenant = TenantAccess.requireCurrent();
        ItemOSServico item = items.lockForPatio(itemId, tenant)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço da OS não encontrado"));
        if (item.getOrdemServico().getTipoOS() == TipoOS.ORCAMENTO ||
                item.getOrdemServico().getTipoOS() == TipoOS.VENDA_PRODUTO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Tipo de OS fora do Pátio");
        }
        if (!Objects.equals(item.getVersao(), expectedVersion)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Serviço alterado por outro operador");
        }
        var employee = employees.findByIdAndEmpresaId(funcionarioId, tenant)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Funcionário inválido"));
        if (employee.getStatus() != com.neritech.saas.rh.domain.enums.StatusFuncionario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Funcionário não está ativo");
        }
        if (item.getStatusExecucao() == StatusExecucao.CONCLUIDO || item.getStatusExecucao() == StatusExecucao.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Serviço concluído ou cancelado");
        }
        Boolean hasOpenTimer = jdbc.queryForObject("""
                select exists (select 1 from tempos_execucao
                where empresa_id=? and item_os_servico_id=? and estado in ('EM_EXECUCAO','PAUSADO'))
                """, Boolean.class, tenant, itemId);
        if (Boolean.TRUE.equals(hasOpenTimer)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Pause e conclua o apontamento antes de trocar o responsável");
        }
        StatusOS osStatus = item.getOrdemServico().getStatus();
        if (osStatus != null && (Boolean.TRUE.equals(osStatus.getFinalizaOS()) || Boolean.TRUE.equals(osStatus.getCancelaOS()))) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "OS encerrada");
        }
        Long previous = item.getMecanicoExecutorId();
        if (Objects.equals(previous, funcionarioId)) return new Assignment(itemId, funcionarioId, item.getVersao());
        item.setMecanicoExecutorId(funcionarioId);
        items.saveAndFlush(item);
        Map<String, Object> prior = new java.util.LinkedHashMap<>();
        prior.put("funcionarioId", previous);
        audit(tenant, "itens_os_servicos", itemId, prior,
                Map.of("funcionarioId", funcionarioId), "Atribuição no Pátio");
        return new Assignment(itemId, funcionarioId, item.getVersao());
    }

    @Transactional
    public MoveResult move(Long osId, Long statusId, Integer expectedVersion, String motivo) {
        Long tenant = TenantAccess.requireCurrent();
        OrdemServico order = orders.lockForPatio(osId, tenant)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OS não encontrada"));
        if (order.getTipoOS() == TipoOS.ORCAMENTO || order.getTipoOS() == TipoOS.VENDA_PRODUTO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Tipo de OS fora do Pátio");
        }
        if (!Objects.equals(order.getVersao(), expectedVersion)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "OS alterada por outro operador");
        }
        StatusOS current = order.getStatus();
        StatusOS target = statuses.findByIdAndEmpresaId(statusId, tenant)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Etapa inválida"));
        String from = current == null ? "ABERTA" : current.getCodigo();
        String to = target.getCodigo();
        if (!Boolean.TRUE.equals(target.getAtivo()) || Boolean.TRUE.equals(target.getFinalizaOS())
                || Boolean.TRUE.equals(target.getCancelaOS()) || (current != null &&
                (Boolean.TRUE.equals(current.getFinalizaOS()) || Boolean.TRUE.equals(current.getCancelaOS())))) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Estado da OS não permite movimento no Pátio");
        }
        // A aprovação, a execução e a conclusão pertencem aos fluxos proprietários da OS.
        boolean allowed = (from.equals("ABERTA") && to.equals("DIAGNOSTICO"))
                || (from.equals("DIAGNOSTICO") && to.equals("AGUARDANDO_APROVACAO"));
        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Transição indisponível no Pátio; use o fluxo da OS");
        }
        order.setStatus(target);
        orders.saveAndFlush(order);
        Map<String, Object> prior = new java.util.LinkedHashMap<>();
        prior.put("statusId", current == null ? null : current.getId());
        prior.put("codigo", current == null ? null : from);
        audit(tenant, "ordens_servico", osId, prior,
                Map.of("statusId", target.getId(), "codigo", to), motivo);
        return new MoveResult(osId, target.getId(), to, order.getVersao());
    }

    private void audit(Long tenant, String table, Long id, Map<String, ?> before,
            Map<String, ?> after, String reason) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var actor = auth == null ? java.util.Optional.<com.neritech.saas.gestaoUsuarios.domain.Usuario>empty()
                : users.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), tenant);
        if (actor.isEmpty()) throw new AccessDeniedException("Autor inválido");
        try {
            jdbc.update("""
                    insert into logs_alteracoes (empresa_id,tabela_afetada,registro_id,operacao,
                    campos_alterados,valores_antigos,valores_novos,usuario_responsavel,
                    motivo_alteracao,contexto_operacao,data_alteracao)
                    values (?,?,?,'UPDATE',?,?,?,?,?,'PATIO-001',current_timestamp)
                    """, tenant, table, id, json.writeValueAsString(after.keySet()),
                    json.writeValueAsString(before), json.writeValueAsString(after),
                    actor.get().getId(), reason == null ? null : reason.strip());
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Falha ao auditar operação do Pátio", ex);
        }
    }

    public record Column(Long id, String nome, String codigo, String cor) {}
    public record Card(Long osId, String numeroOS, Integer versao, Long statusId,
            String statusNome, String statusCodigo, String statusCor, String placa, String modelo,
            String cliente, String responsavel, Long responsavelId, Long boxId, String boxNome,
            String prioridade,
            LocalDateTime previsaoEntrega, boolean atrasado, int servicosTotal,
            int servicosConcluidos, int minutosPrevistos, int minutosTrabalhados) {}
    public record Board(List<Card> cards, List<Column> columns, long count, int page, int size,
            LocalDateTime updatedAt) {}
    public record StageCount(String etapa, long total, long atrasadas) {}
    public record TvBoard(List<StageCount> stages, long count, LocalDateTime updatedAt) {}
    public record Resource(Long id, String nome, String tipo, int capacidade, String localizacao,
            boolean habilitado) {}
    public record TeamMember(Long id, String nome) {}
    public record Assignment(Long itemId, Long funcionarioId, Integer versao) {}
    public record MoveResult(Long osId, Long statusId, String statusCodigo, Integer versao) {}
}
