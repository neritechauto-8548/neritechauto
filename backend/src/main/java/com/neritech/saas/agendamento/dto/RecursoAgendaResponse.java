package com.neritech.saas.agendamento.dto;

import com.neritech.saas.agendamento.domain.enums.TipoRecurso;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecursoAgendaResponse(
        Long id,
        Long empresaId,
        String nomeRecurso,
        TipoRecurso tipoRecurso,
        String descricao,
        Integer capacidadeSimultanea,
        String localizacao,
        Boolean disponivel,
        Boolean requerAgendamento,
        Integer tempoSetupMinutos,
        Integer tempoCleanupMinutos,
        BigDecimal custoHora,
        String observacoes,
        Long responsavelId,
        LocalDateTime dataCadastro) {
}
