package com.neritech.saas.agendamento.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record NoShowRequest(
        @NotNull Long agendamentoId,
        @NotNull Long clienteId,
        @NotNull LocalDate dataAgendamento,
        @NotNull LocalTime horaAgendamento,
        @Min(0) Integer tempoToleranciaMinutos,
        @Min(0) Integer tentativasContato,
        String meioContatoTentado,
        Boolean reagendado,
        Long novoAgendamentoId,
        @DecimalMin("0.00") BigDecimal taxaNoShow,
        Boolean taxaAplicada,
        String motivoDeclarado,
        Boolean justificativaAceita,
        String observacoes,
        Long registradoPor) {
}
