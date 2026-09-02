package com.neritech.saas.ordemservico.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.exception.ApiException;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.ordemservico.domain.ItemOSProduto;
import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.domain.OSAdditionalRequest;
import com.neritech.saas.ordemservico.domain.OSChecklistItem;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.WorkSession;
import com.neritech.saas.ordemservico.domain.enums.StatusExecucao;
import com.neritech.saas.ordemservico.domain.enums.WorkSessionStatus;
import com.neritech.saas.ordemservico.dto.OSClosureModels;
import com.neritech.saas.ordemservico.repository.FotoOSRepository;
import com.neritech.saas.ordemservico.repository.ItemOSProdutoRepository;
import com.neritech.saas.ordemservico.repository.ItemOSServicoRepository;
import com.neritech.saas.ordemservico.repository.OSAdditionalRequestRepository;
import com.neritech.saas.ordemservico.repository.OSChecklistItemRepository;
import com.neritech.saas.ordemservico.repository.WorkSessionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Owner do fluxo canônico de revisão e conclusão operacional da OS.
 *
 * RN-AUTO-OS-171+: guardas são calculadas no backend.
 * A conclusão operacional apenas congela um snapshot auditável. Ela NÃO executa
 * baixa financeira, criação de fatura, contas a receber ou emissão fiscal.
 */
@Service
public class OSClosureService {

    private static final String COMPLETE_OPERATIONALLY = "COMPLETE_OPERATIONALLY";
    private static final int MAX_IDEMPOTENCY_KEY_LENGTH = 128;
    private static final Set<WorkSessionStatus> OPEN_SESSION_STATUSES =
            EnumSet.of(WorkSessionStatus.EM_EXECUCAO, WorkSessionStatus.PAUSADA);
    private static final Set<OSAdditionalRequest.Status> OPEN_ADDITIONAL_STATUSES = EnumSet.of(
            OSAdditionalRequest.Status.RASCUNHO,
            OSAdditionalRequest.Status.PRONTA_PARA_ENVIO,
            OSAdditionalRequest.Status.PENDENTE,
            OSAdditionalRequest.Status.VISUALIZADA,
            OSAdditionalRequest.Status.PARCIAL);

    private final EntityManager entityManager;
    private final ItemOSServicoRepository servicoRepository;
    private final ItemOSProdutoRepository produtoRepository;
    private final OSChecklistItemRepository checklistRepository;
    private final OSAdditionalRequestRepository additionalRepository;
    private final WorkSessionRepository workSessionRepository;
    private final FotoOSRepository fotoOSRepository;
    private final UsuarioRepository usuarioRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public OSClosureService(
            EntityManager entityManager,
            ItemOSServicoRepository servicoRepository,
            ItemOSProdutoRepository produtoRepository,
            OSChecklistItemRepository checklistRepository,
            OSAdditionalRequestRepository additionalRepository,
            WorkSessionRepository workSessionRepository,
            FotoOSRepository fotoOSRepository,
            UsuarioRepository usuarioRepository,
            JdbcTemplate jdbcTemplate,
            ObjectMapper objectMapper) {
        this.entityManager = entityManager;
        this.servicoRepository = servicoRepository;
        this.produtoRepository = produtoRepository;
        this.checklistRepository = checklistRepository;
        this.additionalRepository = additionalRepository;
        this.workSessionRepository = workSessionRepository;
        this.fotoOSRepository = fotoOSRepository;
        this.usuarioRepository = usuarioRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public OSClosureModels.Review review(Long ordemServicoId) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        OrdemServico ordemServico = requireOwnedOrder(ordemServicoId, tenantId, false);
        return buildReview(ordemServico, tenantId, findClosure(tenantId, ordemServicoId));
    }

    @Transactional(readOnly = true)
    public OSClosureModels.Review validate(Long ordemServicoId) {
        return review(ordemServicoId);
    }

    @Transactional
    public OSClosureModels.CommandResult completeOperationally(
            Long ordemServicoId,
            String ifMatch,
            String rawIdempotencyKey) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        Usuario actor = requireAuthenticatedUser(tenantId);
        int expectedVersion = requireExpectedVersion(ifMatch);
        String idempotencyKey = normalizeIdempotencyKey(rawIdempotencyKey);

        OrdemServico ordemServico = requireOwnedOrder(ordemServicoId, tenantId, true);
        String canonicalRequest = ordemServicoId + "|" + expectedVersion;
        CommandReservation reservation = reserveCommand(
                tenantId,
                actor.getId(),
                ordemServicoId,
                idempotencyKey,
                canonicalRequest);

        if (!reservation.created()) {
            return resultFromSnapshot(tenantId, ordemServicoId, reservation.resultSnapshotId());
        }

        requireVersion(ordemServico, expectedVersion);

        ClosureRow existingClosure = findClosure(tenantId, ordemServicoId);
        if (existingClosure != null) {
            throw new ApiException(
                    "A Ordem de Serviço já foi concluída operacionalmente.",
                    HttpStatus.CONFLICT,
                    "OS_ALREADY_COMPLETED");
        }

        OSClosureModels.Review review = buildReview(ordemServico, tenantId, null);
        if (!review.readyToComplete()) {
            String firstBlocker = review.guards().stream()
                    .filter(guard -> "BLOQUEIO".equals(guard.status()))
                    .map(OSClosureModels.Guard::message)
                    .findFirst()
                    .orElse("Existem guardas pendentes para o fechamento operacional.");
            throw new ApiException(firstBlocker, HttpStatus.UNPROCESSABLE_ENTITY, "OS_CLOSURE_GUARD_FAILED");
        }

        String snapshotJson = serializeReview(review);
        Long snapshotId = jdbcTemplate.queryForObject("""
                INSERT INTO os_operational_closures
                    (empresa_id, ordem_servico_id, aggregate_version, snapshot_json, completed_by_user_id)
                VALUES (?, ?, ?, CAST(? AS jsonb), ?)
                RETURNING id
                """,
                Long.class,
                tenantId,
                ordemServicoId,
                ordemServico.getVersao(),
                snapshotJson,
                actor.getId());

        if (snapshotId == null) {
            throw new IllegalStateException("O snapshot do fechamento operacional não pôde ser persistido.");
        }

        int completed = jdbcTemplate.update("""
                UPDATE os_closure_commands
                   SET result_snapshot_id = ?, completed_at = CURRENT_TIMESTAMP
                 WHERE id = ? AND result_snapshot_id IS NULL
                """, snapshotId, reservation.commandId());
        if (completed != 1) {
            throw new IllegalStateException("Não foi possível concluir a reserva idempotente do fechamento.");
        }

        return resultFromSnapshot(tenantId, ordemServicoId, snapshotId);
    }

    private OSClosureModels.Review buildReview(
            OrdemServico ordemServico,
            Long tenantId,
            ClosureRow existingClosure) {
        List<OSClosureModels.Guard> guards = new ArrayList<>();
        List<String> partialSources = new ArrayList<>();
        Long ordemServicoId = ordemServico.getId();

        boolean cancelled = ordemServico.getStatus() != null
                && Boolean.TRUE.equals(ordemServico.getStatus().getCancelaOS());
        guards.add(cancelled
                ? block("OS_CANCELLED", "Estado da OS", "A OS está cancelada e não pode ser concluída operacionalmente.", "Operação", route(ordemServicoId))
                : ok("OS_STATE_VALID", "Estado da OS", "A OS não está cancelada.", "Operação", route(ordemServicoId)));

        boolean customerApprovalPending = ordemServico.getStatus() != null
                && Boolean.TRUE.equals(ordemServico.getStatus().getExigeAprovacao())
                && !Boolean.TRUE.equals(ordemServico.getAprovadoCliente());
        guards.add(customerApprovalPending
                ? block("OS_CUSTOMER_APPROVAL_PENDING", "Aprovação do cliente", "A aprovação obrigatória do cliente ainda não foi registrada.", "Atendimento", route(ordemServicoId) + "/adicionais")
                : ok("OS_CUSTOMER_APPROVAL_OK", "Aprovação do cliente", "Não há aprovação obrigatória pendente no estado atual.", "Atendimento", route(ordemServicoId)));

        boolean hasResponsible = ordemServico.getMecanicoResponsavelId() != null
                || ordemServico.getConsultorResponsavelId() != null;
        guards.add(hasResponsible
                ? ok("OS_RESPONSIBLE_OK", "Responsável", "A OS possui responsável associado.", "Operação", route(ordemServicoId))
                : block("OS_RESPONSIBLE_PENDING", "Responsável", "Associe um responsável antes do fechamento operacional.", "Operação", route(ordemServicoId)));

        List<WorkSession> sessions = workSessionRepository
                .findByOrdemServicoIdAndEmpresaIdOrderByStartedAtDesc(ordemServicoId, tenantId);
        long activeSessions = sessions.stream().filter(session -> OPEN_SESSION_STATUSES.contains(session.getStatus())).count();
        guards.add(activeSessions == 0
                ? ok("OS_SESSIONS_CLOSED", "Sessões de execução", "Nenhuma sessão produtiva está aberta.", "Execução", route(ordemServicoId) + "/execucao")
                : block("OS_ACTIVE_SESSION_EXISTS", "Sessões de execução", activeSessions + " sessão(ões) produtiva(s) ainda está(ão) aberta(s).", "Execução", route(ordemServicoId) + "/execucao"));

        List<ItemOSServico> services = servicoRepository
                .findByOrdemServico_IdAndOrdemServico_EmpresaId(ordemServicoId, tenantId);
        List<ItemOSServico> approvedServices = services.stream()
                .filter(item -> Boolean.TRUE.equals(item.getAprovadoCliente()))
                .toList();
        long unfinishedServices = approvedServices.stream()
                .filter(item -> item.getStatusExecucao() != StatusExecucao.CONCLUIDO
                        && item.getStatusExecucao() != StatusExecucao.CANCELADO)
                .count();
        guards.add(unfinishedServices == 0
                ? ok("OS_SCOPE_COMPLETED", "Escopo de serviços", "Todos os serviços autorizados possuem resultado terminal.", "Execução", route(ordemServicoId) + "/execucao")
                : block("OS_SCOPE_INCOMPLETE", "Escopo de serviços", unfinishedServices + " serviço(s) autorizado(s) ainda não possui(em) resultado terminal.", "Execução", route(ordemServicoId) + "/execucao"));

        boolean executionClosed = ordemServico.getDataFimExecucao() != null;
        guards.add(executionClosed
                ? ok("OS_EXECUTION_COMPLETED", "Execução da OS", "A execução da OS foi encerrada.", "Execução", route(ordemServicoId) + "/execucao")
                : block("OS_EXECUTION_INCOMPLETE", "Execução da OS", "A execução geral da OS ainda não foi encerrada.", "Execução", route(ordemServicoId) + "/execucao"));

        List<OSChecklistItem> checklistItems = checklistRepository
                .findByOrdemServico_IdAndOrdemServico_EmpresaId(ordemServicoId, tenantId);
        long incompleteChecklist = checklistItems.stream().filter(item -> !Boolean.TRUE.equals(item.getFeito())).count();
        if (checklistItems.isEmpty()) {
            guards.add(alert("OS_CHECKLIST_NOT_ATTACHED", "Checklist e qualidade", "Nenhum checklist foi anexado. A obrigatoriedade depende da política da oficina.", "Qualidade", route(ordemServicoId) + "/checklists"));
        } else if (incompleteChecklist > 0) {
            guards.add(block("OS_CHECKLIST_INCOMPLETE", "Checklist e qualidade", incompleteChecklist + " item(ns) de checklist ainda está(ão) pendente(s).", "Qualidade", route(ordemServicoId) + "/checklists"));
        } else {
            guards.add(ok("OS_CHECKLIST_COMPLETE", "Checklist e qualidade", "Todos os itens do checklist anexado foram concluídos.", "Qualidade", route(ordemServicoId) + "/checklists"));
        }

        List<OSAdditionalRequest> additionalRequests = additionalRepository
                .findByOrdemServicoIdAndEmpresaIdOrderByDataCadastroDesc(ordemServicoId, tenantId);
        long pendingAdditionals = additionalRequests.stream()
                .filter(request -> OPEN_ADDITIONAL_STATUSES.contains(request.getStatus()))
                .count();
        guards.add(pendingAdditionals == 0
                ? ok("OS_ADDITIONALS_DECIDED", "Adicionais", "Não há solicitação adicional aguardando decisão.", "Atendimento", route(ordemServicoId) + "/adicionais")
                : block("OS_ADDITIONAL_PENDING", "Adicionais", pendingAdditionals + " solicitação(ões) adicional(is) ainda exige(m) decisão.", "Atendimento", route(ordemServicoId) + "/adicionais"));

        List<ItemOSProduto> products = produtoRepository.findByOrdemServicoId(ordemServicoId).stream()
                .filter(item -> item.getOrdemServico() != null && tenantId.equals(item.getOrdemServico().getEmpresaId()))
                .toList();
        List<ItemOSProduto> approvedProducts = products.stream()
                .filter(item -> Boolean.TRUE.equals(item.getAprovadoCliente()))
                .toList();
        long unreconciledParts = approvedProducts.stream().filter(this::isPartUnreconciled).count();
        guards.add(unreconciledParts == 0
                ? ok("OS_PARTS_RECONCILED", "Peças", "As quantidades aprovadas estão conciliadas entre utilização e devolução registradas.", "Estoque", route(ordemServicoId) + "/pecas")
                : block("OS_PARTS_NOT_RECONCILED", "Peças", unreconciledParts + " peça(s) aprovada(s) ainda possui(em) quantidade sem conciliação de uso/devolução.", "Estoque", route(ordemServicoId) + "/pecas"));

        int evidenceCount = fotoOSRepository
                .findByOrdemServicoIdAndEmpresaIdOrderByIdAsc(ordemServicoId, tenantId)
                .size();
        guards.add(evidenceCount > 0
                ? ok("OS_EVIDENCE_AVAILABLE", "Evidências", evidenceCount + " evidência(s) registrada(s) na OS.", "Qualidade", route(ordemServicoId) + "/fotos")
                : alert("OS_EVIDENCE_NOT_ATTACHED", "Evidências", "Nenhuma evidência foi anexada. Este alerta não bloqueia enquanto não houver política obrigatória configurada.", "Qualidade", route(ordemServicoId) + "/fotos"));

        boolean alreadyCompleted = existingClosure != null;
        boolean hasBlocker = guards.stream().anyMatch(guard -> "BLOQUEIO".equals(guard.status()));
        boolean ready = !alreadyCompleted && !hasBlocker && partialSources.isEmpty();

        return new OSClosureModels.Review(
                ordemServicoId,
                ordemServico.getNumeroOS(),
                ordemServico.getVersao(),
                alreadyCompleted ? "CONCLUIDA_OPERACIONAL" : hasBlocker ? "PENDENCIA_CORRECAO" : "EM_REVISAO",
                ready,
                alreadyCompleted,
                List.copyOf(guards),
                List.copyOf(partialSources),
                existingClosure != null ? existingClosure.snapshotId() : null,
                existingClosure != null ? existingClosure.completedAt() : null);
    }

    private boolean isPartUnreconciled(ItemOSProduto item) {
        BigDecimal approvedQuantity = defaultMoney(item.getQuantidade());
        BigDecimal used = defaultMoney(item.getQuantidadeUtilizada());
        BigDecimal returned = defaultMoney(item.getQuantidadeDevolvida());
        return used.add(returned).compareTo(approvedQuantity) < 0;
    }

    private OSClosureModels.CommandResult resultFromSnapshot(Long tenantId, Long ordemServicoId, Long snapshotId) {
        if (snapshotId == null) {
            throw new ApiException(
                    "O comando de fechamento anterior ainda está sendo processado.",
                    HttpStatus.CONFLICT,
                    "OS_IDEMPOTENCY_IN_PROGRESS");
        }
        List<SnapshotResultRow> rows = jdbcTemplate.query("""
                SELECT c.id, c.aggregate_version, c.completed_at, o.numero_os
                  FROM os_operational_closures c
                  JOIN ordens_servico o ON o.id = c.ordem_servico_id AND o.empresa_id = c.empresa_id
                 WHERE c.id = ? AND c.empresa_id = ? AND c.ordem_servico_id = ?
                """,
                (rs, rowNum) -> new SnapshotResultRow(
                        rs.getLong("id"),
                        rs.getInt("aggregate_version"),
                        rs.getTimestamp("completed_at").toLocalDateTime(),
                        rs.getString("numero_os")),
                snapshotId,
                tenantId,
                ordemServicoId);
        if (rows.size() != 1) {
            throw new EntityNotFoundException("Snapshot de fechamento operacional não encontrado.");
        }
        SnapshotResultRow row = rows.get(0);
        return new OSClosureModels.CommandResult(
                ordemServicoId,
                row.numeroOS(),
                "CONCLUIDA_OPERACIONAL",
                row.aggregateVersion(),
                row.snapshotId(),
                row.completedAt(),
                null);
    }

    private ClosureRow findClosure(Long tenantId, Long ordemServicoId) {
        List<ClosureRow> rows = jdbcTemplate.query("""
                SELECT id, aggregate_version, completed_at
                  FROM os_operational_closures
                 WHERE empresa_id = ? AND ordem_servico_id = ?
                """,
                (rs, rowNum) -> new ClosureRow(
                        rs.getLong("id"),
                        rs.getInt("aggregate_version"),
                        rs.getTimestamp("completed_at").toLocalDateTime()),
                tenantId,
                ordemServicoId);
        return rows.isEmpty() ? null : rows.get(0);
    }

    private OrdemServico requireOwnedOrder(Long ordemServicoId, Long tenantId, boolean lock) {
        var query = entityManager.createQuery("""
                SELECT o FROM OrdemServico o
                 WHERE o.id = :id AND o.empresaId = :tenantId
                """, OrdemServico.class)
                .setParameter("id", ordemServicoId)
                .setParameter("tenantId", tenantId);
        if (lock) {
            query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        }
        return query.getResultStream().findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ordem de serviço não encontrada para a empresa autenticada"));
    }

    private Usuario requireAuthenticatedUser(Long tenantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken
                || authentication.getName() == null
                || authentication.getName().isBlank()) {
            throw new AccessDeniedException("Usuário autenticado não disponível para o fechamento da OS.");
        }
        return usuarioRepository.findByEmailIgnoreCaseAndEmpresaId(authentication.getName(), tenantId)
                .orElseThrow(() -> new AccessDeniedException(
                        "Usuário autenticado não pertence à empresa da Ordem de Serviço."));
    }

    private CommandReservation reserveCommand(
            Long tenantId,
            Long actorUserId,
            Long ordemServicoId,
            String idempotencyKey,
            String canonicalRequest) {
        String requestHash = sha256(canonicalRequest);
        Long insertedId = jdbcTemplate.query("""
                INSERT INTO os_closure_commands
                    (empresa_id, actor_user_id, command, ordem_servico_id, idempotency_key, request_hash)
                VALUES (?, ?, ?, ?, ?, ?)
                ON CONFLICT (empresa_id, actor_user_id, command, idempotency_key) DO NOTHING
                RETURNING id
                """,
                rs -> rs.next() ? rs.getLong("id") : null,
                tenantId,
                actorUserId,
                COMPLETE_OPERATIONALLY,
                ordemServicoId,
                idempotencyKey,
                requestHash);

        if (insertedId != null) {
            return new CommandReservation(insertedId, null, true);
        }

        List<ExistingCommand> commands = jdbcTemplate.query("""
                SELECT id, ordem_servico_id, request_hash, result_snapshot_id
                  FROM os_closure_commands
                 WHERE empresa_id = ? AND actor_user_id = ? AND command = ? AND idempotency_key = ?
                """,
                (rs, rowNum) -> new ExistingCommand(
                        rs.getLong("id"),
                        rs.getLong("ordem_servico_id"),
                        rs.getString("request_hash"),
                        rs.getObject("result_snapshot_id", Long.class)),
                tenantId,
                actorUserId,
                COMPLETE_OPERATIONALLY,
                idempotencyKey);
        if (commands.size() != 1) {
            throw new IllegalStateException("Reserva idempotente do fechamento não pôde ser recuperada.");
        }

        ExistingCommand existing = commands.get(0);
        if (!Objects.equals(existing.ordemServicoId(), ordemServicoId)
                || !Objects.equals(existing.requestHash(), requestHash)) {
            throw new ApiException(
                    "A Idempotency-Key já foi usada para outro fechamento operacional.",
                    HttpStatus.CONFLICT,
                    "OS_IDEMPOTENCY_CONFLICT");
        }
        if (existing.resultSnapshotId() == null) {
            throw new ApiException(
                    "O comando anterior com esta Idempotency-Key ainda está sendo processado.",
                    HttpStatus.CONFLICT,
                    "OS_IDEMPOTENCY_IN_PROGRESS");
        }
        return new CommandReservation(existing.id(), existing.resultSnapshotId(), false);
    }

    private int requireExpectedVersion(String rawIfMatch) {
        if (rawIfMatch == null || rawIfMatch.isBlank()) {
            throw new ApiException(
                    "If-Match é obrigatório para concluir operacionalmente a OS.",
                    HttpStatus.PRECONDITION_REQUIRED,
                    "OS_VERSION_REQUIRED");
        }
        String normalized = rawIfMatch.trim();
        if (normalized.startsWith("W/")) normalized = normalized.substring(2).trim();
        normalized = normalized.replace("\"", "");
        try {
            int version = Integer.parseInt(normalized);
            if (version < 0) throw new NumberFormatException();
            return version;
        } catch (NumberFormatException exception) {
            throw new ApiException(
                    "If-Match deve conter a versão numérica da Ordem de Serviço.",
                    HttpStatus.PRECONDITION_FAILED,
                    "OS_VERSION_CONFLICT");
        }
    }

    private void requireVersion(OrdemServico ordemServico, int expectedVersion) {
        if (ordemServico.getVersao() == null || ordemServico.getVersao() != expectedVersion) {
            throw new ApiException(
                    "A Ordem de Serviço foi alterada. Atualize a revisão antes de concluir.",
                    HttpStatus.CONFLICT,
                    "OS_VERSION_CONFLICT");
        }
    }

    private String normalizeIdempotencyKey(String rawKey) {
        if (rawKey == null || rawKey.isBlank()) {
            throw new ApiException(
                    "Idempotency-Key é obrigatória para concluir operacionalmente a OS.",
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "OS_IDEMPOTENCY_KEY_REQUIRED");
        }
        String normalized = rawKey.trim();
        if (normalized.length() > MAX_IDEMPOTENCY_KEY_LENGTH) {
            throw new ApiException(
                    "Idempotency-Key deve possuir no máximo 128 caracteres.",
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "OS_IDEMPOTENCY_KEY_INVALID");
        }
        return normalized;
    }

    private String serializeReview(OSClosureModels.Review review) {
        try {
            return objectMapper.writeValueAsString(review);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Não foi possível serializar o snapshot operacional da OS.", exception);
        }
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 indisponível no runtime.", exception);
        }
    }

    private BigDecimal defaultMoney(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private String route(Long ordemServicoId) {
        return "/ordens-servico/" + ordemServicoId;
    }

    private OSClosureModels.Guard ok(String code, String label, String message, String owner, String route) {
        return new OSClosureModels.Guard(code, label, "OK", message, owner, route, false);
    }

    private OSClosureModels.Guard alert(String code, String label, String message, String owner, String route) {
        return new OSClosureModels.Guard(code, label, "ALERTA", message, owner, route, false);
    }

    private OSClosureModels.Guard block(String code, String label, String message, String owner, String route) {
        return new OSClosureModels.Guard(code, label, "BLOQUEIO", message, owner, route, false);
    }

    private record ClosureRow(Long snapshotId, Integer aggregateVersion, LocalDateTime completedAt) {
    }

    private record SnapshotResultRow(
            Long snapshotId,
            Integer aggregateVersion,
            LocalDateTime completedAt,
            String numeroOS) {
    }

    private record CommandReservation(Long commandId, Long resultSnapshotId, boolean created) {
    }

    private record ExistingCommand(Long id, Long ordemServicoId, String requestHash, Long resultSnapshotId) {
    }
}
