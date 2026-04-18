package com.neritech.saas.produtoServico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.neritech.saas.produtoServico.domain.enums.TipoPromocao;

public record PromocaoRequest(
                @NotNull(message = "O ID da empresa Ã© obrigatÃ³rio") Long empresaId,

                @NotBlank(message = "O nome da promoÃ§Ã£o Ã© obrigatÃ³rio") @Size(max = 255, message = "O nome da promoÃ§Ã£o deve ter no mÃ¡ximo 255 caracteres") String nome,

                String descricao,
                TipoPromocao tipoPromocao,
                BigDecimal valorDesconto,
                BigDecimal percentualDesconto,
                Integer quantidadeLeve,
                Integer quantidadePague,
                BigDecimal valorMinimoPedido,

                @NotNull(message = "A data de inÃ­cio Ã© obrigatÃ³ria") LocalDateTime dataInicio,

                @NotNull(message = "A data de fim Ã© obrigatÃ³ria") LocalDateTime dataFim,

                Integer limiteUsoTotal,
                Integer limiteUsoCliente,

                @Size(max = 20, message = "O cÃ³digo do cupom deve ter no mÃ¡ximo 20 caracteres") String codigoCupom,

                Boolean aplicacaoAutomatica,
                String categoriasAplicaveis,
                String produtosAplicaveis,
                String clientesAplicaveis,
                String canaisVenda,
                String observacoes,
                Boolean ativo) {
}
