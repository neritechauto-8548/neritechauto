package com.neritech.saas.empresa.dto;

import java.time.LocalDateTime;

public record LocalizacaoResponse(
        Long id,
        Long empresaId,
        String empresaNome,
        String descricao,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}

