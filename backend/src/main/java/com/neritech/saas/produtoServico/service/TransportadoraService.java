package com.neritech.saas.produtoServico.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.produtoServico.domain.Fornecedor;
import com.neritech.saas.produtoServico.domain.PedidoFornecedor;
import com.neritech.saas.produtoServico.domain.enums.TipoPessoa;
import com.neritech.saas.produtoServico.dto.*;
import com.neritech.saas.produtoServico.repository.FornecedorRepository;
import com.neritech.saas.produtoServico.repository.PedidoFornecedorRepository;
import com.neritech.saas.util.DocumentoValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransportadoraService {
    private final FornecedorRepository fornecedores;
    private final PedidoFornecedorRepository pedidos;
    private final JdbcTemplate jdbc;
    private final ObjectMapper json;
    private final UsuarioRepository usuarios;

    public record Historico(String campo, String anterior, String novo, Long autor,
                            LocalDateTime data, int versao) {}

    public TransportadoraService(FornecedorRepository fornecedores, PedidoFornecedorRepository pedidos,
                                  JdbcTemplate jdbc, ObjectMapper json, UsuarioRepository usuarios) {
        this.fornecedores = fornecedores;
        this.pedidos = pedidos;
        this.jdbc = jdbc;
        this.json = json;
        this.usuarios = usuarios;
    }

    @Transactional(readOnly = true)
    public Page<TransportadoraResponse> listar(String busca, String regiao, Boolean ativo, Pageable pageable) {
        Long tenant = TenantAccess.requireCurrent();
        validarPagina(pageable);
        return fornecedores.listarTransportadoras(tenant, ativo, regiao, busca, pageable)
                .map(this::response);
    }

    @Transactional(readOnly = true)
    public TransportadoraResponse obter(Long id) { return response(localizar(id)); }

    @Transactional
    public TransportadoraResponse criar(TransportadoraRequest request) {
        Long tenant = TenantAccess.requireCurrent();
        String cpf = documento(request.cpf()), cnpj = documento(request.cnpj());
        validarDocumento(request.tipoPessoa(), cpf, cnpj);
        Fornecedor f;
        Map<String, Object> antes;
        if (request.fornecedorId() != null) {
            f = fornecedores.findById(request.fornecedorId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado"));
            TenantAccess.require(f.getEmpresaId());
            if (Boolean.TRUE.equals(f.getTransportadora())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Fornecedor já é transportadora");
            }
            if (f.getTipoPessoa() != request.tipoPessoa()
                    || !Objects.equals(documento(f.getCpf()), cpf)
                    || !Objects.equals(documento(f.getCnpj()), cnpj)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "O documento informado não corresponde ao fornecedor existente");
            }
            antes = campos(f);
            // Não sobrescrever contatos e endereço já compartilhados com Compras.
            if (!Boolean.TRUE.equals(f.getAtivo())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Fornecedor mestre inativo");
            }
            f.setTransportadora(true);
            f.setTransportadoraAtiva(request.ativo() == null || request.ativo());
            f.setModalidadeTransporte(request.modalidade());
        } else {
            verificarDuplicidade(tenant, cpf, cnpj, null);
            f = new Fornecedor();
            f.setEmpresaId(tenant);
            f.setTipoPessoa(request.tipoPessoa());
            f.setCpf(cpf);
            f.setCnpj(cnpj);
            f.setTransportadora(true);
            f.setTransportadoraAtiva(request.ativo() == null || request.ativo());
            f.setAtivo(true);
            antes = Map.of();
            aplicar(f, request);
        }
        salvar(f);
        auditar(f, antes);
        return response(f);
    }

    @Transactional
    public TransportadoraResponse editar(Long id, TransportadoraRequest request, Integer version) {
        Fornecedor f = localizar(id);
        if (version == null || !Objects.equals(version, f.getVersao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cadastro desatualizado. Recarregue antes de salvar");
        }
        if (request.fornecedorId() != null && !Objects.equals(request.fornecedorId(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é permitido trocar a identidade do fornecedor");
        }
        String cpf = documento(request.cpf()), cnpj = documento(request.cnpj());
        validarDocumento(request.tipoPessoa(), cpf, cnpj);
        verificarDuplicidade(f.getEmpresaId(), cpf, cnpj, id);
        if (Boolean.TRUE.equals(request.ativo()) && !Boolean.TRUE.equals(f.getAtivo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Fornecedor mestre inativo: reative-o em Fornecedores primeiro");
        }
        if (!Objects.equals(documento(f.getCpf()), cpf)
                || !Objects.equals(documento(f.getCnpj()), cnpj)) {
            Long usado = jdbc.queryForObject("""
                SELECT COUNT(*) FROM pedidos_fornecedor
                WHERE empresa_id = ? AND (fornecedor_id = ? OR transportadora_id = ?)
                """, Long.class, f.getEmpresaId(), id, id);
            if (usado != null && usado > 0) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Documento não pode mudar enquanto há compras vinculadas");
            }
        }
        Map<String, Object> antes = campos(f);
        aplicar(f, request);
        if (request.ativo() != null) f.setTransportadoraAtiva(request.ativo());
        salvar(f);
        auditar(f, antes);
        return response(f);
    }

    @Transactional
    public void inativar(Long id) {
        Fornecedor f = localizar(id);
        if (!Boolean.TRUE.equals(f.getTransportadoraAtiva())) return;
        Map<String, Object> antes = campos(f);
        f.setTransportadoraAtiva(false); // Não inativar o fornecedor comum nem afetar Compras.
        salvar(f);
        auditar(f, antes);
    }

    @Transactional(readOnly = true)
    public TransportadoraPedidoResponse pedido(Long pedidoId) {
        Long tenant = TenantAccess.requireCurrent();
        PedidoFornecedor pedido = pedidos.findById(pedidoId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido inexistente"));
        TenantAccess.require(pedido.getEmpresaId());
        return leituraPedido(pedidoId, tenant, pedido.getFornecedorId());
    }

    @Transactional
    public TransportadoraPedidoResponse vincular(Long id, Long pedidoId, TransportadoraPedidoRequest request) {
        Fornecedor f = localizar(id);
        PedidoFornecedor pedido = pedidos.findById(pedidoId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido inexistente"));
        TenantAccess.require(pedido.getEmpresaId());
        if (!Objects.equals(pedido.getEmpresaId(), f.getEmpresaId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Pedido pertence a outra empresa");
        }
        if (!Boolean.TRUE.equals(f.getAtivo()) || !Boolean.TRUE.equals(f.getTransportadoraAtiva())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Transportadora inativa");
        }
        var atual = jdbc.queryForList("""
            SELECT transportadora_id FROM pedidos_fornecedor WHERE id = ? AND empresa_id = ?
            FOR UPDATE
            """, pedidoId, f.getEmpresaId());
        if (atual.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido inexistente");
        Object anterior = atual.get(0).get("transportadora_id");
        if (anterior != null && !Objects.equals(((Number) anterior).longValue(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Pedido já possui outra transportadora; remova o vínculo anterior");
        }
        String rastreio = request == null ? null : limpar(request.documentoRastreio());
        String existente = jdbc.queryForObject("""
            SELECT documento_rastreio FROM pedidos_fornecedor WHERE id = ? AND empresa_id = ?
            """, String.class, pedidoId, f.getEmpresaId());
        if (anterior == null || !Objects.equals(existente, rastreio)) {
            jdbc.update("""
                UPDATE pedidos_fornecedor SET transportadora_id = ?, documento_rastreio = ?
                WHERE id = ? AND empresa_id = ?
                """, id, rastreio, pedidoId, f.getEmpresaId());
            auditarLogistica(f, pedidoId, "VINCULAR", rastreio);
        }
        return leituraPedido(pedidoId, f.getEmpresaId(), pedido.getFornecedorId());
    }

    @Transactional
    public void desvincular(Long id, Long pedidoId) {
        Fornecedor f = localizar(id);
        PedidoFornecedor pedido = pedidos.findById(pedidoId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido inexistente"));
        TenantAccess.require(pedido.getEmpresaId());
        int changed = jdbc.update("""
            UPDATE pedidos_fornecedor SET transportadora_id = NULL, documento_rastreio = NULL
            WHERE id = ? AND empresa_id = ? AND transportadora_id = ?
            """, pedidoId, f.getEmpresaId(), id);
        if (changed == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vínculo inexistente");
        auditarLogistica(f, pedidoId, "DESVINCULAR", null);
    }

    @Transactional(readOnly = true)
    public Page<Historico> historico(Long id, Pageable pageable) {
        Fornecedor f = localizar(id);
        validarPagina(pageable);
        var page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Long total = jdbc.queryForObject("""
            SELECT COUNT(*) FROM logs_alteracoes WHERE empresa_id = ? AND tabela_afetada = 'fornecedores'
            AND registro_id = ? AND contexto_operacao = 'ADM-010'
            """, Long.class, f.getEmpresaId(), id);
        var rows = jdbc.query("""
            SELECT campos_alterados, valores_antigos, valores_novos, usuario_responsavel,
                   data_alteracao, dados_adicionais FROM logs_alteracoes
            WHERE empresa_id = ? AND tabela_afetada = 'fornecedores'
              AND registro_id = ? AND contexto_operacao = 'ADM-010'
            ORDER BY data_alteracao DESC, id DESC LIMIT ? OFFSET ?
            """, (rs, i) -> {
            try {
                String campo = json.readTree(rs.getString(1)).path(0).asText();
                var antes = json.readTree(rs.getString(2)).path(campo);
                var depois = json.readTree(rs.getString(3)).path(campo);
                int version = json.readTree(rs.getString(6)).path("versao").asInt();
                return new Historico(campo, valor(antes), valor(depois), rs.getLong(4),
                        rs.getTimestamp(5).toLocalDateTime(), version);
            } catch (JsonProcessingException error) { throw new IllegalStateException("Log inválido", error); }
        }, f.getEmpresaId(), id, page.getPageSize(), page.getOffset());
        return new PageImpl<>(rows, page, total == null ? 0L : total);
    }

    private Fornecedor localizar(Long id) {
        Long tenant = TenantAccess.requireCurrent();
        Fornecedor f = fornecedores.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transportadora inexistente"));
        TenantAccess.require(f.getEmpresaId());
        if (!Boolean.TRUE.equals(f.getTransportadora())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transportadora inexistente");
        }
        return f;
    }

    private void verificarDuplicidade(Long tenant, String cpf, String cnpj, Long id) {
        if (cpf != null && fornecedores.existsOtherCpf(tenant, cpf, id)
                || cnpj != null && fornecedores.existsOtherCnpj(tenant, cnpj, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Documento já pertence a um fornecedor nesta empresa. Reutilize o fornecedor existente");
        }
    }

    private static void validarDocumento(TipoPessoa tipo, String cpf, String cnpj) {
        if (tipo == TipoPessoa.FISICA) {
            if (cnpj != null || cpf == null || !DocumentoValidator.isValidCpf(cpf)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "CPF inválido");
            }
        } else if (tipo == TipoPessoa.JURIDICA) {
            if (cpf != null || cnpj == null || cnpj.length() != 14
                    || cnpj.chars().distinct().count() == 1 || !DocumentoValidator.isValidCnpj(cnpj)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "CNPJ inválido");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Tipo de pessoa inválido");
        }
    }

    private static void aplicar(Fornecedor f, TransportadoraRequest r) {
        f.setTipoPessoa(r.tipoPessoa());
        f.setNomeFantasia(r.nomeFantasia().trim());
        f.setRazaoSocial(limpar(r.razaoSocial()));
        f.setCpf(documento(r.cpf()));
        f.setCnpj(documento(r.cnpj()));
        f.setNomeContato(limpar(r.contato()));
        f.setTelefonePrincipal(limpar(r.telefone()));
        f.setEmailPrincipal(limpar(r.email()));
        f.setEnderecoCompleto(limpar(r.endereco()));
        f.setCep(limpar(r.cep()));
        f.setCidade(limpar(r.cidade()));
        f.setEstado(limpar(r.estado()));
        f.setModalidadeTransporte(r.modalidade());
        f.setObservacoes(limpar(r.observacoes()));
    }
    private static String limpar(String v) { return v == null || v.isBlank() ? null : v.trim(); }
    private static String documento(String v) {
        return v == null || v.isBlank() ? null :
                v.replaceAll("[^a-zA-Z0-9]", "").toUpperCase(Locale.ROOT);
    }

    private void salvar(Fornecedor f) {
        try { fornecedores.saveAndFlush(f); }
        catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Documento duplicado", ex);
        }
    }

    private TransportadoraResponse response(Fornecedor f) {
        var summary = jdbc.queryForMap("""
            SELECT COUNT(*) AS total, MAX(p.data_cadastro) AS ultima
            FROM pedidos_fornecedor p WHERE p.empresa_id = ? AND p.transportadora_id = ?
            """, f.getEmpresaId(), f.getId());
        Object last = summary.get("ultima");
        LocalDateTime ultima = last instanceof Timestamp ts ? ts.toLocalDateTime()
                : last instanceof LocalDateTime dt ? dt : null;
        return new TransportadoraResponse(f.getId(), f.getEmpresaId(), f.getId(), f.getTipoPessoa(),
                f.getNomeFantasia(), f.getRazaoSocial(), f.getCpf(), f.getCnpj(),
                f.getNomeContato(), f.getTelefonePrincipal(), f.getEmailPrincipal(),
                f.getEnderecoCompleto(), f.getCep(), f.getCidade(), f.getEstado(),
                f.getModalidadeTransporte(), Boolean.TRUE.equals(f.getAtivo()) && Boolean.TRUE.equals(f.getTransportadoraAtiva()), f.getObservacoes(), f.getVersao(),
                ((Number) summary.get("total")).longValue(), ultima);
    }

    private TransportadoraPedidoResponse leituraPedido(Long pedidoId, Long tenant, Long fornecedorId) {
        var rows = jdbc.query("""
            SELECT p.transportadora_id, f.nome_fantasia, p.numero_nf, p.documento_rastreio
            FROM pedidos_fornecedor p
            LEFT JOIN fornecedores f ON f.id = p.transportadora_id AND f.empresa_id = p.empresa_id
            WHERE p.id = ? AND p.empresa_id = ?
            """, (rs, n) -> new TransportadoraPedidoResponse(pedidoId, tenant, fornecedorId,
                (Long) rs.getObject(1), rs.getString(2), rs.getString(3), rs.getString(4)), pedidoId, tenant);
        return rows.isEmpty() ? null : rows.get(0);
    }

    private static Map<String, Object> campos(Fornecedor f) {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("nomeFantasia", f.getNomeFantasia());
        values.put("razaoSocial", f.getRazaoSocial());
        values.put("cpf", f.getCpf());
        values.put("cnpj", f.getCnpj());
        values.put("contato", f.getNomeContato());
        values.put("email", f.getEmailPrincipal());
        values.put("telefone", f.getTelefonePrincipal());
        values.put("endereco", f.getEnderecoCompleto());
        values.put("cep", f.getCep());
        values.put("cidade", f.getCidade());
        values.put("estado", f.getEstado());
        values.put("modalidade", f.getModalidadeTransporte());
        values.put("ativo", f.getTransportadoraAtiva());
        values.put("transportadora", f.getTransportadora());
        values.put("observacoes", f.getObservacoes());
        return values;
    }

    private void auditar(Fornecedor f, Map<String, Object> antes) {
        Long autor = autor(f.getEmpresaId());
        for (var field : campos(f).entrySet()) {
            if (Objects.equals(antes.get(field.getKey()), field.getValue())) continue;
            registrar(f, autor, "UPDATE", field.getKey(), antes.get(field.getKey()), field.getValue());
        }
    }

    private void auditarLogistica(Fornecedor f, Long pedidoId, String acao, String rastreio) {
        // V149 aceita apenas INSERT/UPDATE/DELETE/SELECT (VARCHAR(10)).
        registrar(f, autor(f.getEmpresaId()), "UPDATE", "pedido",
                null, acao + " Pedido #" + pedidoId
                        + (rastreio == null ? "" : " • Rastreio " + rastreio));
    }

    private void registrar(Fornecedor f, Long autor, String acao, String campo, Object antes, Object depois) {
        jdbc.update("""
            INSERT INTO logs_alteracoes
              (empresa_id, tabela_afetada, registro_id, operacao, campos_alterados,
               valores_antigos, valores_novos, usuario_responsavel, contexto_operacao,
               dados_adicionais, data_alteracao)
            VALUES (?, 'fornecedores', ?, ?, ?, ?, ?, ?, 'ADM-010', ?, CURRENT_TIMESTAMP)
            """, f.getEmpresaId(), f.getId(), acao, json(List.of(campo)),
                json(Collections.singletonMap(campo, antes)), json(Collections.singletonMap(campo, depois)),
                autor, json(Map.of("versao", f.getVersao() == null ? 0 : f.getVersao())));
    }

    private Long autor(Long tenant) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sessão necessária");
        }
        return usuarios.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), tenant)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não pertence à empresa"))
                .getId();
    }
    private String json(Object v) {
        try { return json.writeValueAsString(v); }
        catch (JsonProcessingException ex) { throw new IllegalStateException("Falha de auditoria", ex); }
    }
    private static String valor(com.fasterxml.jackson.databind.JsonNode v) {
        return v.isMissingNode() || v.isNull() ? "—" : v.asText();
    }
    private static void validarPagina(Pageable page) {
        if (page.getPageNumber() < 0 || !List.of(20, 50, 100).contains(page.getPageSize())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        }
    }
}
