package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.*;
import com.neritech.saas.rh.domain.enums.TipoJustificativa;
import com.neritech.saas.rh.domain.enums.TipoOcorrencia;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record FaltaAtrasoRequest(
        @NotNull Long funcionarioId,
        @NotNull TipoOcorrencia tipoOcorrencia,
        @NotNull LocalDate dataOcorrencia,
        LocalTime horarioPrevisto,
        LocalTime horarioReal,
        Integer minutosAtraso,
        BigDecimal horasFalta,
        TipoJustificativa tipoJustificativa,
        String justificativa,
        Boolean justificada,
        @Size(max = 500) String anexoComprovanteUrl,
        Boolean descontoAplicado,
        BigDecimal valorDesconto,
        Boolean advertenciaAplicada,
        String observacoes) {
}
