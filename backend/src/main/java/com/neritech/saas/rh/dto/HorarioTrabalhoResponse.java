package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.enums.TipoHorario;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record HorarioTrabalhoResponse(
        Long id,
        Long empresaId,
        String nome,
        String descricao,
        TipoHorario tipoHorario,
        LocalTime horaEntradaSeg,
        LocalTime horaSaidaSeg,
        LocalTime horaEntradaTer,
        LocalTime horaSaidaTer,
        LocalTime horaEntradaQua,
        LocalTime horaSaidaQua,
        LocalTime horaEntradaQui,
        LocalTime horaSaidaQui,
        LocalTime horaEntradaSex,
        LocalTime horaSaidaSex,
        LocalTime horaEntradaSab,
        LocalTime horaSaidaSab,
        LocalTime horaEntradaDom,
        LocalTime horaSaidaDom,
        Integer intervaloAlmocoMinutos,
        Integer cargaHorariaSemanal,
        Integer toleranciaAtrasoMinutos,
        Boolean ativo,
        Long criadoPor,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
