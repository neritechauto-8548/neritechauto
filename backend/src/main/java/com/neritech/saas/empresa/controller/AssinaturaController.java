package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.dto.AssinaturaDTO;
import com.neritech.saas.empresa.service.EmpresaService;
import com.neritech.saas.empresa.service.StripeService;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/assinatura")
@Tag(name = "Assinatura", description = "Gerência de assinatura Stripe")
@RequiredArgsConstructor
@Slf4j
public class AssinaturaController {

    private final StripeService stripeService;
    private final EmpresaService empresaService;

    @GetMapping("/status/{empresaId}")
    @Operation(summary = "Status da assinatura", description = "Retorna o status da assinatura da empresa")
    public ResponseEntity<AssinaturaDTO> getStatus(@PathVariable Long empresaId) {
        try {
            Empresa empresa = empresaService.findById(empresaId);
            String customerId = empresa.getStripeCustomerId();

            // If no Stripe customer linked yet, try to find by email
            if (customerId == null || customerId.isBlank()) {
                Customer customer = stripeService.findCustomerByEmail(empresa.getEmail());
                if (customer != null) {
                    customerId = customer.getId();
                    empresa.setStripeCustomerId(customerId);
                    empresaService.update(empresa.getId(), empresa);
                } else {
                    return ResponseEntity.ok(AssinaturaDTO.builder()
                            .plano("Sem assinatura")
                            .status("inactive")
                            .build());
                }
            }

            // Get subscriptions from Stripe
            List<Subscription> subs = stripeService.getSubscriptions(customerId);
            if (subs.isEmpty()) {
                return ResponseEntity.ok(AssinaturaDTO.builder()
                        .plano("Sem assinatura")
                        .status("inactive")
                        .stripeCustomerId(customerId)
                        .build());
            }

            Subscription sub = subs.get(0); // Most recent
            String productId = sub.getItems().getData().get(0).getPrice().getProduct();
            String planName = stripeService.resolvePlanName(productId);
            Long amount = sub.getItems().getData().get(0).getPrice().getUnitAmount();
            String preco = amount != null ? String.format("R$ %,.2f", amount / 100.0) : "—";

            boolean isTrial = "trialing".equals(sub.getStatus());
            String inicioTrial = null;
            String fimTrial = null;
            if (sub.getTrialStart() != null) {
                inicioTrial = formatTimestamp(sub.getTrialStart());
            }
            if (sub.getTrialEnd() != null) {
                fimTrial = formatTimestamp(sub.getTrialEnd());
            }

            String proximaCobranca = sub.getCurrentPeriodEnd() != null
                    ? formatTimestamp(sub.getCurrentPeriodEnd()) : null;

            return ResponseEntity.ok(AssinaturaDTO.builder()
                    .plano(planName)
                    .status(sub.getStatus())
                    .precoFormatado(preco)
                    .proximaCobranca(proximaCobranca)
                    .stripeCustomerId(customerId)
                    .stripeSubscriptionId(sub.getId())
                    .stripeProductId(productId)
                    .trial(isTrial)
                    .inicioTrial(inicioTrial)
                    .fimTrial(fimTrial)
                    .build());

        } catch (Exception e) {
            log.error("Erro ao buscar status da assinatura", e);
            return ResponseEntity.ok(AssinaturaDTO.builder()
                    .plano("Erro ao consultar")
                    .status("error")
                    .build());
        }
    }

    @PostMapping("/portal/{empresaId}")
    @Operation(summary = "Portal de faturamento", description = "Gera URL do Stripe Billing Portal")
    public ResponseEntity<Map<String, String>> createPortalSession(
            @PathVariable Long empresaId,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            Empresa empresa = empresaService.findById(empresaId);
            String customerId = empresa.getStripeCustomerId();

            if (customerId == null || customerId.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Empresa não possui cliente Stripe vinculado."
                ));
            }

            String returnUrl = body != null && body.containsKey("returnUrl")
                    ? body.get("returnUrl")
                    : "http://localhost:4200/configuracoes/assinatura";

            String portalUrl = stripeService.createBillingPortalSession(customerId, returnUrl);

            return ResponseEntity.ok(Map.of("url", portalUrl));

        } catch (Exception e) {
            log.error("Erro ao criar sessão do portal Stripe", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Erro ao gerar portal de faturamento: " + e.getMessage()
            ));
        }
    }

    private String formatTimestamp(Long timestamp) {
        return Instant.ofEpochSecond(timestamp)
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
