package com.neritech.saas.comunicacao.dto;

import java.time.LocalDateTime;
import com.neritech.saas.comunicacao.domain.enums.StatusPesquisa;

public record QuestionarioResponse(
        Long id,
        Long empresaId,
        String dsQuestionario,
        String urlImagem,
        Boolean snAtivo,
        StatusPesquisa statusPesquisa,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}

