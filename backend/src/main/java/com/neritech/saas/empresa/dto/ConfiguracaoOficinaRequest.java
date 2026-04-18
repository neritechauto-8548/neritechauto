package com.neritech.saas.empresa.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalTime;

public record ConfiguracaoOficinaRequest(
        @NotNull Long empresaId,
        LocalTime horarioAberturaSegSex,
        LocalTime horarioFechamentoSegSex,
        LocalTime horarioAberturaSabado,
        LocalTime horarioFechamentoSabado,
        Boolean funcionaDomingo,
        LocalTime horarioAberturaDomingo,
        LocalTime horarioFechamentoDomingo,
        Integer tempoAgendamentoPadrao,
        Integer antecedenciaMinimaAgendamento,
        Boolean permiteAgendamentoOnline,
        Boolean enviaLembreteAgendamento,
        Integer tempoLembreteHoras,
        BigDecimal margemLucroPadrao,
        Integer diasGarantiaPadrao,
        String moeda,
        String formatoData,
        String formatoHora,
        String timezone) {
}
