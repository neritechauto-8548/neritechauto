package com.neritech.saas.empresa.dto;

import java.time.LocalDateTime;

public record SituacaoResponse(
        Long id,
        Long empresaId,
        String empresaNome,
        String nmSituacao,
        String dsSituacao,
        String corSituacao,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}

