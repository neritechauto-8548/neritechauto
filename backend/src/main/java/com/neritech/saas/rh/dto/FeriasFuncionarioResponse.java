package com.neritech.saas.rh.dto;

import com.neritech.saas.agendamento.domain.enums.SolicitadoPor;
import com.neritech.saas.rh.domain.enums.StatusFerias;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FeriasFuncionarioResponse(
        Long id,
        Long funcionarioId,
        LocalDate periodoAquisitivoInicio,
        LocalDate periodoAquisitivoFim,
        Integer diasDireito,
        Integer diasGozados,
        Integer diasRestantes,
        LocalDate dataInicioFerias,
        LocalDate dataFimFerias,
        LocalDate dataRetornoPrevisto,
        Boolean abonoPecuniario,
        Integer diasAbono,
        Boolean fracionada,
        Integer numeroFracao,
        StatusFerias status,
        String observacoes,
        Long solicitadoPor,
        LocalDate dataSolicitacao,
        Long aprovadoPor,
        LocalDate dataAprovacao,
        Long canceladoPor,
        LocalDate dataCancelamento,
        String motivoCancelamento,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
