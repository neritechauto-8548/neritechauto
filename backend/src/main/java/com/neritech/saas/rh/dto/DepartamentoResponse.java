package com.neritech.saas.rh.dto;

import java.time.LocalDateTime;

public record DepartamentoResponse(
        Long id,
        Long empresaId,
        String nome,
        String descricao,
        String codigo,
        Long departamentoPaiId,
        Long gerenteId,
        String centroCusto,
        Boolean ativo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
