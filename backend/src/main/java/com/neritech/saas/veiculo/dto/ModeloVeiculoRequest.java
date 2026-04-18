package com.neritech.saas.veiculo.dto;

import com.neritech.saas.veiculo.domain.enums.CategoriaVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record ModeloVeiculoRequest(
                @NotNull(message = "A marca é obrigatória") Long marcaId,

                @NotBlank(message = "O nome do modelo é obrigatório") @Size(max = 150, message = "O nome do modelo deve ter no máximo 150 caracteres") String nome,

                @NotNull(message = "A categoria é obrigatória") CategoriaVeiculo categoria,

                @URL(message = "A URL do logo é inválida") @Size(max = 500, message = "A URL do logo deve ter no máximo 500 caracteres") String logoUrl) {
}
