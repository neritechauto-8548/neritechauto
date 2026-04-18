package com.neritech.saas.agendamento.dto;

import com.neritech.saas.agendamento.domain.*;
import com.neritech.saas.agendamento.domain.enums.PeriodoPreferido;
import com.neritech.saas.agendamento.domain.enums.StatusListaEspera;
import com.neritech.saas.agendamento.domain.enums.UrgenciaListaEspera;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ListaEsperaResponse(
        Long id,
        Long empresaId,
        Long clienteId,
        Long veiculoId,
        Long tipoAgendamentoId,
        String tipoAgendamentoNome,
        LocalDate dataPreferida,
        PeriodoPreferido periodoPreferido,
        String servicosDesejados,
        Long mecanicoPreferidoId,
        UrgenciaListaEspera urgencia,
        String observacoes,
        Boolean notificarDisponibilidade,
        String telefoneContato,
        String emailContato,
        String whatsappContato,
        Integer raioDisponibilidadeKm,
        Integer flexibilidadeDataDias,
        StatusListaEspera status,
        LocalDateTime dataNotificacao,
        LocalDateTime dataExpiracao,
        Long agendamentoGeradoId,
        Integer posicaoLista,
        LocalDateTime dataCadastro) {
}
