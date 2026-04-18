package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.enums.StatusInventario;
import com.neritech.saas.estoque.domain.enums.TipoInventario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record InventarioRequest(
        @NotNull Long empresaId,
        @NotBlank String codigo,
        @NotBlank String descricao,
        TipoInventario tipoInventario,
        @NotNull LocalDate dataInicio,
        LocalDate dataFim,
        StatusInventario status,
        String localizacoesIncluidas,
        String categoriasIncluidas,
        String produtosIncluidos,
        String usuariosResponsaveis,
        String observacoes) {
}
