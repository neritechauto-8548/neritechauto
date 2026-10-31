package com.neritech.saas.produtoServico.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.produtoServico.domain.Fornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class FornecedorHistoricoService {
    private final JdbcTemplate jdbc;
    private final ObjectMapper json;
    private final UsuarioRepository usuarios;

    public record Alteracao(String campo, String anterior, String novo, Long autor,
                           java.time.LocalDateTime data, Integer versao) {}

    public FornecedorHistoricoService(JdbcTemplate jdbc, ObjectMapper json, UsuarioRepository usuarios) {
        this.jdbc = jdbc;
        this.json = json;
        this.usuarios = usuarios;
    }

    /** Não auditar dados bancários; ficam sob responsabilidade do Financeiro. */
    public static Map<String, Object> campos(Fornecedor f) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("tipoPessoa", f.getTipoPessoa());
        m.put("nomeFantasia", f.getNomeFantasia());
        m.put("razaoSocial", f.getRazaoSocial());
        m.put("cpf", f.getCpf());
        m.put("cnpj", f.getCnpj());
        m.put("inscricaoEstadual", f.getInscricaoEstadual());
        m.put("inscricaoMunicipal", f.getInscricaoMunicipal());
        m.put("emailPrincipal", f.getEmailPrincipal());
        m.put("telefonePrincipal", f.getTelefonePrincipal());
        m.put("nomeContato", f.getNomeContato());
        m.put("emailContato", f.getEmailContato());
        m.put("cep", f.getCep());
        m.put("cidade", f.getCidade());
        m.put("estado", f.getEstado());
        m.put("prazoPagamentoDias", f.getPrazoPagamentoDias());
        m.put("descontoPadrao", f.getDescontoPadrao());
        m.put("logradouro", f.getLogradouro());
        m.put("numero", f.getNumero());
        m.put("bairro", f.getBairro());
        m.put("complemento", f.getComplemento());
        m.put("planoContasId", f.getPlanoContasId());
        m.put("linhaPecas", f.getLinhaPecas());
        m.put("observacoes", f.getObservacoes());
        m.put("ativo", f.getAtivo());
        return m;
    }

    public void registrar(Fornecedor f, Map<String, Object> antes) {
        Long tenant = TenantAccess.require(f.getEmpresaId());
        String subject = SecurityContextHolder.getContext().getAuthentication() == null
                ? null : SecurityContextHolder.getContext().getAuthentication().getName();
        Long autor = subject == null ? null : usuarios.findByEmailIgnoreCaseAndEmpresaId(subject, tenant)
                .map(u -> u.getId()).orElse(null);
        if (autor == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Autor da alteração não identificado");
        }
        for (var entry : campos(f).entrySet()) {
            String field = entry.getKey();
            Object previous = antes.get(field);
            Object current = entry.getValue();
            if (Objects.equals(previous, current)) continue;
            jdbc.update("""
                INSERT INTO logs_alteracoes
                  (empresa_id, tabela_afetada, registro_id, operacao, campos_alterados,
                   valores_antigos, valores_novos, usuario_responsavel,
                   contexto_operacao, dados_adicionais, data_alteracao)
                VALUES (?, 'fornecedores', ?, 'UPDATE', ?, ?, ?, ?, 'ADM-008', ?, CURRENT_TIMESTAMP)
                """, tenant, f.getId(), encode(List.of(field)),
                    encode(java.util.Collections.singletonMap(field, previous)),
                    encode(java.util.Collections.singletonMap(field, current)), autor,
                    encode(Map.of("versao", f.getVersao() == null ? 0 : f.getVersao())));
        }
    }

    public Page<Alteracao> historico(Long tenant, Long fornecedorId, int page, int size) {
        TenantAccess.require(tenant);
        if (page < 0 || !List.of(20, 50, 100).contains(size)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        }
        var pagination = PageRequest.of(page, size);
        Long count = jdbc.queryForObject("""
            SELECT COUNT(*) FROM logs_alteracoes
            WHERE empresa_id = ? AND tabela_afetada = 'fornecedores' AND registro_id = ?
              AND contexto_operacao = 'ADM-008'
            """, Long.class, tenant, fornecedorId);
        var content = jdbc.query("""
            SELECT campos_alterados, valores_antigos, valores_novos,
                   usuario_responsavel, data_alteracao, dados_adicionais
              FROM logs_alteracoes
             WHERE empresa_id = ? AND tabela_afetada = 'fornecedores' AND registro_id = ?
               AND contexto_operacao = 'ADM-008'
             ORDER BY data_alteracao DESC, id DESC LIMIT ? OFFSET ?
            """, (rs, ignored) -> {
                String campo = parse(rs.getString("campos_alterados")).path(0).asText();
                JsonNode before = parse(rs.getString("valores_antigos")).path(campo);
                JsonNode after = parse(rs.getString("valores_novos")).path(campo);
                Timestamp timestamp = rs.getTimestamp("data_alteracao");
                return new Alteracao(campo, safeValue(before), safeValue(after),
                        rs.getLong("usuario_responsavel"), timestamp.toLocalDateTime(),
                        parse(rs.getString("dados_adicionais")).path("versao").asInt());
            }, tenant, fornecedorId, size, pagination.getOffset());
        return new PageImpl<>(content, pagination, count == null ? 0 : count);
    }

    private String encode(Object value) {
        try { return json.writeValueAsString(value); }
        catch (JsonProcessingException ex) { throw new IllegalStateException("Erro ao auditar fornecedor", ex); }
    }

    private JsonNode parse(String value) {
        try { return json.readTree(value == null ? "{}" : value); }
        catch (JsonProcessingException ex) { throw new IllegalStateException("Registro de auditoria inválido", ex); }
    }

    private static String safeValue(JsonNode value) {
        return value.isMissingNode() || value.isNull() ? "—" : value.asText();
    }
}
