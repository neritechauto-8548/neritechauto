package com.neritech.saas.empresa.dto;

import java.time.LocalDateTime;

public record DepartamentoAlteracaoResponse(String operacao, String campos, String valores,
        Long autor, LocalDateTime data) {}
