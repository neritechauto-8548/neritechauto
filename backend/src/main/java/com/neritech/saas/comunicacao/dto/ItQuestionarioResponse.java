package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.TipoItemQuestionario;
import java.time.LocalDateTime;

public record ItQuestionarioResponse(
        Long id,
        Long questionarioId,
        String dsItQuestionario,
        TipoItemQuestionario tpItQuestionario,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}

