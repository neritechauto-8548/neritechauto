package com.neritech.saas.produtoServico.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutoFornecedorResponse(
                Long id,
                Long produtoId,
                String produtoNome,
                Long fornecedorId,
                String fornecedorNome,
                String codigoFornecedor,
                String descricaoFornecedor,
                BigDecimal precoCusto,
                BigDecimal precoCustoAnterior,
                LocalDate dataUltimoPreco,
                Integer prazoEntregaDias,
                BigDecimal quantidadeMinima,
                String descontoQuantidade,
                String moeda,
                Boolean principal,
                Boolean ativo,
                String observacoes) {
}
