package com.neritech.saas.empresa.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class ItensConfiguracaoService {
    private static final Set<String> GRUPOS = Set.of("CLIENTE_CATEGORIA", "CLIENTE_RECOMENDACAO", "FINANCEIRO_ETIQUETA", "VENDA_DIRETA_ETIQUETA", "ORDEM_SERVICO_ETIQUETA", "ORDEM_SERVICO_TAG_ITEM");
    private static final java.util.Map<String, String> TABELAS = java.util.Map.of(
        "CLIENTE_CATEGORIA", "categorias_cliente", "CLIENTE_RECOMENDACAO", "recomendacoes_cliente",
        "FINANCEIRO_ETIQUETA", "etiquetas_financeiras", "VENDA_DIRETA_ETIQUETA", "etiquetas_venda_direta",
        "ORDEM_SERVICO_ETIQUETA", "etiquetas_ordem_servico",
        "ORDEM_SERVICO_TAG_ITEM", "tags_itens_ordem_servico_empresa");
    private static final Set<String> CORES = Set.of("#2D2D2D", "#899198", "#FF4D55", "#E83E7A", "#BA42D2", "#7544E8", "#4367E8", "#2585DA", "#16A7B9", "#12B886", "#3BB956", "#7EC51D", "#F3A900", "#FF7A00", "#FFFFFF00");
    public record Item(Long id, String grupo, String descricao, String cor, java.time.LocalDateTime atualizadoEm) {}
    public record Alteracao(String grupo, String descricao, String cor) {}
    private final JdbcTemplate jdbc;
    private final UsuarioRepository usuarios;
    private final ObjectMapper json;

    public ItensConfiguracaoService(JdbcTemplate jdbc, UsuarioRepository usuarios, ObjectMapper json) {
        this.jdbc = jdbc;
        this.usuarios = usuarios;
        this.json = json;
    }

    @Transactional(readOnly = true)
    public List<Item> listar(String grupo, String busca) {
        Long empresa = TenantAccess.requireCurrent();
        validarGrupo(grupo);
        String tabela = TABELAS.get(grupo);
        String termo = busca == null ? "" : busca.trim();
        return jdbc.query("""
            SELECT id, descricao, cor, atualizado_em
            FROM %s
            WHERE empresa_id = ? AND descricao ILIKE ?
            ORDER BY descricao, id
            """.formatted(tabela), (rs, n) -> new Item(rs.getLong("id"), grupo, rs.getString("descricao"), rs.getString("cor"), rs.getTimestamp("atualizado_em").toLocalDateTime()), empresa, "%" + termo + "%");
    }

    @Transactional
    public Item criar(Alteracao alteracao) {
        Long empresa = TenantAccess.requireCurrent();
        validar(alteracao);
        Long autor = usuario(empresa);
        String tabela = TABELAS.get(alteracao.grupo());
        try {
            Long id = jdbc.queryForObject("INSERT INTO " + tabela + " (empresa_id, descricao, cor, criado_por) VALUES (?, ?, ?, ?) RETURNING id",
                Long.class, empresa, alteracao.descricao().trim(), normalizarCor(alteracao.cor()), autor);
            auditar(empresa, id, alteracao.grupo(), "INSERT", null, alteracao, autor);
            return obter(empresa, id, alteracao.grupo());
        } catch (DataIntegrityViolationException repetido) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um item com essa descrição neste grupo.");
        }
    }

    @Transactional
    public Item editar(Long id, Alteracao alteracao) {
        Long empresa = TenantAccess.requireCurrent();
        validar(alteracao);
        Item anterior = obter(empresa, id, alteracao.grupo());
        Long autor = usuario(empresa);
        String tabela = TABELAS.get(alteracao.grupo());
        try {
            int linhas = jdbc.update("UPDATE " + tabela + " SET descricao=?, cor=?, atualizado_em=CURRENT_TIMESTAMP WHERE id=? AND empresa_id=?",
                alteracao.descricao().trim(), normalizarCor(alteracao.cor()), id, empresa);
            if (linhas == 0) throw naoEncontrado();
            auditar(empresa, id, alteracao.grupo(), "UPDATE", anterior, alteracao, autor);
            return obter(empresa, id, alteracao.grupo());
        } catch (DataIntegrityViolationException repetido) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um item com essa descrição neste grupo.");
        }
    }

    @Transactional
    public void excluir(Long id, String grupo) {
        Long empresa = TenantAccess.requireCurrent();
        validarGrupo(grupo);
        Item anterior = obter(empresa, id, grupo);
        Long autor = usuario(empresa);
        jdbc.update("DELETE FROM " + TABELAS.get(grupo) + " WHERE id = ? AND empresa_id = ?", id, empresa);
        auditar(empresa, id, grupo, "DELETE", anterior, null, autor);
    }

    private Item obter(Long empresa, Long id, String grupo) {
        Item item = jdbc.query("SELECT id, descricao, cor, atualizado_em FROM " + TABELAS.get(grupo) + " WHERE id=? AND empresa_id=?",
            rs -> rs.next() ? new Item(rs.getLong("id"), grupo, rs.getString("descricao"), rs.getString("cor"), rs.getTimestamp("atualizado_em").toLocalDateTime()) : null, id, empresa);
        if (item == null) throw naoEncontrado();
        return item;
    }

    private static ResponseStatusException naoEncontrado() { return new ResponseStatusException(HttpStatus.NOT_FOUND, "Item de configuração não encontrado nesta empresa."); }
    private static void validarGrupo(String grupo) { if (grupo == null || !GRUPOS.contains(grupo)) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Grupo de configuração inválido."); }
    private static String normalizarCor(String cor) { return cor.toUpperCase(Locale.ROOT); }
    private static void validar(Alteracao alteracao) {
        if (alteracao == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        validarGrupo(alteracao.grupo());
        if (alteracao.descricao() == null || alteracao.descricao().trim().length() < 2 || alteracao.descricao().trim().length() > 100)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "A descrição deve conter de 2 a 100 caracteres.");
        if (alteracao.cor() == null || !CORES.contains(alteracao.cor().toUpperCase(Locale.ROOT)))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cor não permitida.");
    }
    private Long usuario(Long empresa) {
        var autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao == null || !autenticacao.isAuthenticated()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return usuarios.findByEmailIgnoreCaseAndEmpresaId(autenticacao.getName(), empresa)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário fora da empresa.")).getId();
    }
    private void auditar(Long empresa, Long id, String grupo, String operacao, Object anterior, Object novo, Long autor) {
        jdbc.update("""
            INSERT INTO logs_alteracoes (empresa_id, tabela_afetada, registro_id, operacao, campos_alterados,
              valores_antigos, valores_novos, usuario_responsavel, contexto_operacao, dados_adicionais, data_alteracao)
            VALUES (?, ?, ?, ?, '[\"descricao\",\"cor\"]', ?, ?,
              ?, 'ADM-012', '{}', CURRENT_TIMESTAMP)
            """, empresa, TABELAS.get(grupo), id, operacao, serializar(anterior), serializar(novo), autor);
    }
    private String serializar(Object valor) {
        try { return valor == null ? "{}" : json.writeValueAsString(valor); }
        catch (JsonProcessingException erro) { throw new IllegalStateException("Falha ao registrar auditoria das configurações", erro); }
    }
}
