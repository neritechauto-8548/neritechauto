package com.neritech.saas.produtoServico.dto;

import com.neritech.saas.produtoServico.domain.enums.OrigemProduto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProdutoRequest(
                @NotNull(message = "O ID da empresa Ã© obrigatÃ³rio") Long empresaId,

                Long categoriaId,

                Long unidadeMedidaId,

                @NotBlank(message = "O cÃ³digo interno Ã© obrigatÃ³rio") @Size(max = 50, message = "O cÃ³digo interno deve ter no mÃ¡ximo 50 caracteres") String codigoInterno,

                @Size(max = 50, message = "O cÃ³digo de barras deve ter no mÃ¡ximo 50 caracteres") String codigoBarras,

                @Size(max = 50, message = "O cÃ³digo do fabricante deve ter no mÃ¡ximo 50 caracteres") String codigoFabricante,

                @NotBlank(message = "O nome Ã© obrigatÃ³rio") @Size(max = 255, message = "O nome deve ter no mÃ¡ximo 255 caracteres") String nome,

                String descricao,
                String descricaoDetalhada,

                @Size(max = 100, message = "A marca deve ter no mÃ¡ximo 100 caracteres") String marca,

                @Size(max = 100, message = "O modelo deve ter no mÃ¡ximo 100 caracteres") String modelo,

                String aplicacao,
                String especificacoesTecnicas,

                BigDecimal pesoLiquido,
                BigDecimal pesoBruto,
                BigDecimal dimensoesComprimento,
                BigDecimal dimensoesLargura,
                BigDecimal dimensoesAltura,

                @NotNull(message = "O preÃ§o de custo Ã© obrigatÃ³rio") BigDecimal precoCusto,

                @NotNull(message = "O preÃ§o de venda Ã© obrigatÃ³rio") BigDecimal precoVenda,

                BigDecimal margemLucroPercentual,
                BigDecimal estoqueMinimo,
                BigDecimal estoqueMaximo,
                BigDecimal pontoReposicao,

                Boolean controlaLote,
                Boolean controlaValidade,
                Integer diasValidade,

                @Size(max = 8, message = "O NCM deve ter no mÃ¡ximo 8 caracteres") String ncm,

                @Size(max = 7, message = "O CEST deve ter no mÃ¡ximo 7 caracteres") String cest,

                @Size(max = 1, message = "A origem do produto deve ter no mÃ¡ximo 1 caractere") String origemProduto,

                @Size(max = 500, message = "A URL da foto principal deve ter no mÃ¡ximo 500 caracteres") String fotoPrincipalUrl,

                Integer garantiaMeses,
                String observacoes,
                Boolean ativo,
                Boolean destaque,
                Boolean promocional,
                Integer pontosFidelidade,
                BigDecimal comissaoVendaPercentual,
                BigDecimal quantidadeEstoque) {
}
