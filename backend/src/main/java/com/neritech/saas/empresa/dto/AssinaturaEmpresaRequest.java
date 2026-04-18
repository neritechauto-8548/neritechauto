package com.neritech.saas.empresa.dto;

import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import com.neritech.saas.empresa.domain.enums.FormaPagamento;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AssinaturaEmpresaRequest(
        @NotNull Long empresaId,
        @NotNull Long planoId,
        @NotNull LocalDate dataInicio,
        @NotNull LocalDate dataFim,
        Integer dataVencimentoMensal,
        @NotNull @DecimalMin("0.0") BigDecimal valorMensal,
        BigDecimal valorAnual,
        BigDecimal descontoPercentual,
        StatusAssinatura status,
        FormaPagamento formaPagamento,
        Boolean renovacaoAutomatica,
        LocalDate dataCancelamento,
        String motivoCancelamento) {
}
