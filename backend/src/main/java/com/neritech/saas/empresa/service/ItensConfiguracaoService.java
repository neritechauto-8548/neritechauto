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
    private static final Set<String> GRUPOS = Set.of("CLIENTE_CATEGORIA", "CLIENTE_RECOMENDACAO", "FINANCEIRO_ETIQUETA", "VENDA_DIRETA_ETIQUETA", "ORDEM_SERVICO_ETIQUETA");
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
        String termo = busca == null ? "" : busca.trim();
        return jdbc.query("""
            SELECT id, grupo, descricao, cor, atualizado_em
            FROM itens_configuracao_empresa
            WHERE empresa_id = ? AND grupo = ? AND descricao ILIKE ?
            ORDER BY descricao, id
            """, (rs, n) -> new Item(rs.getLong("id"), rs.getString("grupo"), rs.getString("descricao"), rs.getString("cor"), rs.getTimestamp("atualizado_em").toLocalDateTime()), empresa, grupo, "%" + termo + "%");
    }

    @Transactional
    public Item criar(Alteracao alteracao) {
        Long empresa = TenantAccess.requireCurrent();
        validar(alteracao);
        Long autor = usuario(empresa);
        try {
            Long id = jdbc.queryForObject("""
                INSERT INTO itens_configuracao_empresa (empresa_id, grupo, descricao, cor, criado_por)
                VALUES (?, ?, ?, ?, ?) RETURNING id
                """, Long.class, empresa, alteracao.grupo(), alteracao.descricao().trim(), normalizarCor(alteracao.cor()), autor);
            auditar(empresa, id, "INSERT", null, alteracao, autor);
            return obter(empresa, id);
        } catch (DataIntegrityViolationException repetido) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um item com essa descrição neste grupo.");
        }
    }

    @Transactional
    public Item editar(Long id, Alteracao alteracao) {
        Long empresa = TenantAccess.requireCurrent();
        validar(alteracao);
        Item anterior = obter(empresa, id);
        Long autor = usuario(empresa);
        try {
            int linhas = jdbc.update("""
                UPDATE itens_configuracao_empresa SET grupo = ?, descricao = ?, cor = ?, atualizado_em = CURRENT_TIMESTAMP
                WHERE id = ? AND empresa_id = ?
                """, alteracao.grupo(), alteracao.descricao().trim(), normalizarCor(alteracao.cor()), id, empresa);
            if (linhas == 0) throw naoEncontrado();
            auditar(empresa, id, "UPDATE", anterior, alteracao, autor);
            return obter(empresa, id);
        } catch (DataIntegrityViolationException repetido) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um item com essa descrição neste grupo.");
        }
    }

    @Transactional
    public void excluir(Long id) {
        Long empresa = TenantAccess.requireCurrent();
        Item anterior = obter(empresa, id);
        Long autor = usuario(empresa);
        jdbc.update("DELETE FROM itens_configuracao_empresa WHERE id = ? AND empresa_id = ?", id, empresa);
        auditar(empresa, id, "DELETE", anterior, null, autor);
    }

    private Item obter(Long empresa, Long id) {
        Item item = jdbc.query("""
            SELECT id, grupo, descricao, cor, atualizado_em FROM itens_configuracao_empresa WHERE id = ? AND empresa_id = ?
            """, rs -> rs.next() ? new Item(rs.getLong("id"), rs.getString("grupo"), rs.getString("descricao"), rs.getString("cor"), rs.getTimestamp("atualizado_em").toLocalDateTime()) : null, id, empresa);
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
    private void auditar(Long empresa, Long id, String operacao, Object anterior, Object novo, Long autor) {
        jdbc.update("""
            INSERT INTO logs_alteracoes (empresa_id, tabela_afetada, registro_id, operacao, campos_alterados,
              valores_antigos, valores_novos, usuario_responsavel, contexto_operacao, dados_adicionais, data_alteracao)
            VALUES (?, 'itens_configuracao_empresa', ?, ?, '[\"grupo\",\"descricao\",\"cor\"]', ?, ?,
              ?, 'ADM-012', '{}', CURRENT_TIMESTAMP)
            """, empresa, id, operacao, serializar(anterior), serializar(novo), autor);
    }
    private String serializar(Object valor) {
        try { return valor == null ? "{}" : json.writeValueAsString(valor); }
        catch (JsonProcessingException erro) { throw new IllegalStateException("Falha ao registrar auditoria das configurações", erro); }
    }
}
