package com.neritech.saas.rh.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CargoRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 100) String nome,
        String descricao,
        @Size(max = 10) String codigoCbo,
        Integer nivelHierarquico,
        Long cargoSuperiorId,
        BigDecimal salarioBaseMinimo,
        BigDecimal salarioBaseMaximo,
        String requisitos,
        String responsabilidades,
        String beneficios,
        Boolean temComissao,
        BigDecimal percentualComissaoPadrao,
        BigDecimal metaVendasMensal,
        Integer cargaHorariaSemanal,
        Boolean ativo) {
}
