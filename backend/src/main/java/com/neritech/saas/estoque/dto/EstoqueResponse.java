package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.enums.StatusEstoque;
import java.math.BigDecimal;

public record EstoqueResponse(
        Long id,
        Long empresaId,
        Long produtoId,
        String produtoNome,
        BigDecimal quantidadeAtual,
        Long fornecedorId,
        String fornecedorNome,
        BigDecimal precoCustoLote,
        String notaFiscalNumero,
        String certificadoQualidadeUrl,
        StatusEstoque status,
        String motivoBloqueio,
        String observacoes,
        Long usuarioCadastro) {
}
