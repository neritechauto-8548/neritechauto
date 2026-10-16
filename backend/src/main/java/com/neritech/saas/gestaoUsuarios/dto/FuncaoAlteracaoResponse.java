package com.neritech.saas.gestaoUsuarios.dto;

import java.time.LocalDateTime;

public record FuncaoAlteracaoResponse(String operacao, String campos, String valores,
        Long autor, LocalDateTime data) {}
