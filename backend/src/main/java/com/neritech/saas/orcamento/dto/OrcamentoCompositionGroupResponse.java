package com.neritech.saas.orcamento.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrcamentoCompositionGroupResponse(
        Long id,
        String title,
        String customerDescription,
        boolean recommended,
        String visibility,
        int position,
        BigDecimal subtotal,
        List<OrcamentoCompositionLineResponse> lines) {
}
