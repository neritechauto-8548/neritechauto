package com.neritech.saas.produtoServico.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutoFornecedorRequest(
                @NotNull(message = "O ID do produto Ã© obrigatÃ³rio") Long produtoId,

                @NotNull(message = "O ID do fornecedor Ã© obrigatÃ³rio") Long fornecedorId,

                @Size(max = 50, message = "O cÃ³digo do fornecedor deve ter no mÃ¡ximo 50 caracteres") String codigoFornecedor,

                @Size(max = 255, message = "A descriÃ§Ã£o do fornecedor deve ter no mÃ¡ximo 255 caracteres") String descricaoFornecedor,

                @NotNull(message = "O preÃ§o de custo Ã© obrigatÃ³rio") BigDecimal precoCusto,

                BigDecimal precoCustoAnterior,
                LocalDate dataUltimoPreco,
                Integer prazoEntregaDias,
                BigDecimal quantidadeMinima,
                String descontoQuantidade,

                @Size(max = 3, message = "A moeda deve ter no mÃ¡ximo 3 caracteres") String moeda,

                Boolean principal,
                Boolean ativo,
                String observacoes) {
}
