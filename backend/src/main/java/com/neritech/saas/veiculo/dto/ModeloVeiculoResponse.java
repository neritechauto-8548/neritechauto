package com.neritech.saas.veiculo.dto;

import com.neritech.saas.veiculo.domain.enums.CategoriaVeiculo;

public record ModeloVeiculoResponse(
                Long id,
                Long marcaId,
                String marcaNome,
                String nome,
                CategoriaVeiculo categoria,
                String logoUrl) {
}
