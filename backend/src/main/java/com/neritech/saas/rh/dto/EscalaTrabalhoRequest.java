package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.enums.TipoEscala;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record EscalaTrabalhoRequest(
        @NotNull Long empresaId,
        @NotNull Long funcionarioId,
        Long horarioTrabalhoId,
        TipoEscala tipoEscala,
        @NotNull LocalDate dataInicio,
        LocalDate dataFim,
        Integer diasTrabalho,
        Integer diasFolga,
        String funcionariosIncluidos,
        String observacoes,
        Boolean ativo) {
}
