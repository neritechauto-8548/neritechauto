package com.neritech.saas.empresa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.empresa.domain.DepartamentoContabio;
import com.neritech.saas.empresa.dto.DepartamentoAlteracaoResponse;
import com.neritech.saas.empresa.dto.DepartamentoContabioRequest;
import com.neritech.saas.empresa.dto.DepartamentoContabioResponse;
import com.neritech.saas.empresa.mapper.DepartamentoContabioMapper;
import com.neritech.saas.empresa.repository.DepartamentoContabioRepository;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.rh.repository.CargoRepository;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class DepartamentoContabioService {
    private final DepartamentoContabioRepository repository;
    private final EmpresaRepository empresaRepository;
    private final DepartamentoContabioMapper mapper;
    private final FuncionarioRepository funcionarioRepository;
    private final CargoRepository cargoRepository;
    private final UsuarioRepository usuarioRepository;
    private final JdbcTemplate jdbc;
    private final ObjectMapper objectMapper;

    public DepartamentoContabioService(DepartamentoContabioRepository repository, EmpresaRepository empresaRepository,
            DepartamentoContabioMapper mapper, FuncionarioRepository funcionarioRepository,
            CargoRepository cargoRepository, UsuarioRepository usuarioRepository, JdbcTemplate jdbc, ObjectMapper objectMapper) {
        this.repository = repository; this.empresaRepository = empresaRepository; this.mapper = mapper;
        this.funcionarioRepository = funcionarioRepository; this.cargoRepository = cargoRepository;
        this.usuarioRepository = usuarioRepository; this.jdbc = jdbc; this.objectMapper = objectMapper;
    }

    public DepartamentoContabioResponse create(DepartamentoContabioRequest request) {
        Long empresaId = TenantAccess.require(request.empresaId());
        validate(request, empresaId, null);
        var empresa = empresaRepository.findById(empresaId).orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada"));
        DepartamentoContabio entity = mapper.toEntity(request);
        entity.setEmpresa(empresa); normalize(entity, request);
        if (entity.getAtivo() == null) entity.setAtivo(true);
        if (entity.getOrdem() == null) entity.setOrdem(0);
        entity = repository.saveAndFlush(entity);
        audit(entity, "INSERT", snapshot(entity));
        return response(entity);
    }

    @Transactional(readOnly = true)
    public DepartamentoContabioResponse findById(Long id) { return response(owned(id)); }

    @Transactional(readOnly = true)
    public Page<DepartamentoContabioResponse> findAll(String q, Boolean ativo, String tipo, Pageable pageable) {
        Long empresaId = TenantAccess.requireCurrent();
        if (!List.of(20, 50, 100, 1000).contains(pageable.getPageSize())) invalid("Paginação inválida");
        String query = q == null || q.isBlank() ? null : q.trim();
        String type = tipo == null || tipo.isBlank() ? null : tipo.trim();

        org.springframework.data.jpa.domain.Specification<DepartamentoContabio> spec = (root, cq, cb) -> {
            if (cq != null && !Long.class.equals(cq.getResultType()) && !long.class.equals(cq.getResultType())) {
                root.fetch("empresa", jakarta.persistence.criteria.JoinType.LEFT);
            }
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("empresa").get("id"), empresaId));
            if (query != null && !query.isBlank()) {
                String pattern = "%" + query.toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("descricao")), pattern),
                    cb.like(cb.lower(root.get("codigo")), pattern)
                ));
            }
            if (ativo != null) {
                predicates.add(cb.equal(root.get("ativo"), ativo));
            }
            if (type != null && !type.isBlank()) {
                predicates.add(cb.equal(root.get("tipoOperacao"), type));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        return repository.findAll(spec, pageable).map(this::response);
    }

    @Transactional(readOnly = true)
    public List<DepartamentoContabioResponse> findByEmpresaId(Long empresaId) {
        empresaId = TenantAccess.require(empresaId);
        return repository.findByEmpresaId(empresaId).stream().map(this::response).toList();
    }

    public DepartamentoContabioResponse update(Long id, DepartamentoContabioRequest request) {
        Long empresaId = TenantAccess.require(request.empresaId());
        DepartamentoContabio entity = owned(id);
        if (request.versao() == null || !Objects.equals(request.versao(), entity.getVersao())) conflict("Área foi alterada; recarregue antes de salvar");
        validate(request, empresaId, id);
        Map<String,Object> before = snapshot(entity);
        mapper.updateEntityFromRequest(request, entity); normalize(entity, request);
        entity = repository.saveAndFlush(entity);
        audit(entity, "UPDATE", changes(before, snapshot(entity)));
        return response(entity);
    }

    public void delete(Long id) {
        DepartamentoContabio entity = owned(id);
        if (!Boolean.TRUE.equals(entity.getAtivo())) return;
        entity.setAtivo(false);
        repository.saveAndFlush(entity);
        audit(entity, "UPDATE", Map.of("ativo", false));
    }

    private DepartamentoContabio owned(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantAccess.requireCurrent())
                .orElseThrow(() -> new ResourceNotFoundException("Área não encontrada"));
    }

    private void validate(DepartamentoContabioRequest request, Long empresaId, Long id) {
        String nome = request.descricao().trim(); String codigo = request.codigo().trim();
        if (nome.length() < 2) invalid("O nome da área deve ter pelo menos 2 caracteres");
        boolean duplicateName = id == null ? repository.existsByEmpresaIdAndDescricaoIgnoreCase(empresaId, nome)
                : repository.existsByEmpresaIdAndDescricaoIgnoreCaseAndIdNot(empresaId, nome, id);
        boolean duplicateCode = id == null ? repository.existsByEmpresaIdAndCodigoIgnoreCase(empresaId, codigo)
                : repository.existsByEmpresaIdAndCodigoIgnoreCaseAndIdNot(empresaId, codigo, id);
        if (duplicateName || duplicateCode) conflict("Já existe uma área com este nome ou código");
        if (request.supervisorId() != null) {
            var supervisor = funcionarioRepository.findByIdAndEmpresaId(request.supervisorId(), empresaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Responsável não encontrado"));
            if (supervisor.getStatus() != com.neritech.saas.rh.domain.enums.StatusFuncionario.ATIVO) invalid("Responsável deve estar ativo");
        }
    }

    private static void normalize(DepartamentoContabio entity, DepartamentoContabioRequest request) {
        entity.setDescricao(request.descricao().trim()); entity.setCodigo(request.codigo().trim().toUpperCase(Locale.ROOT));
        entity.setDetalhes(blankToNull(request.detalhes())); entity.setTipoOperacao(blankToNull(request.tipoOperacao()));
    }
    private static String blankToNull(String value) { return value == null || value.isBlank() ? null : value.trim(); }
    private static void invalid(String message) { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, message); }
    private static void conflict(String message) { throw new ResponseStatusException(HttpStatus.CONFLICT, message); }

    private DepartamentoContabioResponse response(DepartamentoContabio entity) {
        Long empresaId = entity.getEmpresa() != null ? entity.getEmpresa().getId() : null;
        if (empresaId != null) {
            entity.setNumeroFuncionarios(funcionarioRepository.countByEmpresaIdAndDepartamentoId(empresaId, entity.getId()));
            entity.setNumeroCargos(cargoRepository.countByEmpresaIdAndAreaId(empresaId, entity.getId()));
        }
        entity.setNumeroServicos(0L);
        return mapper.toResponse(entity);
    }

    private Map<String,Object> snapshot(DepartamentoContabio d) {
        Map<String,Object> map = new LinkedHashMap<>(); map.put("codigo", d.getCodigo()); map.put("nome", d.getDescricao());
        map.put("descricao", d.getDetalhes()); map.put("tipoOperacao", d.getTipoOperacao()); map.put("supervisorId", d.getSupervisorId());
        map.put("ativo", d.getAtivo()); map.put("ordem", d.getOrdem()); map.put("tempoPadraoMinutos", d.getTempoPadraoMinutos()); return map;
    }
    private static Map<String,Object> changes(Map<String,Object> before, Map<String,Object> after) {
        Map<String,Object> result = new LinkedHashMap<>(); after.forEach((k,v) -> { if (!Objects.equals(before.get(k),v)) result.put(k,v); }); return result;
    }
    private void audit(DepartamentoContabio d, String operation, Map<String,?> values) {
        if (values.isEmpty()) return;
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var actor = auth == null ? java.util.Optional.<com.neritech.saas.gestaoUsuarios.domain.Usuario>empty()
                : usuarioRepository.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), d.getEmpresa().getId());
        if (actor.isEmpty()) throw new AccessDeniedException("Autor inválido");
        try { jdbc.update("""
                insert into logs_alteracoes (empresa_id,tabela_afetada,registro_id,operacao,campos_alterados,valores_novos,usuario_responsavel,contexto_operacao,data_alteracao)
                values (?,'departamentos_contabio',?,?,?,?,?,'ADM-007',current_timestamp)
                """, d.getEmpresa().getId(), d.getId(), operation, objectMapper.writeValueAsString(values.keySet()), objectMapper.writeValueAsString(values), actor.get().getId());
        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) { throw new IllegalStateException("Falha ao auditar área", ex); }
    }

    @Transactional(readOnly = true)
    public Page<DepartamentoAlteracaoResponse> history(Long id, int page, int size) {
        DepartamentoContabio d = owned(id);
        if (page < 0 || !List.of(20,50,100).contains(size)) invalid("Paginação inválida");
        var pageable = PageRequest.of(page,size);
        Long total = jdbc.queryForObject("select count(*) from logs_alteracoes where empresa_id=? and tabela_afetada='departamentos_contabio' and registro_id=? and contexto_operacao='ADM-007'", Long.class,d.getEmpresa().getId(),id);
        var content = jdbc.query("""
                select operacao,campos_alterados,valores_novos,usuario_responsavel,data_alteracao from logs_alteracoes
                where empresa_id=? and tabela_afetada='departamentos_contabio' and registro_id=? and contexto_operacao='ADM-007'
                order by data_alteracao desc,id desc limit ? offset ?
                """, (rs,row) -> new DepartamentoAlteracaoResponse(rs.getString(1),rs.getString(2),rs.getString(3),rs.getLong(4),rs.getTimestamp(5).toLocalDateTime()),d.getEmpresa().getId(),id,size,pageable.getOffset());
        return new PageImpl<>(content,pageable,total == null ? 0 : total);
    }
}
