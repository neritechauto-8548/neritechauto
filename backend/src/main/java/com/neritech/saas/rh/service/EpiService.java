package com.neritech.saas.rh.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EpiService {
    private final JdbcTemplate jdbc;
    private final UsuarioRepository usuarios;
    private final ObjectMapper json;

    public record EpiRequest(
            @NotBlank @Size(max=160) String nome,
            @Size(max=32) String numeroCa,
            @Size(max=90) String categoria,
            @Size(max=120) String fabricante,
            @Size(max=40) String tamanho,
            @Size(max=4000) String especificacoes,
            @Size(max=4000) String descricao,
            Integer versao) {}

    public record EpiResponse(long id, String nome, String numeroCa, String categoria,
            String fabricante, String tamanho, String especificacoes, String descricao,
            boolean ativo, int versao) {}

    public record EntregaRequest(@NotNull Long epiId, @NotNull LocalDate dataEntrega,
            LocalDate dataValidade, @Size(max=2000) String observacao) {}

    public record EntregaResponse(long id, long funcionarioId, long epiId, String epiNome,
            String numeroCa, LocalDate dataEntrega, LocalDate dataValidade,
            String observacao, LocalDateTime registradoEm) {}

    private String safe(String value) { return value == null ? null : value.trim(); }
    private String searchPattern(String value) {
        return "%" + (value == null ? "" : value.trim().toLowerCase()
                .replace("!", "!!").replace("%", "!%").replace("_", "!_")) + "%";
    }
    private ResponseStatusException missing(String what) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, what + " não encontrado");
    }
    private static final org.springframework.jdbc.core.RowMapper<EpiResponse> EPI = (rs, i) ->
            new EpiResponse(rs.getLong("id"), rs.getString("nome"), rs.getString("numero_ca"),
                    rs.getString("categoria"), rs.getString("fabricante"), rs.getString("tamanho"),
                    rs.getString("especificacoes"), rs.getString("descricao"),
                    rs.getBoolean("ativo"), rs.getInt("versao"));

    @Transactional(readOnly = true)
    public Page<EpiResponse> listar(String pesquisa, int page, int size) {
        long tenant = TenantAccess.requireCurrent();
        if (page < 0 || !List.of(20, 50, 100).contains(size)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        }
        String q = searchPattern(pesquisa);
        String where = " from rh_epis where empresa_id=? and ativo=true and (lower(nome) like ? escape '!' or lower(coalesce(numero_ca,'')) like ? escape '!')";
        Long total = jdbc.queryForObject("select count(*)" + where, Long.class, tenant, q, q);
        List<EpiResponse> content = jdbc.query("select *" + where + " order by nome,id limit ? offset ?",
                EPI, tenant, q, q, size, page * size);
        return new PageImpl<>(content, PageRequest.of(page, size), total == null ? 0 : total);
    }

    @Transactional(readOnly = true)
    public EpiResponse buscar(long id) {
        long tenant = TenantAccess.requireCurrent();
        return jdbc.query("select * from rh_epis where id=? and empresa_id=?", EPI, id, tenant)
                .stream().findFirst().orElseThrow(() -> missing("EPI"));
    }

    private void validar(EpiRequest req) {
        if (req.nome() == null || req.nome().trim().length() < 2 || req.nome().length() > 160)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Nome do EPI inválido");
    }

    @Transactional
    public EpiResponse criar(EpiRequest req) {
        long tenant = TenantAccess.requireCurrent();
        validar(req);
        long autor = actor(tenant);
        try {
            Long id = jdbc.queryForObject("""
                insert into rh_epis (empresa_id,nome,numero_ca,categoria,fabricante,tamanho,especificacoes,descricao)
                values (?,?,?,?,?,?,?,?) returning id
                """, Long.class, tenant, safe(req.nome()), safe(req.numeroCa()), safe(req.categoria()),
                    safe(req.fabricante()), safe(req.tamanho()), safe(req.especificacoes()), safe(req.descricao()));
            if (id == null) throw new IllegalStateException("EPI não gerado");
            auditar(tenant, id, autor, "INSERT", Map.of("nome", req.nome().trim()));
            return buscar(id);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um EPI com este nome e CA na empresa", ex);
        }
    }

    @Transactional
    public EpiResponse atualizar(long id, EpiRequest req) {
        long tenant = TenantAccess.requireCurrent();
        validar(req);
        if (req.versao() == null) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Versão obrigatória");
        buscar(id);
        long autor = actor(tenant);
        try {
            int count = jdbc.update("""
                update rh_epis set nome=?, numero_ca=?,categoria=?,fabricante=?,tamanho=?,
                especificacoes=?,descricao=?,versao=versao+1,data_atualizacao=now()
                where id=? and empresa_id=? and versao=? and ativo=true
                """, safe(req.nome()), safe(req.numeroCa()), safe(req.categoria()),
                    safe(req.fabricante()), safe(req.tamanho()), safe(req.especificacoes()),
                    safe(req.descricao()), id, tenant, req.versao());
            if (count != 1) throw new ResponseStatusException(HttpStatus.CONFLICT, "EPI foi alterado ou inativado; recarregue");
            auditar(tenant, id, autor, "UPDATE", Map.of("nome", req.nome().trim()));
            return buscar(id);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um EPI com este nome e CA na empresa", ex);
        }
    }

    @Transactional
    public void inativar(long id) {
        long tenant = TenantAccess.requireCurrent();
        EpiResponse old = buscar(id);
        if (!old.ativo()) return;
        long autor = actor(tenant);
        int changed = jdbc.update("update rh_epis set ativo=false,versao=versao+1,data_atualizacao=now() where id=? and empresa_id=? and ativo=true",
                id, tenant);
        if (changed == 1) auditar(tenant, id, autor, "UPDATE", Map.of("ativo", false));
    }

    private void exigirFuncionario(long funcionarioId, long tenant) {
        Integer count = jdbc.queryForObject("select count(*) from funcionarios where id=? and empresa_id=?",
                Integer.class, funcionarioId, tenant);
        if (count == null || count == 0) throw missing("Funcionário");
    }

    @Transactional(readOnly = true)
    public List<EntregaResponse> entregas(long funcionarioId) {
        long tenant = TenantAccess.requireCurrent();
        exigirFuncionario(funcionarioId, tenant);
        return jdbc.query("""
                select ent.id,ent.funcionario_id,ent.epi_id,epi.nome,epi.numero_ca,
                    ent.data_entrega,ent.data_validade,ent.observacao,ent.data_cadastro
                from rh_epi_entregas ent join rh_epis epi on epi.id=ent.epi_id and epi.empresa_id=ent.empresa_id
                where ent.empresa_id=? and ent.funcionario_id=?
                order by ent.data_entrega desc,ent.id desc
                """, (rs, i) -> new EntregaResponse(rs.getLong(1), rs.getLong(2), rs.getLong(3),
                rs.getString(4), rs.getString(5), rs.getDate(6).toLocalDate(),
                rs.getDate(7) == null ? null : rs.getDate(7).toLocalDate(),
                rs.getString(8), rs.getTimestamp(9).toLocalDateTime()), tenant, funcionarioId);
    }

    @Transactional
    public EntregaResponse entregar(long funcionarioId, EntregaRequest req) {
        long tenant = TenantAccess.requireCurrent();
        exigirFuncionario(funcionarioId, tenant);
        EpiResponse epi = buscar(req.epiId());
        if (!epi.ativo()) throw new ResponseStatusException(HttpStatus.CONFLICT, "EPI inativo");
        if (req.dataEntrega().isAfter(LocalDate.now()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data futura não é uma entrega realizada");
        if (req.dataValidade() != null && req.dataValidade().isBefore(req.dataEntrega()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Validade não pode anteceder a entrega");
        long autor = actor(tenant);
        Long id = jdbc.queryForObject("""
            insert into rh_epi_entregas (empresa_id,funcionario_id,epi_id,data_entrega,data_validade,observacao,registrado_por)
            values (?,?,?,?,?,?,?) returning id
            """, Long.class, tenant, funcionarioId, req.epiId(), req.dataEntrega(),
                req.dataValidade(), safe(req.observacao()), autor);
        if (id == null) throw new IllegalStateException("Entrega não gerada");
        auditar(tenant, id, autor, "INSERT", Map.of("funcionarioId", funcionarioId, "epiId", req.epiId()));
        return entregas(funcionarioId).stream().filter(item -> item.id() == id).findFirst()
                .orElseThrow(() -> missing("Entrega"));
    }

    private long actor(long tenant) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth == null ? java.util.Optional.<com.neritech.saas.gestaoUsuarios.domain.Usuario>empty()
            : usuarios.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), tenant))
                .orElseThrow(() -> new org.springframework.security.access.AccessDeniedException("Autor não autorizado")).getId();
    }

    private void auditar(long tenant, long id, long actor, String action, Map<String, ?> fields) {
        try {
            jdbc.update("""
                insert into logs_alteracoes (empresa_id,tabela_afetada,registro_id,operacao,campos_alterados,
                valores_novos,usuario_responsavel,contexto_operacao,data_alteracao)
                values (?,?,?,?,?,?,?,?,now())
                """, tenant, fields.containsKey("epiId") ? "rh_epi_entregas" : "rh_epis",
                id, action, json.writeValueAsString(fields.keySet()), json.writeValueAsString(fields),
                actor, "ADM-004-EPI");
        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            throw new IllegalStateException("Falha ao auditar EPI", ex);
        }
    }
}
