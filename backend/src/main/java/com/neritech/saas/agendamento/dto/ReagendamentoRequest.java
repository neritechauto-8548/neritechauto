package com.neritech.saas.agendamento.dto;

import com.neritech.saas.agendamento.domain.enums.MotivoReagendamento;
import com.neritech.saas.agendamento.domain.enums.SolicitadoPor;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReagendamentoRequest(
        @NotNull Long agendamentoOriginalId,
        Long agendamentoNovoId,
        @NotNull LocalDate dataOriginal,
        @NotNull LocalTime horaOriginal,
        @NotNull LocalDate dataNovo,
        @NotNull LocalTime horaNovo,
        @NotNull MotivoReagendamento motivoReagendamento,
        String descricaoMotivo,
        @NotNull SolicitadoPor solicitadoPor,
        @DecimalMin("0.00") BigDecimal taxaReagendamento,
        Boolean aprovadoCliente,
        String observacoes,
        Long usuarioResponsavel) {
}
