package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.StatusFatura;
import com.neritech.saas.financeiro.domain.enums.TipoFatura;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FaturaRequest(
        @NotBlank(message = "NÃºmero da fatura Ã© obrigatÃ³rio") String numeroFatura,

        @NotNull(message = "Cliente Ã© obrigatÃ³rio") Long clienteId,

        Long ordemServicoId,
        TipoFatura tipoFatura,

        @NotNull(message = "Data de emissÃ£o Ã© obrigatÃ³ria") LocalDate dataEmissao,

        @NotNull(message = "Data de vencimento Ã© obrigatÃ³ria") LocalDate dataVencimento,

        BigDecimal valorDescontos,
        BigDecimal valorAcrescimos,
        StatusFatura status,
        Long formaPagamentoId,
        Long condicaoPagamentoId,
        String observacoes,
        String observacoesInternas,

        List<ItemFaturaRequest> itens) {
}
