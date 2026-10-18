package com.neritech.saas.produtoServico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ServicoRequest(
        @Size(max = 50) String codigo,
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
        String nome,

        @NotNull(message = "A área é obrigatória") Long areaId,
        @NotBlank(message = "A unidade é obrigatória") @Size(max = 30) String unidade,
        @NotNull(message = "O tempo previsto é obrigatório")
        @jakarta.validation.constraints.DecimalMin(value = "0.01", message = "O tempo previsto deve ser maior que zero")
        BigDecimal tempoPrevistoHoras,

        @NotNull(message = "O preço base é obrigatório")
        BigDecimal precoBase,

        @NotNull(message = "O custo é obrigatório")
        BigDecimal custo,

        @jakarta.validation.constraints.DecimalMin(value = "0.0", message = "A alíquota não pode ser negativa")
        BigDecimal aliquotaIss,
        @Size(max = 30) String codigoServicoMunicipal,

        String instrucoesExecucao,

        Boolean ativo) {
}
