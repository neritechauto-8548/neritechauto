package com.neritech.saas.empresa.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.empresa.dto.EmpresaAlteracaoResponse;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Audita apenas campos explicitamente selecionados; nunca credenciais de provedores. */
@Service
public class EmpresaAlteracaoService {
    private final JdbcTemplate jdbc;
    private final ObjectMapper json;
    private final UsuarioRepository usuarios;

    public EmpresaAlteracaoService(JdbcTemplate jdbc, ObjectMapper json, UsuarioRepository usuarios) {
        this.jdbc = jdbc;
        this.json = json;
        this.usuarios = usuarios;
    }

    public void registrar(Long empresaId, String tabela, Long registroId, Integer versao,
                          Map<String, ?> antes, Map<String, ?> depois) {
        TenantAccess.require(empresaId);
        if (!List.of("empresa", "configuracoes_fiscais", "configuracoes_empresa",
                "enderecos_empresa", "configuracoes_oficina").contains(tabela)) {
            throw new IllegalArgumentException("Origem de auditoria inválida");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário autenticado necessário");
        }
        Long autor = usuarios.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Usuário não pertence à empresa")).getId();

        for (Map.Entry<String, ?> campo : depois.entrySet()) {
            String nome = campo.getKey();
            Object antigo = antes.get(nome);
            Object novo = campo.getValue();
            if (Objects.equals(antigo, novo)) continue;
            Map<String, Object> oldValue = new LinkedHashMap<>();
            oldValue.put(nome, antigo);
            Map<String, Object> newValue = new LinkedHashMap<>();
            newValue.put(nome, novo);
            jdbc.update("""
                    INSERT INTO logs_alteracoes
                    (empresa_id, tabela_afetada, registro_id, operacao, campos_alterados,
                     valores_antigos, valores_novos, usuario_responsavel, contexto_operacao,
                     dados_adicionais, data_alteracao)
                    VALUES (?, ?, ?, 'UPDATE', ?, ?, ?, ?, 'ADM-001', ?, CURRENT_TIMESTAMP)
                    """, empresaId, tabela, registroId, toJson(List.of(nome)),
                    toJson(oldValue), toJson(newValue), autor, toJson(Map.of("versao", versao == null ? 0 : versao)));
        }
    }

    public Page<EmpresaAlteracaoResponse> historico(Long empresaId, int page, int size) {
        TenantAccess.require(empresaId);
        if (page < 0 || !List.of(20, 50, 100).contains(size)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        }
        var pageable = PageRequest.of(page, size);
        Long count = jdbc.queryForObject("""
                SELECT COUNT(*) FROM logs_alteracoes
                WHERE empresa_id = ? AND contexto_operacao = 'ADM-001'
                """, Long.class, empresaId);
        List<EmpresaAlteracaoResponse> content = jdbc.query("""
                SELECT tabela_afetada, campos_alterados, valores_antigos, valores_novos,
                       usuario_responsavel, data_alteracao, dados_adicionais
                FROM logs_alteracoes
                WHERE empresa_id = ? AND contexto_operacao = 'ADM-001'
                ORDER BY data_alteracao DESC, id DESC LIMIT ? OFFSET ?
                """, (rs, rowNum) -> {
            String campo = parse(rs.getString("campos_alterados")).path(0).asText();
            JsonNode oldValue = parse(rs.getString("valores_antigos")).path(campo);
            JsonNode newValue = parse(rs.getString("valores_novos")).path(campo);
            Timestamp data = rs.getTimestamp("data_alteracao");
            return new EmpresaAlteracaoResponse(
                    rs.getString("tabela_afetada"), campo,
                    valor(oldValue), valor(newValue), rs.getLong("usuario_responsavel"),
                    data.toLocalDateTime(), parse(rs.getString("dados_adicionais")).path("versao").asInt());
        }, empresaId, size, pageable.getOffset());
        return new PageImpl<>(content, pageable, count == null ? 0 : count);
    }

    private JsonNode parse(String text) {
        try { return json.readTree(text == null ? "{}" : text); }
        catch (JsonProcessingException ex) { throw new IllegalStateException("Histórico inconsistente", ex); }
    }

    private String toJson(Object data) {
        try { return json.writeValueAsString(data); }
        catch (JsonProcessingException ex) { throw new IllegalStateException("Erro ao registrar auditoria", ex); }
    }

    private static String valor(JsonNode node) {
        return node.isMissingNode() || node.isNull() ? "—" : node.asText();
    }
}
