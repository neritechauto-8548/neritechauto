package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.StatusConciliacao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ConciliacaoBancariaResponse(
        Long id,
        Long empresaId,
        Long contaBancariaId,
        String contaBancariaNome,
        LocalDate dataConciliacao,
        BigDecimal saldoSistema,
        BigDecimal saldoBanco,
        BigDecimal diferenca,
        StatusConciliacao status,
        String arquivoExtratoUrl,
        String observacoes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
