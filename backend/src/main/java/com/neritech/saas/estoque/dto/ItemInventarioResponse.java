package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.enums.StatusItemInventario;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ItemInventarioResponse(
        Long id,
        Long inventarioId,
        Long produtoId,
        String produtoNome,
        Long localizacaoId,
        String localizacaoNome,
        String loteNumero,
        BigDecimal quantidadeSistema,
        BigDecimal quantidadeContada,
        BigDecimal diferenca,
        BigDecimal valorUnitario,
        BigDecimal valorTotalSistema,
        BigDecimal valorTotalContado,
        BigDecimal diferencaValor,
        StatusItemInventario status,
        String motivoDiferenca,
        String observacoes,
        Long usuarioContagem,
        LocalDateTime dataContagem,
        Long usuarioConferencia,
        LocalDateTime dataConferencia,
        String fotoComprovanteUrl) {
}
