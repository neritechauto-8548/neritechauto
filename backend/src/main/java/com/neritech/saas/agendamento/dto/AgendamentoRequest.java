package com.neritech.saas.agendamento.dto;

import com.neritech.saas.agendamento.domain.enums.CanalAgendamento;
import com.neritech.saas.agendamento.domain.enums.MetodoConfirmacao;
import com.neritech.saas.agendamento.domain.enums.StatusAgendamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO legado de criação/edição. empresaId e numeroAgendamento permanecem apenas
 * para compatibilidade de desserialização; o backend não os trata como autoridade.
 */
public record AgendamentoRequest(
        Long empresaId,
        @Size(max = 20) String numeroAgendamento,
        @NotNull Long clienteId,
        Long veiculoId,
        Long tipoAgendamentoId,
        @NotNull LocalDate dataAgendamento,
        @NotNull LocalTime horaInicio,
        @NotNull LocalTime horaFim,
        @Positive Integer duracaoEstimadaMinutos,
        @Size(max = 4000) String servicosSolicitados,
        @Size(max = 4000) String problemaRelatado,
        @Size(max = 4000) String observacoesCliente,
        @Size(max = 4000) String observacoesInternas,
        Long mecanicoPreferidoId,
        Long mecanicoAlocadoId,
        @Size(max = 4000) String recursosNecessarios,
        @NotNull StatusAgendamento status,
        Boolean confirmadoCliente,
        MetodoConfirmacao metodoConfirmacao,
        BigDecimal valorEstimado,
        Long formaPagamentoPreferidaId,
        @NotNull CanalAgendamento canalAgendamento) {
}
