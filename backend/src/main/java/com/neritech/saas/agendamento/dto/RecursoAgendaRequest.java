package com.neritech.saas.agendamento.dto;

import com.neritech.saas.agendamento.domain.enums.TipoRecurso;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record RecursoAgendaRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 100) String nomeRecurso,
        @NotNull TipoRecurso tipoRecurso,
        String descricao,
        @Min(1) Integer capacidadeSimultanea,
        @Size(max = 255) String localizacao,
        Boolean disponivel,
        Boolean requerAgendamento,
        @Min(0) Integer tempoSetupMinutos,
        @Min(0) Integer tempoCleanupMinutos,
        @DecimalMin("0.00") BigDecimal custoHora,
        String observacoes,
        Long responsavelId) {
}
