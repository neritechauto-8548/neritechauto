package com.neritech.saas.produtoServico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ServicoRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
        String nome,

        @NotNull(message = "O preço base é obrigatório")
        BigDecimal precoBase,

        @NotNull(message = "O custo é obrigatório")
        BigDecimal custo,

        String instrucoesExecucao,

        Boolean ativo) {
}
