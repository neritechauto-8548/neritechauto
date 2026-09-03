package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.exception.ApiException;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.WorkSession;
import com.neritech.saas.ordemservico.domain.enums.StatusExecucao;
import com.neritech.saas.ordemservico.domain.enums.WorkSessionStatus;
import com.neritech.saas.ordemservico.dto.OrdemServicoExecutionResponse;
import com.neritech.saas.ordemservico.dto.WorkSessionPauseRequest;
import com.neritech.saas.ordemservico.dto.WorkSessionResponse;
import com.neritech.saas.ordemservico.repository.ItemOSServicoRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.ordemservico.repository.WorkSessionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
public class WorkSessionService {

    private static final Set<WorkSessionStatus> OPEN_SESSION_STATUSES =
            EnumSet.of(WorkSessionStatus.EM_EXECUCAO, WorkSessionStatus.PAUSADA);

    private static final Set<String> PAUSE_REASONS = Set.of(
            "ALMOCO_INTERVALO",
            "AGUARDANDO_PECA",
            "AGUARDANDO_CLIENTE",
            "FALHA_TECNICA",
            "SEGURANCA",
            "TROCA_PRIORIDADE",
            "AUSENCIA",
            "SISTEMA",
            "OUTRO");

    private final WorkSessionRepository workSessionRepository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final ItemOSServicoRepository itemOSServicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final WorkSessionIdempotencyService idempotencyService;

    public WorkSessionService(
            WorkSessionRepository workSessionRepository,
            OrdemServicoRepository ordemServicoRepository,
            ItemOSServicoRepository itemOSServicoRepository,
            UsuarioRepository usuarioRepository,
            WorkSessionIdempotencyService idempotencyService) {
        this.workSessionRepository = workSessionRepository;
        this.ordemServicoRepository = ordemServicoRepository;
        this.itemOSServicoRepository = itemOSServicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.idempotencyService = idempotencyService;
    }

    @Transactional(readOnly = true)
    public OrdemServicoExecutionResponse findExecution(Long ordemServicoId) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        OrdemServico ordemServico = requireOwnedOrder(ordemServicoId, tenantId);
        Usuario actor = requireAuthenticatedUser(tenantId);
        LocalDateTime now = LocalDateTime.now();

        List<ItemOSServico> items = itemOSServicoRepository.findByOrdemServicoId(ordemServicoId);
        List<WorkSession> sessions = workSessionRepository
                .findByOrdemServicoIdAndEmpresaIdOrderByStartedAtDesc(ordemServicoId, tenantId);
        WorkSession activeForActor = workSessionRepository
                .findFirstByEmpresaIdAndTechnicianUserIdAndStatusIn(tenantId, actor.getId(), OPEN_SESSION_STATUSES)
                .orElse(null);

        List<OrdemServicoExecutionResponse.ServiceExecution> services = items.stream()
                .map(item -> toServiceExecution(item, activeForActor, sessions, now))
                .toList();

        long elapsedSeconds = sessions.stream()
                .mapToLong(session -> elapsedAt(session, now))
                .sum();
        int estimatedMinutes = items.stream()
                .map(ItemOSServico::getTempoExecucaoPrevisto)
                .filter(value -> value != null && value > 0)
                .mapToInt(Integer::intValue)
                .sum();
        int inProgress = (int) services.stream()
                .filter(service -> "EM_EXECUCAO".equals(service.status()) || "PAUSADO".equals(service.status()))
                .count();
        int blockers = (int) services.stream()
                .filter(service -> !service.blockers().isEmpty())
                .count();

        WorkSessionResponse activeSession = activeForActor != null
                && ordemServicoId.equals(activeForActor.getOrdemServicoId())
                ? toResponse(activeForActor, now)
                : null;

        return new OrdemServicoExecutionResponse(
                ordemServicoId,
                ordemServico.getNumeroOS(),
                now,
                new OrdemServicoExecutionResponse.Summary(
                        items.size(),
                        inProgress,
                        elapsedSeconds,
                        estimatedMinutes,
                        null,
                        blockers),
                activeSession,
                services);
    }

    @Transactional
    public WorkSessionResponse start(Long ordemServicoId, Long serviceId, String idempotencyKey) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        Usuario actor = requireAuthenticatedUser(tenantId);

        WorkSessionIdempotencyService.Reservation reservation = idempotencyService.reserve(
                tenantId,
                actor.getId(),
                "START",
                idempotencyKey,
                ordemServicoId + "|" + serviceId);

        if (!reservation.created()) {
            return findIdempotentResult(reservation.workSessionId(), tenantId);
        }

        OrdemServico ordemServico = requireOwnedOrder(ordemServicoId, tenantId);
        ItemOSServico item = requireService(ordemServicoId, serviceId);
        validateExecutable(item);

        workSessionRepository
                .findFirstByEmpresaIdAndTechnicianUserIdAndStatusIn(tenantId, actor.getId(), OPEN_SESSION_STATUSES)
                .ifPresent(existing -> {
                    throw new ApiException(
                            "O técnico já possui uma sessão de execução aberta.",
                            HttpStatus.CONFLICT,
                            "OS_TECHNICIAN_SESSION_ACTIVE");
                });

        LocalDateTime now = LocalDateTime.now();
        WorkSession session = new WorkSession();
        session.setEmpresaId(tenantId);
        session.setOrdemServicoId(ordemServicoId);
        session.setItemServicoId(serviceId);
        session.setTechnicianUserId(actor.getId());
        session.setSource("WEB");
        session.setStatus(WorkSessionStatus.EM_EXECUCAO);
        session.setStartedAt(now);
        session.setActiveSegmentStartedAt(now);
        session.setElapsedSeconds(0L);

        try {
            session = workSessionRepository.saveAndFlush(session);
        } catch (DataIntegrityViolationException exception) {
            throw new ApiException(
                    "O técnico já possui uma sessão de execução aberta.",
                    HttpStatus.CONFLICT,
                    "OS_TECHNICIAN_SESSION_ACTIVE",
                    exception);
        }

        if (item.getMecanicoExecutorId() == null) {
            item.setMecanicoExecutorId(actor.getId());
        }
        if (item.getDataInicioExecucao() == null) {
            item.setDataInicioExecucao(now);
        }
        item.setStatusExecucao(StatusExecucao.EM_EXECUCAO);
        itemOSServicoRepository.save(item);

        if (ordemServico.getDataInicioExecucao() == null) {
            ordemServico.setDataInicioExecucao(now);
            ordemServicoRepository.save(ordemServico);
        }

        idempotencyService.complete(tenantId, actor.getId(), reservation, session.getId());
        return toResponse(session, now);
    }

    @Transactional
    public WorkSessionResponse pause(
            Long sessionId,
            WorkSessionPauseRequest request,
            String ifMatch,
            String idempotencyKey) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        Usuario actor = requireAuthenticatedUser(tenantId);
        WorkSession session = requireOwnedSession(sessionId, tenantId);
        requireSessionActor(session, actor);
        int expectedVersion = requireExpectedVersion(ifMatch);
        String reason = normalizePauseReason(request.reason(), request.note());

        WorkSessionIdempotencyService.Reservation reservation = idempotencyService.reserve(
                tenantId,
                actor.getId(),
                "PAUSE",
                idempotencyKey,
                sessionId + "|" + expectedVersion + "|" + reason + "|" + safe(request.note()));
        if (!reservation.created()) {
            return findIdempotentResult(reservation.workSessionId(), tenantId);
        }

        requireVersion(session, expectedVersion);
        if (session.getStatus() != WorkSessionStatus.EM_EXECUCAO) {
            throw new ApiException(
                    "A sessão não está em execução e não pode ser pausada.",
                    HttpStatus.CONFLICT,
                    "OS_SESSION_NOT_ACTIVE");
        }

        LocalDateTime now = LocalDateTime.now();
        session.setElapsedSeconds(elapsedAt(session, now));
        session.setActiveSegmentStartedAt(null);
        session.setPausedAt(now);
        session.setPauseReason(reason);
        session.setPauseNote(normalizeNote(request.note()));
        session.setStatus(WorkSessionStatus.PAUSADA);
        session = workSessionRepository.saveAndFlush(session);

        ItemOSServico item = requireService(session.getOrdemServicoId(), session.getItemServicoId());
        item.setStatusExecucao(StatusExecucao.PAUSADO);
        itemOSServicoRepository.save(item);

        idempotencyService.complete(tenantId, actor.getId(), reservation, session.getId());
        return toResponse(session, now);
    }

    @Transactional
    public WorkSessionResponse resume(
            Long sessionId,
            String ifMatch,
            String idempotencyKey) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        Usuario actor = requireAuthenticatedUser(tenantId);
        WorkSession session = requireOwnedSession(sessionId, tenantId);
        requireSessionActor(session, actor);
        int expectedVersion = requireExpectedVersion(ifMatch);

        WorkSessionIdempotencyService.Reservation reservation = idempotencyService.reserve(
                tenantId,
                actor.getId(),
                "RESUME",
                idempotencyKey,
                sessionId + "|" + expectedVersion);
        if (!reservation.created()) {
            return findIdempotentResult(reservation.workSessionId(), tenantId);
        }

        requireVersion(session, expectedVersion);
        if (session.getStatus() != WorkSessionStatus.PAUSADA) {
            throw new ApiException(
                    "A sessão não está pausada e não pode ser retomada.",
                    HttpStatus.CONFLICT,
                    "OS_SESSION_NOT_ACTIVE");
        }

        ItemOSServico item = requireService(session.getOrdemServicoId(), session.getItemServicoId());
        validateExecutable(item);

        LocalDateTime now = LocalDateTime.now();
        session.setStatus(WorkSessionStatus.EM_EXECUCAO);
        session.setPausedAt(null);
        session.setActiveSegmentStartedAt(now);
        session = workSessionRepository.saveAndFlush(session);

        item.setStatusExecucao(StatusExecucao.EM_EXECUCAO);
        itemOSServicoRepository.save(item);

        idempotencyService.complete(tenantId, actor.getId(), reservation, session.getId());
        return toResponse(session, now);
    }

    @Transactional
    public WorkSessionResponse finish(
            Long sessionId,
            String ifMatch,
            String idempotencyKey) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        Usuario actor = requireAuthenticatedUser(tenantId);
        WorkSession session = requireOwnedSession(sessionId, tenantId);
        requireSessionActor(session, actor);
        int expectedVersion = requireExpectedVersion(ifMatch);

        WorkSessionIdempotencyService.Reservation reservation = idempotencyService.reserve(
                tenantId,
                actor.getId(),
                "FINISH",
                idempotencyKey,
                sessionId + "|" + expectedVersion);
        if (!reservation.created()) {
            return findIdempotentResult(reservation.workSessionId(), tenantId);
        }

        requireVersion(session, expectedVersion);
        if (session.getStatus() == WorkSessionStatus.PAUSADA) {
            throw new ApiException(
                    "Retome a sessão antes de finalizá-la.",
                    HttpStatus.CONFLICT,
                    "OS_SESSION_NOT_ACTIVE");
        }
        if (session.getStatus() != WorkSessionStatus.EM_EXECUCAO) {
            throw new ApiException(
                    "A sessão não está ativa.",
                    HttpStatus.CONFLICT,
                    "OS_SESSION_NOT_ACTIVE");
        }

        LocalDateTime now = LocalDateTime.now();
        session.setElapsedSeconds(elapsedAt(session, now));
        session.setActiveSegmentStartedAt(null);
        session.setEndedAt(now);
        session.setStatus(WorkSessionStatus.FINALIZADA);
        session = workSessionRepository.saveAndFlush(session);

        ItemOSServico item = requireService(session.getOrdemServicoId(), session.getItemServicoId());
        long totalServiceSeconds = workSessionRepository
                .findByItemServicoIdAndEmpresaIdOrderByStartedAtDesc(item.getId(), tenantId)
                .stream()
                .mapToLong(workSession -> elapsedAt(workSession, now))
                .sum();
        item.setTempoExecucaoReal((int) Math.ceil(totalServiceSeconds / 60.0d));
        // Encerrar o relógio não conclui o serviço. A conclusão é outro comando de domínio.
        item.setStatusExecucao(StatusExecucao.EM_EXECUCAO);
        itemOSServicoRepository.save(item);

        idempotencyService.complete(tenantId, actor.getId(), reservation, session.getId());
        return toResponse(session, now);
    }

    private OrdemServicoExecutionResponse.ServiceExecution toServiceExecution(
            ItemOSServico item,
            WorkSession activeForActor,
            List<WorkSession> orderSessions,
            LocalDateTime now) {
        boolean authorized = Boolean.TRUE.equals(item.getAprovadoCliente());
        List<String> blockers = new ArrayList<>();

        StatusExecucao status = item.getStatusExecucao();
        if (!authorized) {
            blockers.add("OS_SERVICE_NOT_AUTHORIZED");
        }
        if (status == StatusExecucao.CANCELADO) {
            blockers.add("OS_SERVICE_CANCELLED");
        }
        if (activeForActor != null && !item.getId().equals(activeForActor.getItemServicoId())) {
            blockers.add("OS_TECHNICIAN_SESSION_ACTIVE");
        }

        List<String> actions = new ArrayList<>();
        if (hasAuthority("OS_EDITAR") && status != StatusExecucao.CONCLUIDO) {
            if (activeForActor == null && blockers.isEmpty()) {
                actions.add("START");
            } else if (activeForActor != null && item.getId().equals(activeForActor.getItemServicoId())) {
                if (activeForActor.getStatus() == WorkSessionStatus.EM_EXECUCAO) {
                    // Bloqueios surgidos durante uma sessão não podem prender o relógio.
                    actions.add("PAUSE");
                    actions.add("FINISH");
                } else if (activeForActor.getStatus() == WorkSessionStatus.PAUSADA && blockers.isEmpty()) {
                    // Retomar volta a executar trabalho e, portanto, revalida os guardas.
                    actions.add("RESUME");
                }
            }
        }

        long serviceElapsedSeconds = orderSessions.stream()
                .filter(session -> item.getId().equals(session.getItemServicoId()))
                .mapToLong(session -> elapsedAt(session, now))
                .sum();
        Integer realMinutes = serviceElapsedSeconds > 0
                ? (int) Math.ceil(serviceElapsedSeconds / 60.0d)
                : item.getTempoExecucaoReal();

        String renderedStatus = status != null
                ? status.name()
                : authorized ? StatusExecucao.PRONTO.name() : StatusExecucao.NAO_INICIADO.name();

        return new OrdemServicoExecutionResponse.ServiceExecution(
                item.getId(),
                item.getServicoId(),
                item.getDescricao(),
                item.getMecanicoExecutorId(),
                renderedStatus,
                authorized,
                item.getTempoExecucaoPrevisto(),
                realMinutes,
                null,
                item.getDataInicioExecucao(),
                item.getDataFimExecucao(),
                List.copyOf(blockers),
                List.copyOf(actions));
    }

    private WorkSessionResponse toResponse(WorkSession session, LocalDateTime now) {
        ItemOSServico item = requireService(session.getOrdemServicoId(), session.getItemServicoId());
        String technicianName = usuarioRepository
                .findByIdAndEmpresaId(session.getTechnicianUserId(), session.getEmpresaId())
                .map(Usuario::getNomeCompleto)
                .orElse("Usuário indisponível");

        List<String> blockers = new ArrayList<>();
        if (!Boolean.TRUE.equals(item.getAprovadoCliente())) {
            blockers.add("OS_SERVICE_NOT_AUTHORIZED");
        }
        if (item.getStatusExecucao() == StatusExecucao.CANCELADO) {
            blockers.add("OS_SERVICE_CANCELLED");
        }

        List<String> actions = new ArrayList<>();
        if (hasAuthority("OS_EDITAR")) {
            if (session.getStatus() == WorkSessionStatus.EM_EXECUCAO) {
                actions.add("PAUSE");
                actions.add("FINISH");
            } else if (session.getStatus() == WorkSessionStatus.PAUSADA && blockers.isEmpty()) {
                actions.add("RESUME");
            }
        }

        return new WorkSessionResponse(
                session.getId(),
                session.getOrdemServicoId(),
                session.getItemServicoId(),
                session.getTechnicianUserId(),
                technicianName,
                session.getStatus().name(),
                session.getSource(),
                session.getStartedAt(),
                session.getPausedAt(),
                session.getEndedAt(),
                session.getPauseReason(),
                session.getPauseNote(),
                elapsedAt(session, now),
                session.getVersao(),
                now,
                item.getStatusExecucao() != null ? item.getStatusExecucao().name() : null,
                List.copyOf(blockers),
                List.copyOf(actions));
    }

    private WorkSessionResponse findIdempotentResult(Long sessionId, Long tenantId) {
        WorkSession session = requireOwnedSession(sessionId, tenantId);
        return toResponse(session, LocalDateTime.now());
    }

    private OrdemServico requireOwnedOrder(Long ordemServicoId, Long tenantId) {
        return ordemServicoRepository.findByIdAndEmpresaId(ordemServicoId, tenantId)
                .orElseThrow(() -> new ApiException(
                        "Ordem de serviço não encontrada para a empresa autenticada.",
                        HttpStatus.NOT_FOUND,
                        "OS_NOT_FOUND"));
    }

    private ItemOSServico requireService(Long ordemServicoId, Long serviceId) {
        return itemOSServicoRepository.findByIdAndOrdemServicoId(serviceId, ordemServicoId)
                .orElseThrow(() -> new ApiException(
                        "Serviço não encontrado dentro desta Ordem de Serviço.",
                        HttpStatus.NOT_FOUND,
                        "OS_SERVICE_NOT_FOUND"));
    }

    private WorkSession requireOwnedSession(Long sessionId, Long tenantId) {
        return workSessionRepository.findByIdAndEmpresaId(sessionId, tenantId)
                .orElseThrow(() -> new ApiException(
                        "Sessão de execução não encontrada para a empresa autenticada.",
                        HttpStatus.NOT_FOUND,
                        "OS_SESSION_NOT_FOUND"));
    }

    private Usuario requireAuthenticatedUser(Long tenantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken
                || authentication.getName() == null
                || authentication.getName().isBlank()) {
            throw new AccessDeniedException("Usuário autenticado não disponível para execução da OS.");
        }

        return usuarioRepository.findByEmailIgnoreCaseAndEmpresaId(authentication.getName(), tenantId)
                .orElseThrow(() -> new AccessDeniedException(
                        "Usuário autenticado não pertence à empresa da sessão."));
    }

    private void requireSessionActor(WorkSession session, Usuario actor) {
        if (!actor.getId().equals(session.getTechnicianUserId()) && !hasRoleAdmin()) {
            throw new AccessDeniedException("A sessão de execução pertence a outro técnico.");
        }
    }

    private void validateExecutable(ItemOSServico item) {
        if (!Boolean.TRUE.equals(item.getAprovadoCliente())) {
            throw new ApiException(
                    "O serviço precisa estar autorizado antes de iniciar a execução.",
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "OS_SERVICE_NOT_AUTHORIZED");
        }
        if (item.getStatusExecucao() == StatusExecucao.CANCELADO) {
            throw new ApiException(
                    "Serviço cancelado não pode iniciar execução.",
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "OS_SERVICE_BLOCKED");
        }
        if (item.getStatusExecucao() == StatusExecucao.CONCLUIDO) {
            throw new ApiException(
                    "O serviço já foi concluído.",
                    HttpStatus.CONFLICT,
                    "OS_SERVICE_ALREADY_COMPLETED");
        }
    }

    private int requireExpectedVersion(String rawIfMatch) {
        if (rawIfMatch == null || rawIfMatch.isBlank()) {
            throw new ApiException(
                    "If-Match é obrigatório para alterar uma sessão de execução.",
                    HttpStatus.PRECONDITION_REQUIRED,
                    "OS_SESSION_VERSION_REQUIRED");
        }

        String normalized = rawIfMatch.trim();
        if (normalized.startsWith("W/")) {
            normalized = normalized.substring(2).trim();
        }
        normalized = normalized.replace("\"", "");

        try {
            int version = Integer.parseInt(normalized);
            if (version < 0) throw new NumberFormatException();
            return version;
        } catch (NumberFormatException exception) {
            throw new ApiException(
                    "If-Match deve conter a versão numérica da sessão.",
                    HttpStatus.PRECONDITION_FAILED,
                    "OS_SESSION_VERSION_INVALID");
        }
    }

    private void requireVersion(WorkSession session, int expectedVersion) {
        if (session.getVersao() == null || session.getVersao() != expectedVersion) {
            throw new ApiException(
                    "A sessão foi alterada por outro processo. Atualize a execução antes de continuar.",
                    HttpStatus.CONFLICT,
                    "OS_SESSION_VERSION_CONFLICT");
        }
    }

    private String normalizePauseReason(String rawReason, String note) {
        String normalized = rawReason == null ? "" : rawReason.trim().toUpperCase();
        if (!PAUSE_REASONS.contains(normalized)) {
            throw new ApiException(
                    "Motivo de pausa inválido.",
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "OS_PAUSE_REASON_INVALID");
        }
        if ("OUTRO".equals(normalized) && (note == null || note.isBlank())) {
            throw new ApiException(
                    "Informe uma observação quando o motivo da pausa for OUTRO.",
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "OS_PAUSE_NOTE_REQUIRED");
        }
        return normalized;
    }

    private String normalizeNote(String value) {
        if (value == null) return null;
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private long elapsedAt(WorkSession session, LocalDateTime now) {
        long stored = session.getElapsedSeconds() != null ? session.getElapsedSeconds() : 0L;
        if (session.getStatus() == WorkSessionStatus.EM_EXECUCAO
                && session.getActiveSegmentStartedAt() != null) {
            long live = Math.max(0L, Duration.between(session.getActiveSegmentStartedAt(), now).getSeconds());
            return stored + live;
        }
        return stored;
    }

    private boolean hasAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream().anyMatch(granted ->
                authority.equals(granted.getAuthority()) || "ROLE_ADMIN".equals(granted.getAuthority()));
    }

    private boolean hasRoleAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream().anyMatch(granted ->
                "ROLE_ADMIN".equals(granted.getAuthority()));
    }
}
