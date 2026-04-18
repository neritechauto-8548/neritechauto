package com.neritech.saas.produtoServico.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.neritech.saas.produtoServico.domain.enums.TipoTabelaPreco;

public record TabelaPrecoResponse(
                Long id,
                Long empresaId,
                String nome,
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
