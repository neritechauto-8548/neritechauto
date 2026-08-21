package com.neritech.saas.cliente.dto;

public record EnderecoClienteSummaryResponse(
        Long id,
        String locationSummary,
        String maskedPostalCode,
        String country) {
}
