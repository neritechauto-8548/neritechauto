package com.neritech.saas.orcamento.dto;

import java.util.List;

public record OrcamentoCatalogSearchResponse(
        String query,
        List<OrcamentoCatalogItemResponse> items,
        boolean truncated) {
}
