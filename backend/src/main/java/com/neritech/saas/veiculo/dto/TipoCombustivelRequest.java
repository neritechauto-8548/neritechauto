package com.neritech.saas.veiculo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TipoCombustivelRequest(
                @NotBlank(message = "O nome é obrigatório") @Size(max = 30, message = "O nome deve ter no máximo 30 caracteres") String nome) {
}
