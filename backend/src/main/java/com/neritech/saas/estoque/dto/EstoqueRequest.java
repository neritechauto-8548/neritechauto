package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.enums.StatusEstoque;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record EstoqueRequest(
        @NotNull(message = "O ID da empresa Ã© obrigatÃ³rio") Long empresaId,

        @NotNull(message = "O ID do produto Ã© obrigatÃ³rio") Long produtoId,

        BigDecimal quantidadeAtual,
        Long fornecedorId,
        BigDecimal precoCustoLote,
        String notaFiscalNumero,
        String certificadoQualidadeUrl,
        StatusEstoque status,
        String motivoBloqueio,
        String observacoes,
        Long usuarioCadastro) {
}
