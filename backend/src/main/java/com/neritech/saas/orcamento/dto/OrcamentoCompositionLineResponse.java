package com.neritech.saas.orcamento.dto;

import java.math.BigDecimal;

public record OrcamentoCompositionLineResponse(
        Long id,
        String lineType,
        Long catalogItemId,
        Integer catalogVersion,
        String source,
        Long kitOriginId,
        Integer kitOriginVersion,
        String description,
        String reference,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal discountAmount,
        BigDecimal totalAmount,
        String availabilityStatus,
        int position) {
}
