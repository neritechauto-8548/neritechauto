package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.BaseCalculoImposto;
import com.neritech.saas.financeiro.domain.enums.TipoImposto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ImpostoResponse(
        Long id,
        Long empresaId,
        TipoImposto tipoImposto,
        String nomeImposto,
        BigDecimal aliquotaPercentual,
        BaseCalculoImposto baseCalculo,
        String codigoReceita,
        String aplicavelRegime,
        Boolean ativo,
        LocalDate dataInicioVigencia,
        LocalDate dataFimVigencia,
        String observacoes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
