package com.neritech.saas.orcamento.service;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.orcamento.dto.OrcamentoDraftRequest;
import com.neritech.saas.orcamento.exception.OrcamentoIdempotencyConflictException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;

/**
 * Reserva chaves de criação de orçamento dentro da mesma transação do draft.
 *
 * A unicidade no PostgreSQL é o mecanismo autoritativo contra duas requisições
 * concorrentes com a mesma chave. O ator é persistido somente como hash para
 * evitar armazenar e-mail/login em uma tabela técnica.
 */
@Service
public class OrcamentoCreationIdempotencyService {

    private static final int MAX_KEY_LENGTH = 128;

    private final JdbcTemplate jdbcTemplate;
    private final HttpServletRequest httpServletRequest;

    public OrcamentoCreationIdempotencyService(JdbcTemplate jdbcTemplate, HttpServletRequest httpServletRequest) {
        this.jdbcTemplate = jdbcTemplate;
        this.httpServletRequest = httpServletRequest;
    }

    public Reservation reserve(Long tenantId, OrcamentoDraftRequest request) {
        Objects.requireNonNull(tenantId, "tenantId");
        Objects.requireNonNull(request, "request");

        String idempotencyKey = normalizeKey(httpServletRequest.getHeader("Idempotency-Key"));
        String actorHash = sha256(requireAuthenticatedActor());
        String requestHash = sha256(canonicalRequest(request));

        int inserted = jdbcTemplate.update("""
                INSERT INTO orcamento_creation_idempotency
                    (empresa_id, actor_hash, idempotency_key, request_hash)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (empresa_id, actor_hash, idempotency_key) DO NOTHING
                """, tenantId, actorHash, idempotencyKey, requestHash);

        if (inserted == 1) {
            return new Reservation(idempotencyKey, actorHash, requestHash, null, true);
        }

        List<ExistingReservation> existing = jdbcTemplate.query("""
                SELECT request_hash, ordem_servico_id
                  FROM orcamento_creation_idempotency
                 WHERE empresa_id = ?
                   AND actor_hash = ?
                   AND idempotency_key = ?
                """,
                (rs, rowNum) -> new ExistingReservation(
                        rs.getString("request_hash"),
                        rs.getObject("ordem_servico_id", Long.class)),
                tenantId,
                actorHash,
                idempotencyKey);

        if (existing.size() != 1) {
            throw new IllegalStateException("Reserva idempotente não pôde ser recuperada.");
        }

        ExistingReservation found = existing.get(0);
        if (!requestHash.equals(found.requestHash())) {
            throw new OrcamentoIdempotencyConflictException(
                    "A Idempotency-Key já foi usada para outro conteúdo de orçamento.");
        }

        if (found.ordemServicoId() == null) {
            throw new IllegalStateException(
                    "A criação anterior com esta Idempotency-Key ainda não possui resultado persistido.");
        }

        return new Reservation(idempotencyKey, actorHash, requestHash, found.ordemServicoId(), false);
    }

    public void complete(Long tenantId, Reservation reservation, Long ordemServicoId) {
        if (!reservation.created()) {
            return;
        }

        int updated = jdbcTemplate.update("""
                UPDATE orcamento_creation_idempotency
                   SET ordem_servico_id = ?,
                       completed_at = CURRENT_TIMESTAMP
                 WHERE empresa_id = ?
                   AND actor_hash = ?
                   AND idempotency_key = ?
                   AND request_hash = ?
                   AND ordem_servico_id IS NULL
                """,
                ordemServicoId,
                tenantId,
                reservation.actorHash(),
                reservation.idempotencyKey(),
                reservation.requestHash());

        if (updated != 1) {
            throw new IllegalStateException("Não foi possível concluir a reserva idempotente do orçamento.");
        }
    }

    private String normalizeKey(String rawKey) {
        if (rawKey == null || rawKey.isBlank()) {
            throw new BusinessException("Idempotency-Key é obrigatória para criar orçamento.");
        }

        String normalized = rawKey.trim();
        if (normalized.length() > MAX_KEY_LENGTH) {
            throw new BusinessException("Idempotency-Key deve possuir no máximo 128 caracteres.");
        }
        return normalized;
    }

    private String requireAuthenticatedActor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken
                || authentication.getName() == null
                || authentication.getName().isBlank()) {
            throw new IllegalStateException("Usuário autenticado não disponível para idempotência do orçamento.");
        }
        return authentication.getName();
    }

    private String canonicalRequest(OrcamentoDraftRequest request) {
        return String.join("|",
                field(request.clienteId()),
                field(request.veiculoId()),
                field(request.quilometragemEntrada()),
                field(request.responsavelId()),
                field(request.relatoCliente()),
                field(request.observacoesInternas()),
                field(request.observacoesCliente()));
    }

    private String field(Object value) {
        if (value == null) {
            return "-1:";
        }
        String text = String.valueOf(value);
        return text.length() + ":" + text;
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 indisponível no runtime.", exception);
        }
    }

    public record Reservation(
            String idempotencyKey,
            String actorHash,
            String requestHash,
            Long ordemServicoId,
            boolean created) {
    }

    private record ExistingReservation(String requestHash, Long ordemServicoId) {
    }
}
