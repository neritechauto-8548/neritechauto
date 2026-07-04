package com.neritech.saas.dashboard.service;

import com.neritech.saas.dashboard.dto.AdminDashboardMetricsResponse;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.empresa.service.StripeService;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionCollection;
import com.stripe.param.CustomerListParams;
import com.stripe.param.SubscriptionListParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminDashboardService {

    private final EmpresaRepository empresaRepository;
    private final VeiculoRepository veiculoRepository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final StripeService stripeService; // Utilizado para checar configuração

    public AdminDashboardMetricsResponse getMetrics() {
        // Postgres Metrics
        long totalOficinas = empresaRepository.count();
        long totalVeiculos = veiculoRepository.count();
        long totalOrdensServico = ordemServicoRepository.count();

        AdminDashboardMetricsResponse.AdminDashboardMetricsResponseBuilder builder = AdminDashboardMetricsResponse.builder()
                .totalOficinas(totalOficinas)
                .totalVeiculos(totalVeiculos)
                .totalOrdensServico(totalOrdensServico);

        if (!stripeService.isConfigured()) {
            log.warn("Stripe is not configured. Returning local metrics only.");
            return builder.build();
        }

        try {
            // Conta os clientes no Stripe (usamos a contagem total, o ideal para milhões de clientes seria outro método)
            long totalStripeCustomers = countStripeCustomers();
            builder.totalStripeCustomers(totalStripeCustomers);

            // Analisa as assinaturas
            analyzeSubscriptions(builder);

        } catch (StripeException e) {
            log.error("Erro ao buscar dados do Stripe para o Admin Dashboard", e);
        }

        return builder.build();
    }

    private long countStripeCustomers() throws StripeException {
        long count = 0;
        CustomerListParams params = CustomerListParams.builder().setLimit(100L).build();
        for (Customer c : Customer.list(params).autoPagingIterable()) {
            count++;
        }
        return count;
    }

    private void analyzeSubscriptions(AdminDashboardMetricsResponse.AdminDashboardMetricsResponseBuilder builder) throws StripeException {
        long active = 0;
        long trialing = 0;
        long canceled = 0;
        long pastDue = 0;
        BigDecimal mrr = BigDecimal.ZERO;

        SubscriptionListParams params = SubscriptionListParams.builder().setLimit(100L).setStatus(SubscriptionListParams.Status.ALL).build();
        for (Subscription sub : Subscription.list(params).autoPagingIterable()) {
            String status = sub.getStatus();
            if ("active".equalsIgnoreCase(status)) {
                active++;
                mrr = mrr.add(calculateSubscriptionMRR(sub));
            } else if ("trialing".equalsIgnoreCase(status)) {
                trialing++;
                // trial generally doesn't add to MRR until it converts
            } else if ("canceled".equalsIgnoreCase(status)) {
                canceled++;
            } else if ("past_due".equalsIgnoreCase(status)) {
                pastDue++;
            }
        }

        builder.activeSubscriptions(active)
               .trailingSubscriptions(trialing)
               .canceledSubscriptions(canceled)
               .pastDueSubscriptions(pastDue)
               .mrr(mrr);
    }

    private BigDecimal calculateSubscriptionMRR(Subscription sub) {
        if (sub.getItems() == null || sub.getItems().getData() == null) return BigDecimal.ZERO;
        
        BigDecimal total = BigDecimal.ZERO;
        for (var item : sub.getItems().getData()) {
            if (item.getPrice() != null && item.getPrice().getUnitAmount() != null) {
                // Unit amount is in cents
                BigDecimal amount = BigDecimal.valueOf(item.getPrice().getUnitAmount()).divide(BigDecimal.valueOf(100));
                BigDecimal quantity = BigDecimal.valueOf(item.getQuantity() != null ? item.getQuantity() : 1);
                
                // If it's a monthly subscription
                if ("month".equalsIgnoreCase(item.getPrice().getRecurring().getInterval())) {
                    long intervalCount = item.getPrice().getRecurring().getIntervalCount();
                    // if billed every month, add to MRR. If billed every 3 months, divide by 3.
                    amount = amount.divide(BigDecimal.valueOf(intervalCount), 2, java.math.RoundingMode.HALF_UP);
                    total = total.add(amount.multiply(quantity));
                } else if ("year".equalsIgnoreCase(item.getPrice().getRecurring().getInterval())) {
                    // if yearly, divide by 12
                    long intervalCount = item.getPrice().getRecurring().getIntervalCount();
                    amount = amount.divide(BigDecimal.valueOf(12 * intervalCount), 2, java.math.RoundingMode.HALF_UP);
                    total = total.add(amount.multiply(quantity));
                }
            }
        }
        return total;
    }
}
