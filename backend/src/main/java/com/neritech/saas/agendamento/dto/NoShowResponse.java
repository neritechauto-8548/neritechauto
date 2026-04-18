package com.neritech.saas.agendamento.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public record NoShowResponse(
        Long id,
        Long agendamentoId,
        String numeroAgendamento,
        Long clienteId,
        LocalDate dataAgendamento,
        LocalTime horaAgendamento,
        Integer tempoToleranciaMinutos,
        Integer tentativasContato,
        String meioContatoTentado,
        Boolean reagendado,
        Long novoAgendamentoId,
        String numeroNovoAgendamento,
        BigDecimal taxaNoShow,
        Boolean taxaAplicada,
        String motivoDeclarado,
        Boolean justificativaAceita,
        String observacoes,
        Long registradoPor,
        LocalDateTime dataRegistro) {
}
