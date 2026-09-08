package com.neritech.saas.orcamento.dto;

import java.math.BigDecimal;

public record OrcamentoCommercialPermissionsResponse(
        boolean canEditPackagePrice,
        boolean canEditUnitPrice,
        boolean canApplyDiscount,
        boolean canApproveDiscount,
        boolean canViewCost,
        BigDecimal discountAuthorityPercent) {
}

