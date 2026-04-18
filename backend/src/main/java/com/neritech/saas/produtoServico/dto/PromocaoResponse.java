package com.neritech.saas.produtoServico.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.neritech.saas.produtoServico.domain.enums.TipoPromocao;

public record PromocaoResponse(
                Long id,
                Long empresaId,
                String nome,
                String descricao,
                TipoPromocao tipoPromocao,
                BigDecimal valorDesconto,
                BigDecimal percentualDesconto,
                Integer quantidadeLeve,
                Integer quantidadePague,
                BigDecimal valorMinimoPedido,
                LocalDateTime dataInicio,
                LocalDateTime dataFim,
                Integer limiteUsoTotal,
                Integer limiteUsoCliente,
                Integer vezesUtilizada,
                String codigoCupom,
                Boolean aplicacaoAutomatica,
                String categoriasAplicaveis,
                String produtosAplicaveis,
                String clientesAplicaveis,
                String canaisVenda,
                String observacoes,
                Boolean ativo) {
}
