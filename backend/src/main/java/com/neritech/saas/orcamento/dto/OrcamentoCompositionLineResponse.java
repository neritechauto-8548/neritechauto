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
        BigDecimal grossAmount,
        BigDecimal discountAmount,
        String discountType,
        BigDecimal discountValue,
        String discountReason,
        String discountAuthorityStatus,
        BigDecimal discountAuthorityLimitPercent,
        Long discountApprovalRequestId,
        BigDecimal totalAmount,
        BigDecimal allocatedPackageAmount,
        BigDecimal packageAdjustmentAmount,
        String priceSourceType,
        Long priceSourceId,
        Integer priceSourceVersion,
        String priceAppliedAt,
        boolean priceOverridden,
        String priceOverrideReason,
        String availabilityStatus,
        int position) {
}

