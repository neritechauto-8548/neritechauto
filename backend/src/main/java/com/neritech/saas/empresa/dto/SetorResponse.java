package com.neritech.saas.empresa.dto;

import java.time.LocalDateTime;

public record SetorResponse(
        Long id,
        Long empresaId,
        String empresaNome,
        String nome,
        Boolean ativo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}

