package com.neritech.saas.empresa.dto;

import java.time.LocalDateTime;

public record DepartamentoContabioResponse(
        Long id,
        Long empresaId,
        String empresaNome,
        String descricao,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}

