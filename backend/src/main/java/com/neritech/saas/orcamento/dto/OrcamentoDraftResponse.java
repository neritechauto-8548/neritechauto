package com.neritech.saas.orcamento.dto;

import java.time.LocalDateTime;

/** Resposta mínima do rascunho criado pela TELA-AUTO-ORC-002. */
public record OrcamentoDraftResponse(
        Long id,
        String numeroOrcamento,
        String status,
        Long clienteId,
        Long veiculoId,
        LocalDateTime criadoEm) {
}
