package com.neritech.saas.produtoServico.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServicoResponse(
        Long id,
        Long empresaId,
        String codigo,
        String nome,
        Long areaId,
        String unidade,
        BigDecimal tempoPrevistoHoras,
        BigDecimal precoBase,
        BigDecimal custo,
        BigDecimal aliquotaIss,
        String codigoServicoMunicipal,
        String instrucoesExecucao,
        Boolean ativo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
