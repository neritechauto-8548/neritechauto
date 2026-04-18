package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.enums.StatusInventario;
import com.neritech.saas.estoque.domain.enums.TipoInventario;
import java.math.BigDecimal;
import java.time.LocalDate;

public record InventarioResponse(
        Long id,
        Long empresaId,
        String codigo,
        String descricao,
        TipoInventario tipoInventario,
        LocalDate dataInicio,
        LocalDate dataFim,
        StatusInventario status,
        String localizacoesIncluidas,
        String categoriasIncluidas,
        String produtosIncluidos,
        String usuariosResponsaveis,
        Integer totalItensPlanejados,
        Integer totalItensContados,
        Integer totalDivergencias,
        BigDecimal valorTotalSistema,
        BigDecimal valorTotalContado,
        BigDecimal diferencaValor,
        String observacoes,
        Long finalizadoPor) {
}
