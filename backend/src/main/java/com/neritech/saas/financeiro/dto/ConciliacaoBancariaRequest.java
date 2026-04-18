package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.StatusConciliacao;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ConciliacaoBancariaRequest(
        @NotNull(message = "Conta bancÃ¡ria Ã© obrigatÃ³ria") Long contaBancariaId,

        @NotNull(message = "Data de conciliaÃ§Ã£o Ã© obrigatÃ³ria") LocalDate dataConciliacao,

        @NotNull(message = "Saldo do sistema Ã© obrigatÃ³rio") BigDecimal saldoSistema,

        @NotNull(message = "Saldo do banco Ã© obrigatÃ³rio") BigDecimal saldoBanco,

        BigDecimal diferenca,
        StatusConciliacao status,
        String arquivoExtratoUrl,
        String observacoes) {
}
