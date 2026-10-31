package com.neritech.saas.financeiro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.financeiro.domain.ComissaoFuncionario;
import com.neritech.saas.financeiro.domain.enums.StatusFatura;
import com.neritech.saas.financeiro.dto.ComissaoFuncionarioRequest;
import com.neritech.saas.financeiro.dto.ComissaoFuncionarioResponse;
import com.neritech.saas.financeiro.mapper.ComissaoFuncionarioMapper;
import com.neritech.saas.financeiro.repository.ComissaoFuncionarioRepository;
import com.neritech.saas.financeiro.repository.FaturaRepository;
import com.neritech.saas.financeiro.repository.ItemFaturaRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ComissaoFuncionarioService {
    private final ComissaoFuncionarioRepository repository;
    private final FuncionarioRepository funcionarioRepository;
    private final FaturaRepository faturaRepository;
    private final ItemFaturaRepository itemFaturaRepository;
    private final ComissaoFuncionarioMapper mapper;
    private final UsuarioRepository usuarios;
    private final JdbcTemplate jdbc;
    private final ObjectMapper json;

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Comissão não encontrada");
    }
    private ComissaoFuncionario owned(Long id, long tenant) {
        return repository.findById(id).filter(c -> tenant == c.getEmpresaId()).orElseThrow(this::notFound);
    }
    private ComissaoFuncionario locked(Long id, long tenant) {
        return repository.findOwnedForUpdate(id, tenant).orElseThrow(this::notFound);
    }
    private long actor(long tenant) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth == null ? java.util.Optional.<com.neritech.saas.gestaoUsuarios.domain.Usuario>empty()
                : usuarios.findByEmailIgnoreCaseAndEmpresaId(auth.getName(), tenant))
                .orElseThrow(() -> new org.springframework.security.access.AccessDeniedException("Autor não autorizado"))
                .getId();
    }
    private void audit(long tenant, long id, long userId, String operation, Map<String, ?> fields) {
        try {
            jdbc.update("""
                insert into logs_alteracoes (empresa_id,tabela_afetada,registro_id,operacao,campos_alterados,
                valores_novos,usuario_responsavel,contexto_operacao,data_alteracao)
                values (?,'fin_comissoes_funcionarios',?,?,?,?,?,'FIN-007',current_timestamp)
                """, tenant, id, operation, json.writeValueAsString(fields.keySet()),
                json.writeValueAsString(fields), userId);
        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            throw new IllegalStateException("Falha ao auditar comissão", ex);
        }
    }
    private void validarPaginacao(Pageable pageable) {
        if (!java.util.List.of(20, 50, 100).contains(pageable.getPageSize()) || pageable.getPageNumber() < 0)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
    }

    @Transactional(readOnly = true)
    public Page<ComissaoFuncionarioResponse> findAll(Long empresaId, Pageable pageable) {
        return findAllByEmpresa(empresaId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ComissaoFuncionarioResponse> findAllByEmpresa(Long empresaId, Pageable pageable) {
        return listar(empresaId, null, null, null, null, null, null, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ComissaoFuncionarioResponse> listar(Long empresaId, String nome, Long funcionarioId,
            Long ordemServicoId, Boolean paga, LocalDate inicio, LocalDate fim, Pageable pageable) {
        long tenant = TenantAccess.require(empresaId);
        validarPaginacao(pageable);
        if (inicio != null && fim != null && inicio.isAfter(fim))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Período inválido");
        if (nome != null && nome.length() > 120)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Busca muito extensa");
        String q = nome == null ? "" : nome.trim().toLowerCase().replace("!", "!!")
                .replace("%", "!%").replace("_", "!_");
        Specification<ComissaoFuncionario> spec = (root, query, cb) -> {
            var rules = new ArrayList<jakarta.persistence.criteria.Predicate>();
            rules.add(cb.equal(root.get("empresaId"), tenant));
            if (!q.isBlank()) {
                rules.add(cb.like(cb.lower(root.join("funcionario", JoinType.LEFT).get("nomeCompleto")),
                        "%" + q + "%", '!'));
            }
            if (funcionarioId != null) rules.add(cb.equal(root.get("funcionario").get("id"), funcionarioId));
            if (ordemServicoId != null) rules.add(cb.equal(root.get("ordemServicoId"), ordemServicoId));
            if (paga != null) rules.add(cb.equal(root.get("paga"), paga));
            if (inicio != null) rules.add(cb.greaterThanOrEqualTo(root.get("dataCompetencia"), inicio));
            if (fim != null) rules.add(cb.lessThanOrEqualTo(root.get("dataCompetencia"), fim));
            return cb.and(rules.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        return repository.findAll(spec, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ComissaoFuncionarioResponse findById(Long id, Long empresaId) {
        return mapper.toResponse(owned(id, TenantAccess.require(empresaId)));
    }

    @Transactional
    public ComissaoFuncionarioResponse create(Long empresaId, ComissaoFuncionarioRequest request) {
        long tenant = TenantAccess.require(empresaId);
        long autor = actor(tenant);
        ComissaoFuncionario entity = mapper.toEntity(request);
        entity.setEmpresaId(tenant);
        entity.setCriadoPor(autor);
        entity.setPaga(false); // pagamento é registrado exclusivamente pela transição auditada
        entity.setDataPagamento(null);
        entity.setValorPago(null);
        vincular(entity, request, tenant);
        entity = repository.saveAndFlush(entity);
        audit(tenant, entity.getId(), autor, "INSERT", Map.of("valorComissao", entity.getValorComissao()));
        return mapper.toResponse(entity);
    }

    private void vincular(ComissaoFuncionario entity, ComissaoFuncionarioRequest request, long tenant) {
        if (request.funcionarioId() != null) {
            entity.setFuncionario(funcionarioRepository.findByIdAndEmpresaId(request.funcionarioId(), tenant)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado")));
        }
        if (request.faturaId() != null) {
            entity.setFatura(faturaRepository.findByIdAndEmpresaId(request.faturaId(), tenant)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fatura não encontrada")));
        } else entity.setFatura(null);
        if (request.itemFaturaId() != null) {
            var item = itemFaturaRepository.findById(request.itemFaturaId())
                    .filter(i -> i.getFatura() != null && tenant == i.getFatura().getEmpresaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item da fatura não encontrado"));
            if (request.faturaId() == null || !request.faturaId().equals(item.getFatura().getId()))
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Item não pertence à fatura selecionada");
            entity.setItemFatura(item);
        } else entity.setItemFatura(null);
    }

    @Transactional
    public ComissaoFuncionarioResponse update(Long id, Long empresaId, ComissaoFuncionarioRequest request) {
        long tenant = TenantAccess.require(empresaId);
        ComissaoFuncionario entity = locked(id, tenant);
        if (Boolean.TRUE.equals(entity.getPaga()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Estorne a quitação antes de alterar a comissão");
        if (entity.getDataAprovacao() != null)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Comissão já aprovada; revisão precisa de fluxo próprio");
        long autor = actor(tenant);
        mapper.updateEntityFromDTO(request, entity);
        entity.setPaga(false);
        vincular(entity, request, tenant);
        entity = repository.saveAndFlush(entity);
        audit(tenant, id, autor, "UPDATE", Map.of("valorComissao", entity.getValorComissao()));
        return mapper.toResponse(entity);
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        TenantAccess.require(empresaId);
        owned(id, empresaId);
        throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Comissões não podem ser excluídas fisicamente; utilize o processo auditado de revisão");
    }

    @Transactional
    public ComissaoFuncionarioResponse aprovar(Long id, Long empresaId) {
        long tenant = TenantAccess.require(empresaId);
        var entity = locked(id, tenant);
        if (Boolean.TRUE.equals(entity.getPaga()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Comissão já quitada");
        if (entity.getDataAprovacao() != null) return mapper.toResponse(entity);
        if (entity.getValorComissao() == null || entity.getValorComissao().signum() < 0)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Valor de comissão inválido");
        long autor = actor(tenant);
        entity.setAprovadaPor(autor);
        entity.setDataAprovacao(LocalDateTime.now());
        entity = repository.saveAndFlush(entity);
        audit(tenant, id, autor, "UPDATE", Map.of("aprovada", true));
        return mapper.toResponse(entity);
    }

    @Transactional
    public ComissaoFuncionarioResponse quitar(Long id, Long empresaId, LocalDate data, String observacao) {
        long tenant = TenantAccess.require(empresaId);
        var entity = locked(id, tenant);
        if (Boolean.TRUE.equals(entity.getPaga()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Comissão já quitada");
        if (entity.getDataAprovacao() == null)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "É necessário aprovar a comissão antes de quitá-la");
        if (entity.getFatura() != null && entity.getFatura().getStatus() != StatusFatura.PAGA)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A fatura vinculada não possui recebimento integral confirmado");
        if (data == null || data.isAfter(LocalDate.now()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Informe data de pagamento válida");
        if (observacao != null && observacao.length() > 2000)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Observação muito extensa");
        long autor = actor(tenant);
        entity.setPaga(true);
        entity.setDataPagamento(data);
        entity.setValorPago(entity.getValorComissao());
        if (observacao != null && !observacao.isBlank()) entity.setObservacoes(observacao.trim());
        entity = repository.saveAndFlush(entity);
        audit(tenant, id, autor, "UPDATE", Map.of("paga", true, "dataPagamento", data.toString()));
        return mapper.toResponse(entity);
    }

    @Transactional
    public ComissaoFuncionarioResponse estornar(Long id, Long empresaId) {
        long tenant = TenantAccess.require(empresaId);
        var entity = locked(id, tenant);
        if (!Boolean.TRUE.equals(entity.getPaga()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Comissão não está quitada");
        long autor = actor(tenant);
        entity.setPaga(false);
        entity.setDataPagamento(null);
        entity.setValorPago(null);
        entity = repository.saveAndFlush(entity);
        audit(tenant, id, autor, "UPDATE", Map.of("paga", false, "estornoQuitacao", true));
        return mapper.toResponse(entity);
    }
}
