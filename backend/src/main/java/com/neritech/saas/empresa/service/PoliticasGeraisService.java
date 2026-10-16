package com.neritech.saas.empresa.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PoliticasGeraisService {
    // Política geral não duplica fuso/moeda/horários de configuracoes_oficina,
    // fiscal, empresa, modelos de mensagem ou credenciais de outros owners.
    private record Definicao(String secao, String titulo, String padrao) {}
    private static final Map<String, Definicao> PERMITIDAS = Map.of(
        "locale", new Definicao("Empresa", "Idioma e formato", "pt-BR"),
        "precisaoQuantidades", new Definicao("Operação", "Casas decimais em quantidades", "2"),
        "validadeOrcamentoDias", new Definicao("Orçamentos", "Validade padrão (dias)", "15"),
        "avisosAtivos", new Definicao("Comunicação", "Exibir avisos internos", "true"),
        "retencaoHistoricoDias", new Definicao("Operação", "Retenção planejada (dias)", "365")
    );
    public record Alteracao(String campo, String anterior, String novo, Long autor,
                           LocalDateTime data, Integer versao) {}
    public record Politica(String chave, String secao, String titulo, String valorAplicado,
                           String valorSalvo, String origem, boolean ativo,
                           Integer versao, LocalDateTime alteradoEm, Long responsavel) {}
    public record Alterar(String chave, String valor) {}
    public record Estado(Boolean ativo) {}
    private final JdbcTemplate jdbc;
    private final UsuarioRepository usuarios;
    private final ObjectMapper json;

    public PoliticasGeraisService(JdbcTemplate jdbc, UsuarioRepository usuarios, ObjectMapper json) {
        this.jdbc = jdbc;
        this.usuarios = usuarios;
        this.json = json;
    }

    @Transactional(readOnly = true)
    public Page<Politica> listar(String busca, String secao, Boolean ativo, Pageable pageable) {
        Long tenant = TenantAccess.requireCurrent();
        validarPagina(pageable);
        Map<String, Politica> salvas = new HashMap<>();
        jdbc.query("""
            SELECT chave, valor, ativo, versao, data_atualizacao, atualizado_por
            FROM politicas_gerais_empresa WHERE empresa_id = ?
            """, rs -> {
            salvas.put(rs.getString("chave"), montar(rs));
        }, tenant);
        List<Politica> lista = PERMITIDAS.entrySet().stream().map(e ->
                salvas.getOrDefault(e.getKey(), new Politica(e.getKey(), e.getValue().secao(),
                    e.getValue().titulo(), e.getValue().padrao(), null, "PADRAO", true, null, null, null)))
                .filter(x -> secao == null || secao.isBlank() || x.secao().equalsIgnoreCase(secao.trim()))
                .filter(x -> busca == null || busca.isBlank()
                        || x.chave().toLowerCase(Locale.ROOT).contains(busca.trim().toLowerCase(Locale.ROOT))
                        || x.titulo().toLowerCase(Locale.ROOT).contains(busca.trim().toLowerCase(Locale.ROOT)))
                .filter(x -> ativo == null || x.ativo() == ativo)
                .sorted(Comparator.comparing(Politica::secao).thenComparing(Politica::chave))
                .toList();
        int first = Math.min((int) Math.min(Integer.MAX_VALUE, pageable.getOffset()), lista.size());
        int last = Math.min(first + pageable.getPageSize(), lista.size());
        return new PageImpl<>(lista.subList(first, last), pageable, lista.size());
    }

    @Transactional(readOnly = true)
    public Politica obter(String chave) {
        validarChave(chave);
        Long tenant = TenantAccess.requireCurrent();
        return jdbc.query("""
            SELECT chave, valor, ativo, versao, data_atualizacao, atualizado_por
            FROM politicas_gerais_empresa WHERE empresa_id = ? AND chave = ?
            """, rs -> rs.next() ? montar(rs) : padrao(chave), tenant, chave);
    }

    @Transactional
    public Politica criar(Alterar request) {
        Long tenant = TenantAccess.requireCurrent();
        validarValor(request.chave(), request.valor());
        Long autor = usuario(tenant);
        try {
            Long id = jdbc.queryForObject("""
                INSERT INTO politicas_gerais_empresa
                  (empresa_id, chave, valor, ativo, atualizado_por)
                VALUES (?, ?, ?, true, ?) RETURNING id
                """, Long.class, tenant, request.chave(), request.valor(), autor);
            registrar(tenant, id, request.chave(), null, request.valor(), 0, autor, "INSERT");
        } catch (DataIntegrityViolationException duplicate) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Política já personalizada para esta empresa", duplicate);
        }
        return obter(request.chave());
    }

    @Transactional
    public Politica editar(String chave, String valor, Integer versao) {
        Long tenant = TenantAccess.requireCurrent();
        validarValor(chave, valor);
        requireVersion(versao);
        Politica anterior = obter(chave);
        if (anterior.versao() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Crie a política antes de editá-la");
        }
        Long autor = usuario(tenant);
        int rows = jdbc.update("""
            UPDATE politicas_gerais_empresa
            SET valor = ?, versao = versao + 1, atualizado_por = ?, data_atualizacao = CURRENT_TIMESTAMP
            WHERE empresa_id = ? AND chave = ? AND versao = ?
            """, valor, autor, tenant, chave, versao);
        if (rows == 0) throw conflito();
        if (!Objects.equals(anterior.valorSalvo(), valor)) {
            registrar(tenant, registroId(tenant, chave), chave, anterior.valorSalvo(),
                    valor, versao + 1, autor, "UPDATE");
        }
        return obter(chave);
    }

    @Transactional
    public Politica situacao(String chave, boolean ativo, Integer versao) {
        Long tenant = TenantAccess.requireCurrent();
        validarChave(chave);
        requireVersion(versao);
        Politica anterior = obter(chave);
        if (anterior.versao() == null) throw conflito();
        Long autor = usuario(tenant);
        int rows = jdbc.update("""
            UPDATE politicas_gerais_empresa
            SET ativo = ?, versao = versao + 1, atualizado_por = ?, data_atualizacao = CURRENT_TIMESTAMP
            WHERE empresa_id = ? AND chave = ? AND versao = ?
            """, ativo, autor, tenant, chave, versao);
        if (rows == 0) throw conflito();
        if (anterior.ativo() != ativo) {
            registrar(tenant, registroId(tenant, chave), chave + ".ativo",
                    anterior.ativo(), ativo, versao + 1, autor, "UPDATE");
        }
        return obter(chave);
    }

    @Transactional(readOnly = true)
    public Page<Alteracao> historico(String chave, Pageable pageable) {
        Long tenant = TenantAccess.requireCurrent();
        validarChave(chave);
        validarPagina(pageable);
        Long total = jdbc.queryForObject("""
            SELECT COUNT(*) FROM logs_alteracoes
            WHERE empresa_id = ? AND tabela_afetada = 'politicas_gerais_empresa'
              AND contexto_operacao = 'ADM-011' AND (campos_alterados = ? OR campos_alterados = ?)
            """, Long.class, tenant, serialize(List.of(chave)), serialize(List.of(chave + ".ativo")));
        var dados = jdbc.query("""
            SELECT campos_alterados, valores_antigos, valores_novos, usuario_responsavel,
                   data_alteracao, dados_adicionais FROM logs_alteracoes
            WHERE empresa_id = ? AND tabela_afetada = 'politicas_gerais_empresa'
              AND contexto_operacao = 'ADM-011' AND (campos_alterados = ? OR campos_alterados = ?)
            ORDER BY data_alteracao DESC, id DESC LIMIT ? OFFSET ?
            """, (rs, index) -> {
                try {
                    String field = json.readTree(rs.getString(1)).path(0).asText();
                    var before = json.readTree(rs.getString(2)).path(field);
                    var after = json.readTree(rs.getString(3)).path(field);
                    return new Alteracao(field, before.isNull() || before.isMissingNode() ? "—" : before.asText(),
                            after.isNull() || after.isMissingNode() ? "—" : after.asText(),
                            rs.getLong(4), rs.getTimestamp(5).toLocalDateTime(),
                            json.readTree(rs.getString(6)).path("versao").asInt());
                } catch (JsonProcessingException ex) { throw new IllegalStateException("Log inválido", ex); }
            }, tenant, serialize(List.of(chave)), serialize(List.of(chave + ".ativo")),
                pageable.getPageSize(), pageable.getOffset());
        return new PageImpl<>(dados, pageable, total == null ? 0 : total);
    }

    private Politica montar(ResultSet rs) throws SQLException {
        String key = rs.getString("chave");
        Definicao d = PERMITIDAS.get(key);
        if (d == null) throw new IllegalStateException("Política desconhecida armazenada");
        boolean active = rs.getBoolean("ativo");
        return new Politica(key, d.secao(), d.titulo(), active ? rs.getString("valor") : d.padrao(),
                rs.getString("valor"), active ? "EMPRESA" : "PADRAO_INATIVA", active,
                rs.getInt("versao"), rs.getTimestamp("data_atualizacao").toLocalDateTime(),
                rs.getObject("atualizado_por", Long.class));
    }

    private Politica padrao(String chave) {
        Definicao d = PERMITIDAS.get(chave);
        return new Politica(chave, d.secao(), d.titulo(), d.padrao(),
                null, "PADRAO", true, null, null, null);
    }

    private void validarChave(String chave) {
        if (chave == null || !PERMITIDAS.containsKey(chave)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Política não permitida");
        }
    }
    private void validarValor(String chave, String valor) {
        validarChave(chave);
        if (valor == null || valor.isBlank() || valor.length() > 160
            || valor.toLowerCase(Locale.ROOT).matches(".*(token|password|senha|secret|apikey).*")) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Valor inválido ou credencial proibida");
        }
        boolean valid = switch (chave) {
            case "locale" -> List.of("pt-BR", "en-US").contains(valor);
            case "precisaoQuantidades" -> valor.matches("[0-4]");
            case "validadeOrcamentoDias" -> validoEntre(valor, 1, 365);
            case "retencaoHistoricoDias" -> validoEntre(valor, 30, 3650);
            case "avisosAtivos" -> List.of("true", "false").contains(valor);
            default -> false;
        };
        if (!valid) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Valor fora dos limites da política");
    }

    private static boolean validoEntre(String valor, int min, int max) {
        try { int i = Integer.parseInt(valor); return i >= min && i <= max && valor.matches("[0-9]+"); }
        catch (NumberFormatException ex) { return false; }
    }
    private static void requireVersion(Integer value) {
        if (value == null || value < 0) throw new ResponseStatusException(HttpStatus.CONFLICT, "Versão obrigatória");
    }
    private static ResponseStatusException conflito() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Configuração alterada em outra sessão");
    }
    private static void validarPagina(Pageable page) {
        if (!List.of(20, 50, 100).contains(page.getPageSize())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        }
    }

    private Long usuario(Long tenant) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return usuarios.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), tenant)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Usuário fora da empresa")).getId();
    }
    private Long registroId(Long tenant, String chave) {
        return jdbc.queryForObject("""
            SELECT id FROM politicas_gerais_empresa WHERE empresa_id = ? AND chave = ?
            """, Long.class, tenant, chave);
    }
    private void registrar(Long tenant, Long id, String field, Object oldValue, Object newValue,
                          int versao, Long autor, String operacao) {
        jdbc.update("""
            INSERT INTO logs_alteracoes
              (empresa_id, tabela_afetada, registro_id, operacao, campos_alterados,
               valores_antigos, valores_novos, usuario_responsavel,
               contexto_operacao, dados_adicionais, data_alteracao)
            VALUES (?, 'politicas_gerais_empresa', ?, ?, ?, ?, ?, ?, 'ADM-011', ?, CURRENT_TIMESTAMP)
            """, tenant, id, operacao, serialize(List.of(field)),
                serialize(Collections.singletonMap(field, oldValue)),
                serialize(Collections.singletonMap(field, newValue)),
                autor, serialize(Map.of("versao", versao)));
    }
    private String serialize(Object object) {
        try { return json.writeValueAsString(object); }
        catch (JsonProcessingException ex) { throw new IllegalStateException("Falha na auditoria", ex); }
    }
}
