package com.neritech.saas.ordemservico.dto;

import com.neritech.saas.estoque.domain.enums.MotivoDevolucao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ItemOSProdutoResponse(
        Long id,
        Long ordemServicoId,
        Long produtoId,
        String descricao,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal,
        BigDecimal descontoPercentual,
        BigDecimal descontoValor,
        BigDecimal valorFinal,
        String loteNumero,
        Long localizacaoEstoqueId,
        BigDecimal quantidadeReservada,
        BigDecimal quantidadeUtilizada,
        LocalDateTime dataReserva,
        LocalDateTime dataUtilizacao,
        Long fornecedorId,
        BigDecimal precoCusto,
        BigDecimal margemLucroRealizada,
        Integer garantiaMeses,
        String numeroSerie,
        String observacoes,
        Boolean aprovadoCliente,
        LocalDateTime dataAprovacaoCliente,
        Boolean devolvido,
        BigDecimal quantidadeDevolvida,
        String motivoDevolucao,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao,
        Integer versao) {
}
