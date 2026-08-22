package com.neritech.saas.orcamento.dto;

import java.util.List;

public record OrcamentoListResponse(
        List<OrcamentoListItemResponse> items,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean summaryAvailable,
        String summaryUnavailableReason) {
}
