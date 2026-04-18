package com.neritech.saas.comunicacao.dto;

import java.time.LocalDateTime;

public record QuestionarioResponse(
        Long id,
        Long empresaId,
        String dsQuestionario,
        String urlImagem,
        Boolean snAtivo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}

