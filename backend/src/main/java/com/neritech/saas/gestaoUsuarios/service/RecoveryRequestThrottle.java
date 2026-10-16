package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.gestaoUsuarios.domain.RecoveryRequestAttempt;
import com.neritech.saas.gestaoUsuarios.repository.RecoveryRequestAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Locale;

/**
 * Durable account-independent recovery throttle.
 * 429 only depends on the caller IP, not on whether the account exists.
 * Per-email soft throttle always responds generically.
 */
@Service
@RequiredArgsConstructor
public class RecoveryRequestThrottle {
    private final RecoveryRequestAttemptRepository attempts;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean checkAndRecord(String email, String sourceIp) {
        String emailHash = sha256(email.trim().toLowerCase(Locale.ROOT));
        String ipHash = sha256(sourceIp == null ? "unknown" : sourceIp);
        LocalDateTime since = LocalDateTime.now().minusMinutes(15);
        if (attempts.countByIpHashAndCreatedAtAfter(ipHash, since) >= 12) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Muitas solicitações. Tente novamente mais tarde.");
        }
        boolean allowed = attempts.countByEmailHashAndCreatedAtAfter(emailHash, since) < 3;
        RecoveryRequestAttempt attempt = new RecoveryRequestAttempt();
        attempt.setEmailHash(emailHash);
        attempt.setIpHash(ipHash);
        attempts.saveAndFlush(attempt);
        return allowed;
    }

    private static String sha256(String value) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 indisponível", ex);
        }
    }
}
