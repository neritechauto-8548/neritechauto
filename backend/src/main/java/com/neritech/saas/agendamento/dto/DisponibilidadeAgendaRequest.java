package com.neritech.saas.agendamento.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public record DisponibilidadeAgendaRequest(
        @NotNull Long empresaId,
        @NotNull Long funcionarioId,
        @NotNull LocalDate dataDisponibilidade,
        @NotNull @Min(0) @Max(6) Integer diaSemana,
        @NotNull LocalTime horaInicio,
        @NotNull LocalTime horaFim,
        LocalTime intervaloAlmocoInicio,
        LocalTime intervaloAlmocoFim,
        Boolean disponivel,
        @Min(1) Integer capacidadeAtendimentos,
        String especialidadesDisponiveis,
        String tiposAgendamentoAceitos,
        String observacoes) {
}
