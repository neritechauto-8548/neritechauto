package com.neritech.saas.agendamento.dto;

import com.neritech.saas.agendamento.domain.enums.TipoBloqueio;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public record BloqueioAgendaRequest(
        @NotNull Long empresaId,
        Long funcionarioId,
        @NotNull TipoBloqueio tipoBloqueio,
        @NotNull LocalDate dataInicio,
        @NotNull LocalDate dataFim,
        LocalTime horaInicio,
        LocalTime horaFim,
        Boolean recorrente,
        @Size(max = 7) String diasSemanaRecorrencia,
        @NotBlank String motivo,
        Boolean afetaTodosFuncionarios,
        String funcionariosAfetados,
        String observacoes,
        Boolean ativo) {
}
