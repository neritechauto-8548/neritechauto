package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.enums.TipoEscala;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EscalaTrabalhoResponse(
        Long id,
        Long empresaId,
        Long funcionarioId,
        Long horarioTrabalhoId,
        TipoEscala tipoEscala,
        LocalDate dataInicio,
        LocalDate dataFim,
        Integer diasTrabalho,
        Integer diasFolga,
        String funcionariosIncluidos,
        String observacoes,
        Boolean ativo,
        Long criadoPor,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
