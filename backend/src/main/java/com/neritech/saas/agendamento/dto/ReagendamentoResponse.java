package com.neritech.saas.agendamento.dto;

import com.neritech.saas.agendamento.domain.enums.MotivoReagendamento;
import com.neritech.saas.agendamento.domain.enums.SolicitadoPor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public record ReagendamentoResponse(
        Long id,
        Long agendamentoOriginalId,
        String numeroAgendamentoOriginal,
        Long agendamentoNovoId,
        String numeroAgendamentoNovo,
        LocalDate dataOriginal,
        LocalTime horaOriginal,
        LocalDate dataNovo,
        LocalTime horaNovo,
        MotivoReagendamento motivoReagendamento,
        String descricaoMotivo,
        SolicitadoPor solicitadoPor,
        BigDecimal taxaReagendamento,
        Boolean aprovadoCliente,
        LocalDateTime dataAprovacao,
        String observacoes,
        Long usuarioResponsavel,
        LocalDateTime dataReagendamento) {
}
