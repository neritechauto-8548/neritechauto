package com.neritech.saas.empresa.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.empresa.domain.Seguradora;
import com.neritech.saas.empresa.dto.SeguradoraRequest;
import com.neritech.saas.empresa.dto.SeguradoraResponse;
import com.neritech.saas.empresa.repository.SeguradoraRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.util.DocumentoValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Service
public class SeguradoraService {
    private final SeguradoraRepository repository;
    private final OrdemServicoRepository os;
    private final JdbcTemplate jdbc;
    private final UsuarioRepository usuarios;
    private final ObjectMapper json;

    public record VinculoOS(Long seguradoraId, String seguradora, Long ordemServicoId, String tipoVinculo) {}
    public record Historico(String campo, String anterior, String novo, Long autor,
                            LocalDateTime data, Integer versao) {}

    public SeguradoraService(SeguradoraRepository repository, OrdemServicoRepository os,
                             JdbcTemplate jdbc, UsuarioRepository usuarios, ObjectMapper json) {
        this.repository = repository;
        this.os = os;
        this.jdbc = jdbc;
        this.usuarios = usuarios;
        this.json = json;
    }

    @Transactional(readOnly = true)
    public Page<SeguradoraResponse> listar(String busca, String cnpj, String nome, String nomeFantasia, Boolean ativo, Pageable page) {
        Long tenant = TenantAccess.requireCurrent();
        if (page.getPageSize() != 20 && page.getPageSize() != 50 && page.getPageSize() != 100) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Paginação deve ser 20, 50 ou 100");
        }
        String filtro = busca == null || busca.isBlank() ? "" : busca.trim();
        return repository.buscar(tenant, ativo, filtro,
                cnpj == null ? "" : cnpj.replaceAll("[^A-Za-z0-9]", ""),
                nome == null ? "" : nome.trim(), nomeFantasia == null ? "" : nomeFantasia.trim(), page)
                .map(this::resposta);
    }

    @Transactional(readOnly = true)
    public SeguradoraResponse obter(Long id) { return resposta(localizar(id)); }

    @Transactional
    public SeguradoraResponse criar(SeguradoraRequest request) {
        Long tenant = TenantAccess.requireCurrent();
        String documento = documento(request.cnpj());
        validarDocumento(tenant, documento, null);
        Seguradora entity = new Seguradora();
        entity.setEmpresaId(tenant);
        atribuir(entity, request, documento);
        entity.setAtivo(request.ativo() == null || request.ativo());
        salvar(entity);
        registrar(entity, "INSERT", Map.of());
        return resposta(entity);
    }

    @Transactional
    public SeguradoraResponse editar(Long id, SeguradoraRequest request, Integer expectedVersion) {
        Seguradora entity = localizar(id);
        if (expectedVersion != null && !Objects.equals(expectedVersion, entity.getVersao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cadastro alterado em outra sessão");
        }
        String documento = documento(request.cnpj());
        validarDocumento(entity.getEmpresaId(), documento, id);
        Map<String, Object> antes = campos(entity);
        atribuir(entity, request, documento);
        if (request.ativo() != null) entity.setAtivo(request.ativo());
        salvar(entity);
        registrar(entity, "UPDATE", antes);
        return resposta(entity);
    }

    @Transactional
    public void inativar(Long id) {
        Seguradora entity = localizar(id);
        if (!Boolean.TRUE.equals(entity.getAtivo())) return;
        Map<String, Object> antes = campos(entity);
        entity.setAtivo(false);
        salvar(entity);
        registrar(entity, "UPDATE", antes);
    }

    @Transactional
    public VinculoOS vincular(Long id, Long ordemId, String tipo) {
        Seguradora s = localizar(id);
        if (!Boolean.TRUE.equals(s.getAtivo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Seguradora inativa");
        }
        if (!List.of("CONVENIO", "REEMBOLSO").contains(tipo)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Vínculo inválido");
        }
        os.findByIdAndEmpresaId(ordemId, s.getEmpresaId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "OS fora da empresa autenticada"));
        try {
            jdbc.update("""
                INSERT INTO seguradoras_os (empresa_id, seguradora_id, ordem_servico_id, tipo_vinculo)
                VALUES (?, ?, ?, ?)
                """, s.getEmpresaId(), id, ordemId, tipo);
        } catch (DataIntegrityViolationException conflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "OS já possui seguradora vinculada", conflict);
        }
        registrarVinculo(s, ordemId, "INSERT", tipo);
        return new VinculoOS(id, nomeExibicao(s), ordemId, tipo);
    }

    @Transactional(readOnly = true)
    public VinculoOS vinculoDaOS(Long ordemId) {
        Long tenant = TenantAccess.requireCurrent();
        os.findByIdAndEmpresaId(ordemId, tenant)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "OS fora da empresa autenticada"));
        var vinculos = jdbc.query("""
            SELECT s.id, COALESCE(NULLIF(s.nome_fantasia, ''), s.razao_social), v.ordem_servico_id, v.tipo_vinculo
              FROM seguradoras_os v JOIN seguradoras s ON s.id = v.seguradora_id
             WHERE v.empresa_id = ? AND v.ordem_servico_id = ? AND s.empresa_id = ?
            """, (rs, row) -> new VinculoOS(rs.getLong(1), rs.getString(2),
                rs.getLong(3), rs.getString(4)), tenant, ordemId, tenant);
        return vinculos.isEmpty() ? null : vinculos.get(0);
    }

    @Transactional
    public void desvincular(Long id, Long ordemId) {
        Seguradora s = localizar(id);
        if (!os.findByIdAndEmpresaId(ordemId, s.getEmpresaId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "OS fora da empresa autenticada");
        }
        int changed = jdbc.update("""
            DELETE FROM seguradoras_os WHERE empresa_id = ? AND seguradora_id = ? AND ordem_servico_id = ?
            """, s.getEmpresaId(), id, ordemId);
        if (changed == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vínculo não encontrado");
        registrarVinculo(s, ordemId, "DELETE", null);
    }

    @Transactional(readOnly = true)
    public Page<Historico> historico(Long id, Pageable page) {
        Seguradora s = localizar(id);
        if (page.getPageSize() != 20 && page.getPageSize() != 50 && page.getPageSize() != 100) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        }
        Long count = jdbc.queryForObject("""
            SELECT COUNT(*) FROM logs_alteracoes WHERE empresa_id = ?
              AND tabela_afetada = 'seguradoras' AND registro_id = ? AND contexto_operacao = 'ADM-009'
            """, Long.class, s.getEmpresaId(), id);
        var data = jdbc.query("""
            SELECT campos_alterados, valores_antigos, valores_novos, usuario_responsavel,
                   data_alteracao, dados_adicionais
            FROM logs_alteracoes WHERE empresa_id = ? AND tabela_afetada = 'seguradoras'
              AND registro_id = ? AND contexto_operacao = 'ADM-009'
            ORDER BY data_alteracao DESC, id DESC LIMIT ? OFFSET ?
            """, (rs, index) -> {
                try {
                    var fields = json.readTree(rs.getString(1));
                    String field = fields.path(0).asText();
                    var oldValue = json.readTree(rs.getString(2)).path(field);
                    var newValue = json.readTree(rs.getString(3)).path(field);
                    var extra = json.readTree(rs.getString(6));
                    return new Historico(field, oldValue.isMissingNode() || oldValue.isNull() ? "—" : oldValue.asText(),
                            newValue.isMissingNode() || newValue.isNull() ? "—" : newValue.asText(),
                            rs.getLong(4), rs.getTimestamp(5).toLocalDateTime(), extra.path("versao").asInt());
                } catch (JsonProcessingException invalid) {
                    throw new IllegalStateException("Histórico inválido", invalid);
                }
            }, s.getEmpresaId(), id, page.getPageSize(), page.getOffset());
        return new org.springframework.data.domain.PageImpl<>(data, page, count == null ? 0 : count);
    }

    private Seguradora localizar(Long id) {
        Long tenant = TenantAccess.requireCurrent();
        return repository.findByIdAndEmpresaId(id, tenant).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Seguradora fora da empresa autenticada"));
    }

    private static String documento(String raw) {
        return raw == null || raw.isBlank() ? null :
                raw.replaceAll("[^a-zA-Z0-9]", "").toUpperCase(Locale.ROOT);
    }

    private void validarDocumento(Long tenant, String cnpj, Long existing) {
        if (cnpj == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "CNPJ é obrigatório");
        }
        if (cnpj.length() != 14 || cnpj.chars().distinct().count() == 1 || !DocumentoValidator.isValidCnpj(cnpj)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "CNPJ inválido");
        }
        if (repository.duplicado(tenant, cnpj, existing)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CNPJ já cadastrado nesta empresa");
        }
    }

    private static String textoOpcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    private static String nomeExibicao(Seguradora s) {
        return s.getNomeFantasia() == null || s.getNomeFantasia().isBlank()
                ? s.getRazaoSocial() : s.getNomeFantasia();
    }

    private static void atribuir(Seguradora s, SeguradoraRequest r, String cnpj) {
        s.setCnpj(cnpj);
        s.setRazaoSocial(r.razaoSocial().trim());
        s.setNomeFantasia(textoOpcional(r.nomeFantasia()));
        s.setInscricaoEstadual(textoOpcional(r.inscricaoEstadual()));
        s.setInscricaoMunicipal(textoOpcional(r.inscricaoMunicipal()));
        s.setEmail(textoOpcional(r.email()));
        s.setObservacoes(textoOpcional(r.observacoes()));
        s.setMargemRefatoramento(r.margemRefatoramento());
        s.setSite(textoOpcional(r.site()));
        s.setRetencaoIss(Boolean.TRUE.equals(r.retencaoIss()));
    }

    private void salvar(Seguradora s) {
        try { repository.saveAndFlush(s); }
        catch (DataIntegrityViolationException conflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Seguradora duplicada", conflict);
        }
    }

    private SeguradoraResponse resposta(Seguradora s) {
        Long total = jdbc.queryForObject("""
            SELECT COUNT(*) FROM seguradoras_os WHERE empresa_id = ? AND seguradora_id = ?
            """, Long.class, s.getEmpresaId(), s.getId());
        return new SeguradoraResponse(s.getId(), s.getEmpresaId(), s.getCnpj(),
                s.getRazaoSocial(), s.getNomeFantasia(), s.getInscricaoEstadual(),
                s.getInscricaoMunicipal(), s.getEmail(), s.getObservacoes(),
                s.getMargemRefatoramento(), s.getSite(), s.getRetencaoIss(), s.getAtivo(),
                s.getVersao(), total == null ? 0 : total);
    }

    private static Map<String, Object> campos(Seguradora s) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("cnpj", s.getCnpj());
        m.put("razaoSocial", s.getRazaoSocial());
        m.put("nomeFantasia", s.getNomeFantasia());
        m.put("inscricaoEstadual", s.getInscricaoEstadual());
        m.put("inscricaoMunicipal", s.getInscricaoMunicipal());
        m.put("email", s.getEmail());
        m.put("observacoes", s.getObservacoes());
        m.put("margemRefatoramento", s.getMargemRefatoramento());
        m.put("site", s.getSite());
        m.put("retencaoIss", s.getRetencaoIss());
        m.put("ativo", s.getAtivo());
        return m;
    }

    private void registrar(Seguradora s, String operation, Map<String, Object> before) {
        Long autor = autor(s.getEmpresaId());
        for (var item : campos(s).entrySet()) {
            String field = item.getKey();
            Object old = before.get(field);
            if (Objects.equals(old, item.getValue())) continue;
            jdbc.update("""
                INSERT INTO logs_alteracoes
                (empresa_id, tabela_afetada, registro_id, operacao, campos_alterados, valores_antigos,
                 valores_novos, usuario_responsavel, contexto_operacao, dados_adicionais, data_alteracao)
                VALUES (?, 'seguradoras', ?, ?, ?, ?, ?, ?, 'ADM-009', ?, CURRENT_TIMESTAMP)
                """, s.getEmpresaId(), s.getId(), operation, serialize(List.of(field)),
                    serialize(java.util.Collections.singletonMap(field, old)),
                    serialize(java.util.Collections.singletonMap(field, item.getValue())),
                    autor, serialize(Map.of("versao", s.getVersao() == null ? 0 : s.getVersao())));
        }
    }

    private void registrarVinculo(Seguradora s, Long ordem, String operation, String tipo) {
        jdbc.update("""
            INSERT INTO logs_alteracoes
              (empresa_id, tabela_afetada, registro_id, operacao, campos_alterados, valores_antigos,
               valores_novos, usuario_responsavel, contexto_operacao, dados_adicionais, data_alteracao)
            VALUES (?, 'seguradoras', ?, ?, '["ordemServico"]', '{}', ?, ?, 'ADM-009', '{}', CURRENT_TIMESTAMP)
            """, s.getEmpresaId(), s.getId(), operation,
                serialize(Map.of("ordemServico", ordem, "tipoVinculo", tipo == null ? "" : tipo)), autor(s.getEmpresaId()));
    }

    private Long autor(Long tenant) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não autenticado");
        }
        return usuarios.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), tenant)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Usuário fora da empresa")).getId();
    }

    private String serialize(Object value) {
        try { return json.writeValueAsString(value); }
        catch (JsonProcessingException ex) { throw new IllegalStateException("Falha na auditoria", ex); }
    }
}
