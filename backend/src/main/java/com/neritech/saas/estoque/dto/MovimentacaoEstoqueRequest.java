package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MovimentacaoEstoqueRequest(
        @NotNull Long empresaId,
        @NotNull Long produtoId,
        @NotNull TipoMovimentacao tipoMovimentacao,
        String subtipoMovimentacao,
        @NotNull BigDecimal quantidade,
        BigDecimal valorUnitario,
        Long localizacaoOrigemId,
        Long localizacaoDestinoId,
        String documentoTipo,
        String documentoNumero,
        Long documentoId,
        Long fornecedorId,
        String loteNumero,
        LocalDate dataValidade,
        LocalDate dataFabricacao,
        String motivo,
        String observacoes,
        @NotNull Long usuarioResponsavel,
        Long ordemServicoId) {
}
