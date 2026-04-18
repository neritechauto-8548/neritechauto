package com.neritech.saas.ordemservico.dto;

import com.neritech.saas.ordemservico.domain.enums.StatusExecucao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ItemOSServicoResponse(
        Long id,
        Long ordemServicoId,
        Long servicoId,
        String descricao,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal,
        BigDecimal descontoPercentual,
        BigDecimal descontoValor,
        BigDecimal valorFinal,
        Long mecanicoExecutorId,
        Integer tempoExecucaoPrevisto,
        Integer tempoExecucaoReal,
        LocalDateTime dataInicioExecucao,
        LocalDateTime dataFimExecucao,
        StatusExecucao statusExecucao,
        Integer garantiaDias,
        String observacoes,
        Boolean aprovadoCliente,
        LocalDateTime dataAprovacaoCliente,
        BigDecimal comissaoPercentual,
        BigDecimal comissaoValor,
        Integer ordemExecucao,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao,
        Integer versao) {
}
