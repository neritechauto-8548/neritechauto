package com.neritech.saas.trial.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TrialRequestThrottle {
    private final JdbcTemplate jdbc;

    public TrialRequestThrottle(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void checkAndRecord(String email, String remoteAddress) {
        String emailHash = hash(email.trim().toLowerCase(Locale.ROOT));
        String ipHash = hash(remoteAddress == null ? "unknown" : remoteAddress);
        Long ipCount = jdbc.queryForObject("SELECT COUNT(*) FROM trial_cadastro_tentativas WHERE ip_hash = ? AND criado_em > CURRENT_TIMESTAMP - INTERVAL '1 hour'", Long.class, ipHash);
        Long emailCount = jdbc.queryForObject("SELECT COUNT(*) FROM trial_cadastro_tentativas WHERE email_hash = ? AND criado_em > CURRENT_TIMESTAMP - INTERVAL '1 hour'", Long.class, emailHash);
        if (ipCount != null && ipCount >= 20 || emailCount != null && emailCount >= 3) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Muitas tentativas. Aguarde antes de tentar novamente.");
        }
        jdbc.update("INSERT INTO trial_cadastro_tentativas (ip_hash, email_hash, criado_em) VALUES (?, ?, CURRENT_TIMESTAMP)", ipHash, emailHash);
    }

    private static String hash(String value) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); }
        catch (NoSuchAlgorithmException e) { throw new IllegalStateException("SHA-256 indisponível", e); }
    }
}
