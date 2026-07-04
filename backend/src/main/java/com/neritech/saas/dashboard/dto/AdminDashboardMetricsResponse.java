package com.neritech.saas.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AdminDashboardMetricsResponse {
    // Stripe Metrics
    private long totalStripeCustomers;
    private long activeSubscriptions;
    private long trailingSubscriptions;
    private long canceledSubscriptions;
    private long pastDueSubscriptions;
    private BigDecimal mrr; // Monthly Recurring Revenue
    
    // PostgreSQL Metrics
    private long totalOficinas;
    private long totalVeiculos;
    private long totalOrdensServico;
}
