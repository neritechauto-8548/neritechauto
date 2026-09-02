package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;

@Service
public class WorkSessionIdempotencyService {

    private static final int MAX_KEY_LENGTH = 128;

    private final JdbcTemplate jdbcTemplate;

    public WorkSessionIdempotencyService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Reservation reserve(
            Long tenantId,
            Long actorUserId,
            String command,
            String rawKey,
            String canonicalRequest) {
        Objects.requireNonNull(tenantId, "tenantId");
        Objects.requireNonNull(actorUserId, "actorUserId");

        String key = normalizeKey(rawKey);
        String requestHash = sha256(canonicalRequest);

        int inserted = jdbcTemplate.update("""
                INSERT INTO os_work_session_commands
                    (empresa_id, actor_user_id, command, idempotency_key, request_hash)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT (empresa_id, actor_user_id, command, idempotency_key) DO NOTHING
                """, tenantId, actorUserId, command, key, requestHash);

        if (inserted == 1) {
            return new Reservation(command, key, requestHash, null, true);
        }

        List<ExistingReservation> rows = jdbcTemplate.query("""
                SELECT request_hash, work_session_id
                  FROM os_work_session_commands
                 WHERE empresa_id = ?
                   AND actor_user_id = ?
                   AND command = ?
                   AND idempotency_key = ?
                """,
                (rs, rowNum) -> new ExistingReservation(
                        rs.getString("request_hash"),
                        rs.getObject("work_session_id", Long.class)),
                tenantId,
                actorUserId,
                command,
                key);

        if (rows.size() != 1) {
            throw new IllegalStateException("Reserva idempotente da execução não pôde ser recuperada.");
        }

        ExistingReservation existing = rows.get(0);
        if (!requestHash.equals(existing.requestHash())) {
            throw new ApiException(
                    "A Idempotency-Key já foi usada para outro comando de execução.",
                    HttpStatus.CONFLICT,
                    "OS_IDEMPOTENCY_CONFLICT");
        }

        if (existing.workSessionId() == null) {
            throw new ApiException(
                    "O comando anterior com esta Idempotency-Key ainda está sendo processado.",
                    HttpStatus.CONFLICT,
                    "OS_IDEMPOTENCY_IN_PROGRESS");
        }

        return new Reservation(command, key, requestHash, existing.workSessionId(), false);
    }

    public void complete(
            Long tenantId,
            Long actorUserId,
            Reservation reservation,
            Long workSessionId) {
        if (!reservation.created()) {
            return;
        }

        int updated = jdbcTemplate.update("""
                UPDATE os_work_session_commands
                   SET work_session_id = ?,
                       completed_at = CURRENT_TIMESTAMP
                 WHERE empresa_id = ?
                   AND actor_user_id = ?
                   AND command = ?
                   AND idempotency_key = ?
                   AND request_hash = ?
                   AND work_session_id IS NULL
                """,
                workSessionId,
                tenantId,
                actorUserId,
                reservation.command(),
                reservation.key(),
                reservation.requestHash());

        if (updated != 1) {
            throw new IllegalStateException("Não foi possível concluir a reserva idempotente da execução.");
        }
    }

    private String normalizeKey(String rawKey) {
        if (rawKey == null || rawKey.isBlank()) {
            throw new ApiException(
                    "Idempotency-Key é obrigatória para comandos de execução.",
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "OS_IDEMPOTENCY_KEY_REQUIRED");
        }

        String normalized = rawKey.trim();
        if (normalized.length() > MAX_KEY_LENGTH) {
            throw new ApiException(
                    "Idempotency-Key deve possuir no máximo 128 caracteres.",
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "OS_IDEMPOTENCY_KEY_INVALID");
        }
        return normalized;
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
            String command,
            String key,
            String requestHash,
            Long workSessionId,
            boolean created) {
    }

    private record ExistingReservation(String requestHash, Long workSessionId) {
    }
}
