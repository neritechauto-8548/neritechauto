package com.neritech.saas.rh.dto;

import java.time.LocalDateTime;

public record CargoAlteracaoResponse(
        String operacao, String campos, String valores, Long autor, LocalDateTime data) {}
