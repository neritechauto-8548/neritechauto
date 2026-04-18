package com.neritech.saas.agendamento.dto;

import com.neritech.saas.agendamento.domain.enums.TipoLembrete;
import com.neritech.saas.agendamento.domain.enums.StatusEnvioLembrete;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LembreteAgendamentoResponse(
        Long id,
        Long agendamentoId,
        String numeroAgendamento,
        TipoLembrete tipoLembrete,
        String destinatario,
        String assunto,
        String mensagem,
        LocalDateTime dataAgendamentoEnvio,
        LocalDateTime dataEnvio,
        StatusEnvioLembrete statusEnvio,
        Integer tentativasEnvio,
        String erroEnvio,
        String respostaCliente,
        LocalDateTime dataResposta,
        BigDecimal custoEnvio,
        String templateUsado,
        String variaveisTemplate,
        Boolean automatico,
        LocalDateTime dataCadastro) {
}
