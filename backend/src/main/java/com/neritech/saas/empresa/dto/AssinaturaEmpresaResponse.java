package com.neritech.saas.empresa.dto;

import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import com.neritech.saas.empresa.domain.enums.FormaPagamento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AssinaturaEmpresaResponse(
        Long id,
        Long empresaId,
        String empresaNome,
        Long planoId,
        String planoNome,
        LocalDate dataInicio,
        LocalDate dataFim,
        Integer dataVencimentoMensal,
        BigDecimal valorMensal,
        BigDecimal valorAnual,
        BigDecimal descontoPercentual,
        StatusAssinatura status,
        FormaPagamento formaPagamento,
        Boolean renovacaoAutomatica,
        LocalDate dataCancelamento,
        String motivoCancelamento,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
