package com.neritech.saas.trial.service;

import com.neritech.saas.empresa.domain.AssinaturaEmpresa;

import java.time.LocalDateTime;
import java.time.ZoneId;

public final class TrialPolicy {
    public static final int DURATION_DAYS = 14;
    public static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");

    private TrialPolicy() {}

    public static LocalDateTime now() {
        return LocalDateTime.now(ZONE);
    }

    public static LocalDateTime endsAt(AssinaturaEmpresa assinatura) {
        if (assinatura.getTrialEndsAt() != null) return assinatura.getTrialEndsAt();
        return assinatura.getDataFim() == null ? null : assinatura.getDataFim().atStartOfDay();
    }

    public static boolean isExpired(AssinaturaEmpresa assinatura) {
        LocalDateTime fim = endsAt(assinatura);
        return fim == null || !now().isBefore(fim);
    }
}
