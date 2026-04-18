package com.neritech.saas.agendamento.dto;

import com.neritech.saas.agendamento.domain.enums.StatusListaEspera;
import com.neritech.saas.agendamento.domain.enums.PeriodoPreferido;
import com.neritech.saas.agendamento.domain.enums.UrgenciaListaEspera;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ListaEsperaRequest(
        @NotNull Long empresaId,
        @NotNull Long clienteId,
        @NotNull Long veiculoId,
        Long tipoAgendamentoId,
        LocalDate dataPreferida,
        PeriodoPreferido periodoPreferido,
        String servicosDesejados,
        Long mecanicoPreferidoId,
        @NotNull UrgenciaListaEspera urgencia,
        String observacoes,
        Boolean notificarDisponibilidade,
        @NotBlank @Size(max = 20) String telefoneContato,
        @Email String emailContato,
        @Size(max = 20) String whatsappContato,
        Integer raioDisponibilidadeKm,
        Integer flexibilidadeDataDias,
        @NotNull StatusListaEspera status) {
}
