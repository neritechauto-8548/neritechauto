package com.neritech.saas.orcamento.dto;

import java.math.BigDecimal;

public record OrcamentoCatalogItemResponse(
        Long id,
        String lineType,
        String description,
        String reference,
        BigDecimal suggestedPrice,
        String availabilityStatus,
        Integer itemCount,
        Integer catalogVersion) {
}
