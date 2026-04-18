package com.neritech.saas.agendamento.dto;

import com.neritech.saas.agendamento.domain.enums.TipoLembrete;
import com.neritech.saas.agendamento.domain.enums.StatusEnvioLembrete;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LembreteAgendamentoRequest(
        @NotNull Long agendamentoId,
        @NotNull TipoLembrete tipoLembrete,
        @NotBlank @Size(max = 255) String destinatario,
        @Size(max = 255) String assunto,
        @NotBlank String mensagem,
        @NotNull LocalDateTime dataAgendamentoEnvio,
        @NotNull StatusEnvioLembrete statusEnvio,
        Integer tentativasEnvio,
        String erroEnvio,
        String respostaCliente,
        LocalDateTime dataResposta,
        @DecimalMin("0.0000") BigDecimal custoEnvio,
        @Size(max = 100) String templateUsado,
        String variaveisTemplate,
        Boolean automatico) {
}
