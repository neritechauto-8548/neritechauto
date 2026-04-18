package com.neritech.saas.agendamento.dto;

import com.neritech.saas.agendamento.domain.enums.TipoBloqueio;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public record BloqueioAgendaResponse(
        Long id,
        Long empresaId,
        Long funcionarioId,
        TipoBloqueio tipoBloqueio,
        LocalDate dataInicio,
        LocalDate dataFim,
        LocalTime horaInicio,
        LocalTime horaFim,
        Boolean recorrente,
        String diasSemanaRecorrencia,
        String motivo,
        Boolean afetaTodosFuncionarios,
        String funcionariosAfetados,
        String observacoes,
        Boolean ativo,
        LocalDateTime dataCadastro) {
}
