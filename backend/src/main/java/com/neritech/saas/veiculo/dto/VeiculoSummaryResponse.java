package com.neritech.saas.veiculo.dto;

import com.neritech.saas.veiculo.domain.enums.StatusVeiculo;

public record VeiculoSummaryResponse(
        Long id,
        String marcaNome,
        String modeloNome,
        Integer anoFabricacao,
        Integer anoModelo,
        String maskedPlate,
        StatusVeiculo status) {
}
