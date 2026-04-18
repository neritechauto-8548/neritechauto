package com.neritech.saas.produtoServico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PedidoFornecedorRequest(
        @NotNull(message = "O ID da empresa é obrigatório") Long empresaId,
        @NotNull(message = "O ID do fornecedor é obrigatório") Long fornecedorId,
        @NotBlank(message = "O responsável é obrigatório") String responsavel,
        LocalDate dataPrevisao,
        String numeroNf,
        String observacao,
        String descricaoInterna
) {}
