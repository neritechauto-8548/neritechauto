package com.neritech.saas.agendamento.dto;

import com.neritech.saas.agendamento.domain.*;
import com.neritech.saas.agendamento.domain.enums.CanalAgendamento;
import com.neritech.saas.agendamento.domain.enums.MetodoConfirmacao;
import com.neritech.saas.agendamento.domain.enums.StatusAgendamento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO de resposta para Agendamento
 */
public record AgendamentoResponse(
        Long id,
        Long empresaId,
        String numeroAgendamento,
        Long clienteId,
        Long veiculoId,
        Long tipoAgendamentoId,
        String tipoAgendamentoNome,
        LocalDate dataAgendamento,
        LocalTime horaInicio,
        LocalTime horaFim,
        Integer duracaoEstimadaMinutos,
        String servicosSolicitados,
        String problemaRelatado,
        String observacoesCliente,
        String observacoesInternas,
        Long mecanicoPreferidoId,
        Long mecanicoAlocadoId,
        String recursosNecessarios,
        StatusAgendamento status,
        Boolean confirmadoCliente,
        LocalDateTime dataConfirmacao,
        MetodoConfirmacao metodoConfirmacao,
        Boolean lembreteEnviado,
        LocalDateTime dataLembrete,
        LocalDateTime chegadaCliente,
        LocalDateTime inicioAtendimento,
        LocalDateTime fimAtendimento,
        Integer avaliacaoAtendimento,
        String comentarioAvaliacao,
        Long ordemServicoGeradaId,
        BigDecimal valorEstimado,
        Long formaPagamentoPreferidaId,
        CanalAgendamento canalAgendamento,
        LocalDateTime dataCadastro) {
}
