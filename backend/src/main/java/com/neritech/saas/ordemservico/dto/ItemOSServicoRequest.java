package com.neritech.saas.ordemservico.dto;

import com.neritech.saas.ordemservico.domain.enums.StatusExecucao;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ItemOSServicoRequest(
        @NotNull Long ordemServicoId,
        Long servicoId,
        String descricao,
        BigDecimal quantidade,
        @NotNull BigDecimal valorUnitario,
        @NotNull BigDecimal valorTotal,
        BigDecimal descontoPercentual,
        BigDecimal descontoValor,
        @NotNull BigDecimal valorFinal,
        Long mecanicoExecutorId,
        Integer tempoExecucaoPrevisto,
        StatusExecucao statusExecucao,
        Integer garantiaDias,
        String observacoes,
        Boolean aprovadoCliente,
        BigDecimal comissaoPercentual,
        Integer ordemExecucao) {
}
