package com.neritech.saas.agendamento.dto;

import com.neritech.saas.agendamento.domain.*;
import com.neritech.saas.agendamento.domain.enums.CanalAgendamento;
import com.neritech.saas.agendamento.domain.enums.MetodoConfirmacao;
import com.neritech.saas.agendamento.domain.enums.StatusAgendamento;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO de requisiÃ§Ã£o para Agendamento
 */
public record AgendamentoRequest(
        @NotNull Long empresaId,
        @Size(max = 20) String numeroAgendamento,
        @NotNull Long clienteId,
        Long veiculoId,
        Long tipoAgendamentoId,
        @NotNull LocalDate dataAgendamento,
        @NotNull LocalTime horaInicio,
        @NotNull LocalTime horaFim,
        Integer duracaoEstimadaMinutos,
        String servicosSolicitados,
        String problemaRelatado,
        String observacoesCliente,
        String observacoesInternas,
        Long mecanicoPreferidoId,
        Long mecanicoAlocadoId,
        String recursosNecessarios,
        @NotNull StatusAgendamento status,
        Boolean confirmadoCliente,
        MetodoConfirmacao metodoConfirmacao,
        BigDecimal valorEstimado,
        Long formaPagamentoPreferidaId,
        @NotNull CanalAgendamento canalAgendamento) {
}
