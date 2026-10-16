package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.domain.Permissao;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import com.neritech.saas.gestaoUsuarios.repository.PermissaoRepository;
import com.neritech.saas.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.dto.FuncaoRequest;
import com.neritech.saas.gestaoUsuarios.dto.FuncaoResponse;
import com.neritech.saas.gestaoUsuarios.dto.PermissaoResponse;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncaoService {

    private final FuncaoRepository funcaoRepository;
    private final PermissaoRepository permissaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final org.springframework.jdbc.core.JdbcTemplate jdbc;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<Funcao> findAll() {
        return funcaoRepository.findAllByEmpresaId(TenantContext.getCurrentTenant());
    }

    @Transactional(readOnly = true)
    public Optional<Funcao> findByNome(String nome) {
        return funcaoRepository.findByNomeAndEmpresaId(nome, TenantContext.getCurrentTenant());
    }

    @Transactional(readOnly = true)
    public Funcao findById(Long id) {
        return funcaoRepository.findByIdAndEmpresaId(id, TenantContext.getCurrentTenant())
                .orElseThrow(() -> new ResourceNotFoundException("Função", "ID", id));
    }

    @Transactional
    public Funcao save(Funcao funcao) {
        syncPermissoes(funcao);
        return funcaoRepository.save(funcao);
    }

    @Transactional
    public Funcao update(Long id, Funcao dadosAtualizados) {
        Funcao funcaoExistente = findById(id);
        
        funcaoExistente.setNome(dadosAtualizados.getNome());
        funcaoExistente.setDescricao(dadosAtualizados.getDescricao());
        funcaoExistente.setAtivo(dadosAtualizados.getAtivo());
        
        // Sincronizar permissões
        funcaoExistente.getPermissoes().clear();
        if (dadosAtualizados.getPermissoes() != null) {
            funcaoExistente.getPermissoes().addAll(dadosAtualizados.getPermissoes());
        }
        syncPermissoes(funcaoExistente);
        
        return funcaoRepository.save(funcaoExistente);
    }

    @Transactional
    public void delete(Long id) {
        deactivate(id);
    }

    private void syncPermissoes(Funcao funcao) {
        if (funcao.getPermissoes() != null && !funcao.getPermissoes().isEmpty()) {
            List<Long> permissaoIds = funcao.getPermissoes().stream()
                    .map(Permissao::getId)
                    .collect(Collectors.toList());
            Set<Permissao> permissoesGerenciadas = permissaoRepository.findAllById(permissaoIds)
                    .stream()
                    .collect(Collectors.toSet());
            funcao.setPermissoes(permissoesGerenciadas);
        }
    }

    @Transactional(readOnly = true)
    public Page<FuncaoResponse> search(String query, Boolean ativo, Pageable pageable) {
        Long empresaId = TenantAccess.requireCurrent();
        if (!java.util.List.of(20, 50, 100).contains(pageable.getPageSize())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        }
        String q = query == null || query.isBlank() ? null : query.trim();
        return funcaoRepository.search(empresaId, q, ativo, pageable).map(this::response);
    }

    @Transactional(readOnly = true)
    public FuncaoResponse get(Long id) {
        return response(owned(id));
    }

    @Transactional
    public FuncaoResponse create(FuncaoRequest request) {
        Long empresaId = TenantAccess.requireCurrent();
        String nome = normalizeName(request.nome());
        validateReservedName(nome);
        if (funcaoRepository.existsByEmpresaIdAndNomeIgnoreCase(empresaId, nome)) conflict("Perfil já cadastrado");
        Funcao funcao = Funcao.builder().empresaId(empresaId).nome(nome)
                .descricao(normalizeOptional(request.descricao())).sistema(false)
                .ativo(request.ativo() == null || request.ativo()).permissoes(resolvePermissions(request.permissaoIds())).build();
        funcao = funcaoRepository.saveAndFlush(funcao);
        audit(funcao, "INSERT", snapshot(funcao));
        return response(funcao);
    }

    @Transactional
    public FuncaoResponse updateSecure(Long id, FuncaoRequest request) {
        Long empresaId = TenantAccess.requireCurrent();
        Funcao funcao = owned(id);
        if (Boolean.TRUE.equals(funcao.getSistema())) conflict("Perfil predefinido não pode ser alterado");
        if (request.versao() == null || !java.util.Objects.equals(request.versao(), funcao.getVersao())) {
            conflict("Perfil foi alterado; recarregue antes de salvar");
        }
        String nome = normalizeName(request.nome());
        validateReservedName(nome);
        if (funcaoRepository.existsByEmpresaIdAndNomeIgnoreCaseAndIdNot(empresaId, nome, id)) conflict("Perfil já cadastrado");
        java.util.Map<String,Object> before = snapshot(funcao);
        funcao.setNome(nome);
        funcao.setDescricao(normalizeOptional(request.descricao()));
        if (request.ativo() != null) funcao.setAtivo(request.ativo());
        funcao.setPermissoes(resolvePermissions(request.permissaoIds()));
        funcao = funcaoRepository.saveAndFlush(funcao);
        audit(funcao, "UPDATE", changes(before, snapshot(funcao)));
        return response(funcao);
    }

    @Transactional
    public void deactivate(Long id) {
        Funcao funcao = owned(id);
        if (Boolean.TRUE.equals(funcao.getSistema())) conflict("Perfil predefinido não pode ser inativado");
        if (!Boolean.TRUE.equals(funcao.getAtivo())) return;
        protectLastAdmin(funcao);
        funcao.setAtivo(false);
        funcaoRepository.saveAndFlush(funcao);
        audit(funcao, "UPDATE", java.util.Map.of("ativo", false));
    }

    private Funcao owned(Long id) {
        return funcaoRepository.findByIdAndEmpresaId(id, TenantAccess.requireCurrent())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado"));
    }

    private Set<Permissao> resolvePermissions(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new java.util.HashSet<>();
        Set<Permissao> found = new java.util.HashSet<>(permissaoRepository.findAllById(ids));
        if (found.size() != ids.size()) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Capability inválida");
        return found;
    }

    private FuncaoResponse response(Funcao f) {
        Set<PermissaoResponse> permissions = f.getPermissoes().stream()
                .map(p -> new PermissaoResponse(p.getId(), p.getChave(), p.getDescricao(), p.getValor()))
                .collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
        return new FuncaoResponse(f.getId(), f.getEmpresaId(), f.getNome(), f.getDescricao(), f.getSistema(), f.getAtivo(),
                funcaoRepository.countUsers(f.getEmpresaId(), f.getId()), permissions, f.getDataAtualizacao(), f.getVersao());
    }

    private void protectLastAdmin(Funcao funcao) {
        if (isAdminName(funcao.getNome()) && funcaoRepository.countAdminsOutsideRole(funcao.getEmpresaId(), funcao.getId()) == 0) {
            conflict("Não é permitido remover o último administrador ativo");
        }
    }

    private static void validateReservedName(String nome) {
        if (isAdminName(nome)) conflict("Perfis administrativos são predefinidos pelo sistema");
    }
    private static boolean isAdminName(String nome) {
        return nome != null && java.util.Set.of("ADMIN", "ADMINISTRADOR", "SUPER_ADMIN").contains(nome.toUpperCase(java.util.Locale.ROOT));
    }
    private static String normalizeName(String value) { return value.trim(); }
    private static String normalizeOptional(String value) { return value == null || value.isBlank() ? null : value.trim(); }
    private static void conflict(String message) { throw new ResponseStatusException(HttpStatus.CONFLICT, message); }

    private java.util.Map<String,Object> snapshot(Funcao f) {
        java.util.Map<String,Object> values = new java.util.LinkedHashMap<>();
        values.put("nome", f.getNome()); values.put("descricao", f.getDescricao()); values.put("ativo", f.getAtivo());
        values.put("capabilities", f.getPermissoes().stream().map(Permissao::getValor).sorted().toList());
        return values;
    }
    private static java.util.Map<String,Object> changes(java.util.Map<String,Object> before, java.util.Map<String,Object> after) {
        java.util.Map<String,Object> result = new java.util.LinkedHashMap<>();
        after.forEach((key,value) -> { if (!java.util.Objects.equals(before.get(key), value)) result.put(key,value); });
        return result;
    }
    private void audit(Funcao f, String operation, java.util.Map<String,?> values) {
        if (values.isEmpty()) return;
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        var actor = auth == null ? java.util.Optional.<com.neritech.saas.gestaoUsuarios.domain.Usuario>empty()
                : usuarioRepository.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), f.getEmpresaId());
        if (actor.isEmpty()) throw new org.springframework.security.access.AccessDeniedException("Autor inválido");
        try {
            jdbc.update("""
                    insert into logs_alteracoes (empresa_id,tabela_afetada,registro_id,operacao,
                    campos_alterados,valores_novos,usuario_responsavel,contexto_operacao,data_alteracao)
                    values (?,'funcoes',?,?,?,?,?,'ADM-006',current_timestamp)
                    """, f.getEmpresaId(), f.getId(), operation, objectMapper.writeValueAsString(values.keySet()),
                    objectMapper.writeValueAsString(values), actor.get().getId());
        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) { throw new IllegalStateException("Falha ao auditar perfil", ex); }
    }

    @Transactional(readOnly = true)
    public Page<com.neritech.saas.gestaoUsuarios.dto.FuncaoAlteracaoResponse> history(Long id, int page, int size) {
        Funcao f = owned(id);
        if (page < 0 || !java.util.List.of(20,50,100).contains(size)) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        var pageable = org.springframework.data.domain.PageRequest.of(page,size);
        Long total = jdbc.queryForObject("select count(*) from logs_alteracoes where empresa_id=? and tabela_afetada='funcoes' and registro_id=? and contexto_operacao='ADM-006'", Long.class, f.getEmpresaId(), id);
        var content = jdbc.query("""
                select operacao,campos_alterados,valores_novos,usuario_responsavel,data_alteracao from logs_alteracoes
                where empresa_id=? and tabela_afetada='funcoes' and registro_id=? and contexto_operacao='ADM-006'
                order by data_alteracao desc,id desc limit ? offset ?
                """, (rs,row) -> new com.neritech.saas.gestaoUsuarios.dto.FuncaoAlteracaoResponse(rs.getString(1),rs.getString(2),rs.getString(3),rs.getLong(4),rs.getTimestamp(5).toLocalDateTime()), f.getEmpresaId(),id,size,pageable.getOffset());
        return new org.springframework.data.domain.PageImpl<>(content,pageable,total == null ? 0 : total);
    }
}
