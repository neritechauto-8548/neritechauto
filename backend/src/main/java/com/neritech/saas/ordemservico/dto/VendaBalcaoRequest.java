package com.neritech.saas.ordemservico.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record VendaBalcaoRequest(
        @Valid @NotNull OrdemServicoRequest ordemServico,
        @Valid List<ItemOSProdutoRequest> produtos
) {
}
