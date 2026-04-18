package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.enums.TipoMovimentacao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MovimentacaoEstoqueResponse(
        Long id,
        Long empresaId,
        Long produtoId,
        String produtoNome,
        TipoMovimentacao tipoMovimentacao,
        String subtipoMovimentacao,
        BigDecimal quantidade,
        BigDecimal quantidadeAnterior,
        BigDecimal quantidadeAtual,
        BigDecimal valorUnitario,
        BigDecimal valorTotal,
        Long localizacaoOrigemId,
        String localizacaoOrigemNome,
        Long localizacaoDestinoId,
        String localizacaoDestinoNome,
        String documentoTipo,
        String documentoNumero,
        Long documentoId,
        Long fornecedorId,
        String fornecedorNome,
        String loteNumero,
        LocalDate dataValidade,
        LocalDate dataFabricacao,
        String motivo,
        String observacoes,
        Long usuarioResponsavel,
        LocalDateTime dataMovimentacao,
        Long ordemServicoId) {
}
