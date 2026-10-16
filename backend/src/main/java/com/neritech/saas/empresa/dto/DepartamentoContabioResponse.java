package com.neritech.saas.empresa.dto;

import java.time.LocalDateTime;

public record DepartamentoContabioResponse(
        Long id,
        Long empresaId,
        String empresaNome,
        String descricao,
        String codigo,
        String detalhes,
        String tipoOperacao,
        Long supervisorId,
        Boolean ativo,
        Integer ordem,
        Integer tempoPadraoMinutos,
        Long numeroFuncionarios,
        Long numeroCargos,
        Long numeroServicos,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao,
        Integer versao
) {}

