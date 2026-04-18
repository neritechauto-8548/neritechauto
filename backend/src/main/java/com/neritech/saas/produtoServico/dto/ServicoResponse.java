package com.neritech.saas.produtoServico.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServicoResponse(
        Long id,
        Long empresaId,
        String nome,
        BigDecimal precoBase,
        BigDecimal custo,
        String instrucoesExecucao,
        Boolean ativo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
