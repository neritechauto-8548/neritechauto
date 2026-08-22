package com.neritech.saas.orcamento.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrcamentoCompositionResponse(
        Long budgetId,
        long revision,
        String calculationStatus,
        String currency,
        BigDecimal requiredTotal,
        BigDecimal recommendedTotal,
        BigDecimal partsTotal,
        BigDecimal laborTotal,
        int groupCount,
        int lineCount,
        boolean canReview,
        List<String> blockers,
        List<OrcamentoCompositionGroupResponse> groups) {
}
