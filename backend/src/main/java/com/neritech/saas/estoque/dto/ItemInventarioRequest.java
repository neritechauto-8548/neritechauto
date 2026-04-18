package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.enums.StatusItemInventario;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ItemInventarioRequest(
        @NotNull Long inventarioId,
        @NotNull Long produtoId,
        Long localizacaoId,
        String loteNumero,
        @NotNull BigDecimal quantidadeSistema,
        BigDecimal quantidadeContada,
        BigDecimal valorUnitario,
        StatusItemInventario status,
        String motivoDiferenca,
        String observacoes,
        Long usuarioContagem,
        Long usuarioConferencia,
        String fotoComprovanteUrl) {
}
