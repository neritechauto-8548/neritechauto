package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.Cargo;
import com.neritech.saas.rh.dto.CargoRequest;
import com.neritech.saas.rh.dto.CargoResponse;
import com.neritech.saas.rh.mapper.CargoMapper;
import com.neritech.saas.rh.repository.CargoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.common.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CargoService {
    private final CargoRepository repository;
    private final CargoMapper mapper;
    private final com.neritech.saas.rh.repository.FuncionarioRepository funcionarioRepository;
    private final com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository usuarioRepository;
    private final jakarta.persistence.EntityManager entityManager;
    private final org.springframework.jdbc.core.JdbcTemplate jdbc;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    public Page<CargoResponse> findAll(Long empresaId, String query, Boolean ativo, Long areaId, Pageable pageable) {
        empresaId = TenantAccess.require(empresaId);
        if (!java.util.List.of(20, 50, 100).contains(pageable.getPageSize())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        }
        if (areaId != null) validateArea(areaId, empresaId);
        String q = query == null || query.isBlank() ? "" : query.trim();
        final Long tenantId = empresaId;
        return repository.search(tenantId, q, ativo, areaId, pageable).map(c -> response(c, tenantId));
    }

    public CargoResponse findById(Long id) {
        Cargo cargo = findOwned(id);
        return response(cargo, cargo.getEmpresaId());
    }

    @Transactional
    public CargoResponse create(CargoRequest request) {
        Long empresaId = TenantAccess.require(request.empresaId());
        validate(request, empresaId, null);
        Cargo entity = mapper.toEntity(request);
        normalize(entity, request);
        if (entity.getAtivo() == null) entity.setAtivo(true);
        entity = repository.saveAndFlush(entity);
        audit(entity, "INSERT", fields(entity));
        return response(entity, empresaId);
    }

    @Transactional
    public CargoResponse update(Long id, CargoRequest request) {
        Long empresaId = TenantAccess.require(request.empresaId());
        Cargo entity = findOwned(id);
        if (request.versao() != null && !java.util.Objects.equals(request.versao(), entity.getVersao())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, "Cargo foi alterado; recarregue antes de salvar");
        }
        validate(request, empresaId, id);
        java.util.Map<String, Object> before = fields(entity);
        mapper.updateEntity(request, entity);
        normalize(entity, request);
        entity = repository.saveAndFlush(entity);
        audit(entity, "UPDATE", changed(before, fields(entity)));
        return response(entity, empresaId);
    }

    @Transactional
    public void delete(Long id) {
        Cargo entity = findOwned(id);
        if (!Boolean.TRUE.equals(entity.getAtivo())) return;
        entity.setAtivo(false);
        repository.saveAndFlush(entity);
        audit(entity, "UPDATE", java.util.Map.of("ativo", false));
    }

    private Cargo findOwned(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantAccess.requireCurrent())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));
    }

    private CargoResponse response(Cargo cargo, Long empresaId) {
        cargo.setNumeroFuncionarios(funcionarioRepository.countByEmpresaIdAndCargoId(empresaId, cargo.getId()));
        return mapper.toResponse(cargo);
    }

    private void validate(CargoRequest request, Long empresaId, Long currentId) {
        String nome = request.nome().trim();
        boolean duplicatedName = currentId == null
                ? repository.existsByEmpresaIdAndNomeIgnoreCase(empresaId, nome)
                : repository.existsByEmpresaIdAndNomeIgnoreCaseAndIdNot(empresaId, nome, currentId);
        if (duplicatedName) conflict("Já existe um cargo com este nome nesta empresa");
        String code = request.codigoCbo() == null ? null : request.codigoCbo().trim();
        if (code != null && !code.isBlank()) {
            boolean duplicatedCode = currentId == null
                    ? repository.existsByEmpresaIdAndCodigoCboIgnoreCase(empresaId, code)
                    : repository.existsByEmpresaIdAndCodigoCboIgnoreCaseAndIdNot(empresaId, code, currentId);
            if (duplicatedCode) conflict("Já existe um cargo com este código nesta empresa");
        }
        if (request.areaId() != null) validateArea(request.areaId(), empresaId);
        if (request.cargoSuperiorId() != null) {
            if (java.util.Objects.equals(request.cargoSuperiorId(), currentId)) conflict("Cargo não pode ser superior de si mesmo");
            repository.findByIdAndEmpresaId(request.cargoSuperiorId(), empresaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cargo superior não encontrado"));
        }
        if (request.salarioBaseMinimo() != null && request.salarioBaseMinimo().signum() < 0
                || request.salarioBaseMaximo() != null && request.salarioBaseMaximo().signum() < 0
                || request.salarioBaseMinimo() != null && request.salarioBaseMaximo() != null
                && request.salarioBaseMinimo().compareTo(request.salarioBaseMaximo()) > 0) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY, "Faixa salarial inválida");
        }
        if (!Boolean.TRUE.equals(request.temComissao()) && request.percentualComissaoPadrao() != null
                && request.percentualComissaoPadrao().signum() > 0) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY, "Comissão exige cargo comissionado");
        }
    }

    private static void normalize(Cargo entity, CargoRequest request) {
        entity.setNome(request.nome().trim());
        entity.setCodigoCbo(request.codigoCbo() == null || request.codigoCbo().isBlank()
                ? null : request.codigoCbo().trim());
    }

    private void validateArea(Long areaId, Long empresaId) {
        var area = entityManager.find(com.neritech.saas.empresa.domain.DepartamentoContabio.class, areaId);
        if (area == null || !empresaId.equals(area.getEmpresa().getId()) || !Boolean.TRUE.equals(area.getAtivo())) {
            throw new ResourceNotFoundException("Área não encontrada");
        }
    }

    private static void conflict(String message) {
        throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, message);
    }

    private java.util.Map<String, Object> fields(Cargo c) {
        java.util.Map<String, Object> values = new java.util.LinkedHashMap<>();
        values.put("nome", c.getNome()); values.put("codigo", c.getCodigoCbo());
        values.put("areaId", c.getAreaId()); values.put("nivel", c.getNivelHierarquico());
        values.put("responsabilidades", c.getResponsabilidades()); values.put("ativo", c.getAtivo());
        values.put("temComissao", c.getTemComissao());
        return values;
    }

    private java.util.Map<String, Object> changed(java.util.Map<String, Object> before, java.util.Map<String, Object> after) {
        java.util.Map<String, Object> result = new java.util.LinkedHashMap<>();
        after.forEach((key, value) -> { if (!java.util.Objects.equals(before.get(key), value)) result.put(key, value); });
        return result;
    }

    private void audit(Cargo cargo, String operation, java.util.Map<String, ?> values) {
        if (values.isEmpty()) return;
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        var actor = auth == null ? java.util.Optional.<com.neritech.saas.gestaoUsuarios.domain.Usuario>empty()
                : usuarioRepository.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), cargo.getEmpresaId());
        if (actor.isEmpty()) throw new org.springframework.security.access.AccessDeniedException("Autor inválido");
        try {
            jdbc.update("""
                    insert into logs_alteracoes (empresa_id,tabela_afetada,registro_id,operacao,
                    campos_alterados,valores_novos,usuario_responsavel,contexto_operacao,data_alteracao)
                    values (?,'cargos',?,?,?,?,?,'ADM-005',current_timestamp)
                    """, cargo.getEmpresaId(), cargo.getId(), operation,
                    objectMapper.writeValueAsString(values.keySet()), objectMapper.writeValueAsString(values), actor.get().getId());
        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            throw new IllegalStateException("Falha ao auditar cargo", ex);
        }
    }

    public Page<com.neritech.saas.rh.dto.CargoAlteracaoResponse> history(Long id, int page, int size) {
        Cargo cargo = findOwned(id);
        if (page < 0 || !java.util.List.of(20, 50, 100).contains(size)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        }
        var pageable = org.springframework.data.domain.PageRequest.of(page, size);
        Long total = jdbc.queryForObject("select count(*) from logs_alteracoes where empresa_id=? and tabela_afetada='cargos' and registro_id=? and contexto_operacao='ADM-005'",
                Long.class, cargo.getEmpresaId(), id);
        var content = jdbc.query("""
                select operacao,campos_alterados,valores_novos,usuario_responsavel,data_alteracao
                from logs_alteracoes where empresa_id=? and tabela_afetada='cargos' and registro_id=?
                and contexto_operacao='ADM-005' order by data_alteracao desc,id desc limit ? offset ?
                """, (rs, row) -> new com.neritech.saas.rh.dto.CargoAlteracaoResponse(
                rs.getString(1), rs.getString(2), rs.getString(3), rs.getLong(4), rs.getTimestamp(5).toLocalDateTime()),
                cargo.getEmpresaId(), id, size, pageable.getOffset());
        return new org.springframework.data.domain.PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
