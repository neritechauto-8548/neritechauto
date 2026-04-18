package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.enums.TipoHorario;
import jakarta.validation.constraints.*;
import java.time.LocalTime;

public record HorarioTrabalhoRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 100) String nome,
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
        Boolean ativo) {
}
