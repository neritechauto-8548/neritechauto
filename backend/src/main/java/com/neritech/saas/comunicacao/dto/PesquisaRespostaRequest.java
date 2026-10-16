package com.neritech.saas.comunicacao.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record PesquisaRespostaRequest(
    @NotNull Long clienteId,
    @NotBlank @Size(max = 36) String chaveIdempotencia,
    @NotEmpty @Valid List<Item> respostas
) {
    public record Item(@NotNull Long perguntaId, @NotBlank @Size(max = 1000) String valor) {}
}
