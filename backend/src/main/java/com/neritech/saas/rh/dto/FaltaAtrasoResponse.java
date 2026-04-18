package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.*;
import com.neritech.saas.rh.domain.enums.TipoJustificativa;
import com.neritech.saas.rh.domain.enums.TipoOcorrencia;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record FaltaAtrasoResponse(
        Long id,
        Long funcionarioId,
        TipoOcorrencia tipoOcorrencia,
        LocalDate dataOcorrencia,
        LocalTime horarioPrevisto,
        LocalTime horarioReal,
        Integer minutosAtraso,
        BigDecimal horasFalta,
        TipoJustificativa tipoJustificativa,
        String justificativa,
        Boolean justificada,
        String anexoComprovanteUrl,
        Boolean descontoAplicado,
        BigDecimal valorDesconto,
        Boolean advertenciaAplicada,
        String observacoes,
        Long registradoPor,
        Long aprovadoPor,
        LocalDate dataAprovacao,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
