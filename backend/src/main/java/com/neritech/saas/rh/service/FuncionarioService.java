package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.Funcionario;
import com.neritech.saas.rh.domain.Cargo;
import com.neritech.saas.empresa.domain.DepartamentoContabio;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.rh.dto.FuncionarioRequest;
import com.neritech.saas.rh.dto.FuncionarioResponse;
import com.neritech.saas.rh.mapper.FuncionarioMapper;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neritech.saas.util.DocumentoValidator;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final FuncionarioMapper mapper;
    private final com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository usuarioRepository;
    private final UsuarioPerfilHelper usuarioPerfilHelper;
    private final jakarta.persistence.EntityManager entityManager;
    private final org.springframework.jdbc.core.JdbcTemplate jdbc;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    public Page<FuncionarioResponse> findAll(Long empresaId, String query,
            com.neritech.saas.rh.domain.enums.StatusFuncionario status, Long cargoId,
            Long departamentoId, Pageable pageable) {
        if (!java.util.List.of(20, 50, 100).contains(pageable.getPageSize())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        }
        String normalized = query == null || query.isBlank() ? null : query.trim();
        return repository.search(TenantAccess.require(empresaId), normalized, status, cargoId, departamentoId, pageable)
                .map(mapper::toResponse);
    }

    public FuncionarioResponse findById(Long id) {
        return mapper.toResponse(findOwned(id));
    }

    @Transactional
    public FuncionarioResponse create(FuncionarioRequest request) {
        Long empresaId = TenantAccess.require(request.empresaId());
        validateRelations(request, empresaId, null);
        if (repository.existsByEmpresaIdAndMatricula(request.empresaId(), request.matricula())) {
            throw new IllegalArgumentException("Matrícula já existe para esta empresa");
        }
        validarFuncionario(request, null);

        Funcionario entity = mapper.toEntity(request);
        if (entity.getStatus() == null) entity.setStatus(com.neritech.saas.rh.domain.enums.StatusFuncionario.ATIVO);
        entity = repository.save(entity);
        audit(entity, "INSERT", auditFields(entity));
        return mapper.toResponse(entity);
    }

    @Transactional
    public FuncionarioResponse update(Long id, FuncionarioRequest request) {
        Long empresaId = TenantAccess.require(request.empresaId());
        Funcionario entity = findOwned(id);
        if (request.usuarioId() != null && repository.existsByEmpresaIdAndUsuarioIdAndIdNot(
                empresaId, request.usuarioId(), id)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, "Usuário já vinculado a outro funcionário");
        }
        if (request.versao() != null && !java.util.Objects.equals(request.versao(), entity.getVersao())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, "Funcionário foi alterado; recarregue antes de salvar");
        }
        java.util.Map<String, Object> before = auditFields(entity);
        validateRelations(request, empresaId, id);
        validarFuncionario(request, entity);

        mapper.updateEntity(request, entity);
        entity = repository.save(entity);
        audit(entity, "UPDATE", changed(before, auditFields(entity)));
        return mapper.toResponse(entity);
    }


    // Funcionario nao herda TenantEntity: escopo aplicado na consulta SQL.
    private Funcionario findOwned(Long id) {
        return repository.findByIdAndEmpresaId(id, TenantAccess.requireCurrent())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
    }

    private void validateLinkedUser(Funcionario entity) {
        if (entity.getUsuarioId() != null && usuarioRepository.findById(entity.getUsuarioId())
                .filter(user -> entity.getEmpresaId().equals(user.getEmpresaId())).isEmpty()) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
    }

    private void validateRelations(FuncionarioRequest request, Long empresaId, Long currentId) {
        if (request.usuarioId() != null && usuarioRepository.findById(request.usuarioId())
                .filter(usuario -> empresaId.equals(usuario.getEmpresaId())).isEmpty()) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        if (request.usuarioId() != null && (currentId == null
                ? repository.findByEmpresaIdAndUsuarioId(empresaId, request.usuarioId()).isPresent()
                : repository.existsByEmpresaIdAndUsuarioIdAndIdNot(empresaId, request.usuarioId(), currentId))) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, "Usuário já vinculado a outro funcionário");
        }
        if (request.cargoId() != null) {
            Cargo cargo = entityManager.find(Cargo.class, request.cargoId());
            if (cargo == null || !empresaId.equals(cargo.getEmpresaId()) || !Boolean.TRUE.equals(cargo.getAtivo())) {
                throw new ResourceNotFoundException("Cargo não encontrado");
            }
            if (request.comissaoPercentual() != null && request.comissaoPercentual().signum() > 0
                    && !Boolean.TRUE.equals(cargo.getTemComissao())) {
                throw new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY,
                        "O cargo selecionado não permite comissionamento");
            }
        }
        if (request.departamentoId() != null) {
            DepartamentoContabio departamento = entityManager.find(DepartamentoContabio.class, request.departamentoId());
            if (departamento == null || !empresaId.equals(departamento.getEmpresa().getId())
                    || !Boolean.TRUE.equals(departamento.getAtivo())) {
                throw new ResourceNotFoundException("Departamento não encontrado");
            }
        }
    }

    private void validarFuncionario(FuncionarioRequest request, Funcionario entity) {
        // Validação do CPF
        if (request.cpf() != null && !request.cpf().isBlank()) {
            if (!DocumentoValidator.isValidCpf(request.cpf())) {
                throw new IllegalArgumentException("CPF inválido");
            }
            boolean isNewOrChangedCpf = entity == null || !request.cpf().equals(entity.getCpf());
            if (isNewOrChangedCpf && repository.existsByEmpresaIdAndCpf(request.empresaId(), request.cpf())) {
                throw new IllegalArgumentException("CPF já cadastrado para esta empresa");
            }
        }

        // Validação de Idade Mínima (16 anos)
        if (request.dataNascimento() != null) {
            LocalDate today = LocalDate.now();
            int age = java.time.Period.between(request.dataNascimento(), today).getYears();
            if (age < 16) {
                throw new IllegalArgumentException("O colaborador deve ter pelo menos 16 anos");
            }
        }

        // Validação de Datas de Vínculo
        if (request.dataDemissao() != null && request.dataAdmissao() != null) {
            if (request.dataDemissao().isBefore(request.dataAdmissao())) {
                throw new IllegalArgumentException("A data de demissão não pode ser anterior à data de admissão");
            }
        }

        // Validação de Motivo de Inativação
        if ((request.status() == com.neritech.saas.rh.domain.enums.StatusFuncionario.INATIVO ||
             request.status() == com.neritech.saas.rh.domain.enums.StatusFuncionario.DEMITIDO ||
             request.status() == com.neritech.saas.rh.domain.enums.StatusFuncionario.AFASTADO) &&
            (request.motivoInativacao() == null || request.motivoInativacao().isBlank())) {
                throw new IllegalArgumentException("O motivo de inativação é obrigatório para o status selecionado");
        }

        // Validação Financeira
        if (request.salarioBase() != null && request.salarioBase().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O salário base não pode ser negativo");
        }
        if (request.valeTransporte() != null && request.valeTransporte().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O vale transporte não pode ser negativo");
        }
        if (request.valeAlimentacao() != null && request.valeAlimentacao().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O vale alimentação não pode ser negativo");
        }

        // Validação da CNH
        if (request.cnhNumero() != null && !request.cnhNumero().isBlank()) {
            if (request.cnhCategoria() == null || request.cnhCategoria().isBlank()) {
                throw new IllegalArgumentException("A categoria da CNH é obrigatória quando o número é informado");
            }
            if (request.cnhValidade() != null && request.cnhValidade().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("A CNH informada está vencida");
            }
        }

        // Validação de Endereço
        if (request.cep() == null || request.cep().isBlank()) {
            throw new IllegalArgumentException("CEP é obrigatório");
        }
        String cleanCep = request.cep().replaceAll("\\D", "");
        if (cleanCep.length() != 8) {
            throw new IllegalArgumentException("CEP inválido");
        }
        if (request.logradouro() == null || request.logradouro().isBlank()) {
            throw new IllegalArgumentException("Logradouro é obrigatório");
        }
        if (request.bairro() == null || request.bairro().isBlank()) {
            throw new IllegalArgumentException("Bairro é obrigatório");
        }
        if (request.cidade() == null || request.cidade().isBlank()) {
            throw new IllegalArgumentException("Cidade é obrigatória");
        }
        if (request.estado() == null || request.estado().isBlank()) {
            throw new IllegalArgumentException("UF é obrigatória");
        }

        // Validação de E-mail Pessoal
        if (request.emailPessoal() != null && !request.emailPessoal().isBlank()) {
            if (!request.emailPessoal().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                throw new IllegalArgumentException("E-mail pessoal inválido");
            }
        }
    }

    @Transactional
    public void delete(Long id) {
        Funcionario entity = findOwned(id);
        if (entity.getStatus() == com.neritech.saas.rh.domain.enums.StatusFuncionario.INATIVO
                || entity.getStatus() == com.neritech.saas.rh.domain.enums.StatusFuncionario.DEMITIDO) return;
        entity.setStatus(com.neritech.saas.rh.domain.enums.StatusFuncionario.INATIVO);
        entity.setMotivoInativacao("Inativado pela administração");
        repository.save(entity);
        audit(entity, "UPDATE", java.util.Map.of("status", "INATIVO"));
    }

    private java.util.Map<String, Object> auditFields(Funcionario f) {
        java.util.Map<String, Object> values = new java.util.LinkedHashMap<>();
        values.put("matricula", f.getMatricula()); values.put("nomeCompleto", f.getNomeCompleto());
        values.put("cargoId", f.getCargoId()); values.put("departamentoId", f.getDepartamentoId());
        values.put("usuarioId", f.getUsuarioId()); values.put("status", f.getStatus());
        values.put("dataAdmissao", f.getDataAdmissao()); values.put("dataDemissao", f.getDataDemissao());
        values.put("comissaoPercentual", f.getComissaoPercentual());
        return values;
    }

    private java.util.Map<String, Object> changed(java.util.Map<String, Object> before, java.util.Map<String, Object> after) {
        java.util.Map<String, Object> values = new java.util.LinkedHashMap<>();
        after.forEach((key, value) -> { if (!java.util.Objects.equals(before.get(key), value)) values.put(key, value); });
        return values;
    }

    private void audit(Funcionario employee, String operation, java.util.Map<String, ?> values) {
        if (values.isEmpty()) return;
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        var actor = auth == null ? java.util.Optional.<com.neritech.saas.gestaoUsuarios.domain.Usuario>empty()
                : usuarioRepository.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), employee.getEmpresaId());
        if (actor.isEmpty()) throw new org.springframework.security.access.AccessDeniedException("Autor inválido");
        try {
            jdbc.update("""
                    insert into logs_alteracoes (empresa_id,tabela_afetada,registro_id,operacao,
                    campos_alterados,valores_novos,usuario_responsavel,contexto_operacao,data_alteracao)
                    values (?,'funcionarios',?,?,?,?,?,'ADM-004',current_timestamp)
                    """, employee.getEmpresaId(), employee.getId(), operation,
                    objectMapper.writeValueAsString(values.keySet()), objectMapper.writeValueAsString(values), actor.get().getId());
        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            throw new IllegalStateException("Falha ao auditar funcionário", ex);
        }
    }

    public org.springframework.data.domain.Page<com.neritech.saas.rh.dto.FuncionarioAlteracaoResponse> history(
            Long id, int page, int size) {
        Funcionario owned = findOwned(id);
        if (page < 0 || !java.util.List.of(20, 50, 100).contains(size)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        }
        var pageable = org.springframework.data.domain.PageRequest.of(page, size);
        Long total = jdbc.queryForObject("select count(*) from logs_alteracoes where empresa_id=? and tabela_afetada='funcionarios' and registro_id=? and contexto_operacao='ADM-004'",
                Long.class, owned.getEmpresaId(), id);
        var content = jdbc.query("""
                select operacao,campos_alterados,valores_novos,usuario_responsavel,data_alteracao
                from logs_alteracoes where empresa_id=? and tabela_afetada='funcionarios'
                and registro_id=? and contexto_operacao='ADM-004'
                order by data_alteracao desc,id desc limit ? offset ?
                """, (rs, row) -> new com.neritech.saas.rh.dto.FuncionarioAlteracaoResponse(
                rs.getString(1), rs.getString(2), rs.getString(3), rs.getLong(4), rs.getTimestamp(5).toLocalDateTime()),
                owned.getEmpresaId(), id, size, pageable.getOffset());
        return new org.springframework.data.domain.PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    private boolean hasReferences(Long id) {
        // Check comissoes_funcionarios
        Number countComissoes = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM comissoes WHERE funcionario_id = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (countComissoes.longValue() > 0) return true;

        // Check reclamacoes_garantia
        Number countReclamacoes = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM gar_reclamacoes_garantia WHERE tecnico_responsavel_id = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (countReclamacoes.longValue() > 0) return true;

        // Check resolucoes_garantia
        Number countResolucoes = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM gar_resolucoes_garantia WHERE funcionario_executor_id = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (countResolucoes.longValue() > 0) return true;

        // Check ferias_funcionarios (substituido_por)
        Number countFeriasSubst = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM ferias_funcionarios WHERE funcionario_id = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (countFeriasSubst.longValue() > 0) return true;

        // Check avaliacoes_funcionarios (avaliador_id)
        Number countAvaliacoesAvaliador = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM avaliacoes_funcionarios WHERE avaliador_id = :id OR funcionario_id = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (countAvaliacoesAvaliador.longValue() > 0) return true;

        // Check treinamentos (instrutor_interno_id)
        Number countTreinamentos = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM treinamentos WHERE instrutor_interno_id = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (countTreinamentos.longValue() > 0) return true;

        // Check logs_sistema (responsavel_resolucao)
        Number countLogs = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM logs_sistema WHERE responsavel_resolucao = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (countLogs.longValue() > 0) return true;

        return false;
    }

    public FuncionarioResponse findByUsuarioId(Long empresaId, Long usuarioId) {
        return repository.findByEmpresaIdAndUsuarioId(TenantAccess.require(empresaId), usuarioId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não vinculado a este usuário"));
    }

    public void validateFotoUpload(Long id) {
        Funcionario entity = findOwned(id);
        validateLinkedUser(entity);
    }

    @Transactional
    public FuncionarioResponse updateFotoPath(Long id, String fotoPath) {
        Funcionario entity = findOwned(id);
        validateLinkedUser(entity);
        entity.setFotoFuncionarioUrl(fotoPath);
        entity = repository.save(entity);

        // Update the user profile avatar if a user is linked
        if (entity.getUsuarioId() != null) {
            String baseUrl = (fotoPath == null || fotoPath.isBlank()) ? null : "/api/v1/rh/funcionarios/" + id + "/foto?t=" + System.currentTimeMillis();
            usuarioPerfilHelper.updateAvatar(entity.getUsuarioId(), entity.getEmpresaId(), baseUrl);
        }

        return mapper.toResponse(entity);
    }
}
