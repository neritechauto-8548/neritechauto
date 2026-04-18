package com.neritech.saas.empresa.dto;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDateTime;

public record ConfiguracaoOficinaResponse(
        Long id,
        Long empresaId,
        String empresaNome,
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
        String timezone,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
