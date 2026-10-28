package com.neritech.saas.rh.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CargoResponse(
        Long id,
        Long empresaId,
        String nome,
        String descricao,
        String codigoCbo,
        Long areaId,
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
        Boolean ativo,
        Long numeroFuncionarios,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao,
        Integer versao) {
}
