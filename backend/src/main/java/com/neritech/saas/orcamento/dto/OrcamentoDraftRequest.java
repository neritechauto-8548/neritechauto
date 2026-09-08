package com.neritech.saas.orcamento.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Contrato mínimo da TELA-AUTO-ORC-002.
 * Tenant e número comercial são deliberadamente ausentes: ambos são autoridade do backend.
 */
public record OrcamentoDraftRequest(
        @NotNull Long clienteId,
        Long veiculoId,
        @Min(0) Integer quilometragemEntrada,
        Long responsavelId,
        @Size(max = 4000) String relatoCliente,
        @Size(max = 4000) String observacoesInternas,
        @Size(max = 4000) String observacoesCliente) {
}
