package com.neritech.saas.agendamento.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public record DisponibilidadeAgendaResponse(
        Long id,
        Long empresaId,
        Long funcionarioId,
        LocalDate dataDisponibilidade,
        Integer diaSemana,
        LocalTime horaInicio,
        LocalTime horaFim,
        LocalTime intervaloAlmocoInicio,
        LocalTime intervaloAlmocoFim,
        Boolean disponivel,
        Integer capacidadeAtendimentos,
        String especialidadesDisponiveis,
        String tiposAgendamentoAceitos,
        String observacoes,
        LocalDateTime dataCadastro) {
}
