package com.neritech.saas.orcamento.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrcamentoListItemResponse(
        Long id,
        String numero,
        Integer versaoAtual,
        OrcamentoListCustomerResponse cliente,
        OrcamentoListVehicleResponse veiculo,
        String status,
        OrcamentoMoneyResponse total,
        LocalDateTime validadeEm,
        Long responsavelId,
        String comunicacaoStatus,
        String proximaAcao,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm,
        List<String> allowedActions) {
}
