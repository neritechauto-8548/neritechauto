package com.neritech.saas.produtoServico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.neritech.saas.produtoServico.domain.enums.TipoTabelaPreco;

public record TabelaPrecoRequest(
                @NotNull(message = "O ID da empresa Ã© obrigatÃ³rio") Long empresaId,

                @NotBlank(message = "O nome da tabela de preÃ§o Ã© obrigatÃ³rio") @Size(max = 100, message = "O nome da tabela de preÃ§o deve ter no mÃ¡ximo 100 caracteres") String nome,

                String descricao,
                TipoTabelaPreco tipoTabela,
                Long grupoClienteId,
                BigDecimal margemLucroPadrao,
                BigDecimal descontoMaximoPermitido,
                LocalDate dataInicio,
                LocalDate dataFim,
                Boolean ativo,
                Boolean padrao,
                String observacoes) {
}
