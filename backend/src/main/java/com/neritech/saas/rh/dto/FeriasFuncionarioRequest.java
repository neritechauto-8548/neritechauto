package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.enums.StatusFerias;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record FeriasFuncionarioRequest(
        @NotNull Long funcionarioId,
        @NotNull LocalDate periodoAquisitivoInicio,
        @NotNull LocalDate periodoAquisitivoFim,
        @NotNull Integer diasDireito,
        Integer diasGozados,
        Integer diasRestantes,
        LocalDate dataInicioFerias,
        LocalDate dataFimFerias,
        LocalDate dataRetornoPrevisto,
        Boolean abonoPecuniario,
        Integer diasAbono,
        Boolean fracionada,
        Integer numeroFracao,
        @NotNull StatusFerias status,
        String observacoes) {
}
