package com.neritech.saas.orcamento.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrcamentoCompositionGroupResponse(
        Long id,
        String title,
        String customerDescription,
        String internalNote,
        Long kitOriginId,
        Integer kitOriginVersion,
        boolean recommended,
        String visibility,
        int position,
        BigDecimal packagePrice,
        String packageDistributionMethod,
        BigDecimal packageOriginalSubtotal,
        BigDecimal packageAdjustmentAmount,
        String packagePriceSourceType,
        Long packagePriceSourceId,
        Integer packagePriceSourceVersion,
        String packageAppliedAt,
        String packageOverrideReason,
        String packageAuthorityStatus,
        BigDecimal subtotal,
        List<OrcamentoCompositionLineResponse> lines) {
}

