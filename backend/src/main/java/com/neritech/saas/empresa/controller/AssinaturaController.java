package com.neritech.saas.empresa.controller;

import com.neritech.saas.common.tenancy.TenantContext;
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
            Long currentTenant = TenantContext.getCurrentTenant();
            if (currentTenant != null && !currentTenant.equals(empresaId)) {
                log.warn("Tentativa de acesso a assinatura de outra empresa. User Tenant: {}, Requested: {}", currentTenant, empresaId);
                empresaId = currentTenant;
            }

            Empresa empresa = this.empresaService.findById(empresaId);
            String customerId = empresa.getStripeCustomerId();

            if (!this.stripeService.isConfigured()) {
                // Se não houver Stripe, tenta buscar a assinatura local do banco
                java.util.Optional<com.neritech.saas.empresa.domain.AssinaturaEmpresa> localSub = 
                    this.empresaService.findActiveSubscriptionByEmpresa(empresaId);
                
                if (localSub.isPresent()) {
                    com.neritech.saas.empresa.domain.AssinaturaEmpresa sub = localSub.get();
                    return ResponseEntity.ok(AssinaturaDTO.builder()
                            .plano(sub.getPlano() != null ? sub.getPlano().getNome() : "Plano Local")
                            .status(sub.getStatus().name())
                            .precoFormatado(String.format("R$ %,.2f", sub.getValorMensal()))
                            .proximaCobranca(sub.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                            .build());
                }

                return ResponseEntity.ok(AssinaturaDTO.builder()
                        .plano("Sem faturamento ativo")
                        .status("INATIVO")
                        .precoFormatado("R$ 0,00")
                        .proximaCobranca("N/A")
                        .build());
            }

            // If no Stripe customer linked yet, try to find by email
            if (customerId == null || customerId.isBlank()) {
                Customer customer = this.stripeService.findCustomerByEmail(empresa.getEmail());
                if (customer != null) {
                    customerId = customer.getId();
                    empresa.setStripeCustomerId(customerId);
                    this.empresaService.update(empresa.getId(), empresa);
                } else {
                    return ResponseEntity.ok(AssinaturaDTO.builder()
                            .plano("Sem assinatura")
                            .status("inactive")
                            .build());
                }
            }

            // Get subscriptions from Stripe
            List<Subscription> subs = this.stripeService.getSubscriptions(customerId);
            if (subs.isEmpty()) {
                return ResponseEntity.ok(AssinaturaDTO.builder()
                        .plano("Sem assinatura")
                        .status("inactive")
                        .stripeCustomerId(customerId)
                        .build());
            }

            Subscription sub = subs.get(0); // Most recent
            String productId = sub.getItems().getData().get(0).getPrice().getProduct();
            String planName = this.stripeService.resolvePlanName(productId);
            Long amount = sub.getItems().getData().get(0).getPrice().getUnitAmount();
            String preco = amount != null ? String.format("R$ %,.2f", amount / 100.0) : "—";

            boolean isTrial = "trialing".equals(sub.getStatus());
            String inicioTrial = null;
            String fimTrial = null;
            long diasRestantesTrial = 0;

            if (sub.getTrialStart() != null) {
                inicioTrial = formatTimestamp(sub.getTrialStart());
            }
            if (sub.getTrialEnd() != null) {
                fimTrial = formatTimestamp(sub.getTrialEnd());
                long diff = sub.getTrialEnd() - Instant.now().getEpochSecond();
                diasRestantesTrial = Math.max(0, diff / (24 * 3600));
            }

            String statusMapeado;
            String stripeStatus = sub.getStatus();
            if ("active".equals(stripeStatus)) statusMapeado = "ATIVO";
            else if ("trialing".equals(stripeStatus)) statusMapeado = "TESTE";
            else if ("past_due".equals(stripeStatus)) statusMapeado = "ATRASADO";
            else if ("canceled".equals(stripeStatus)) statusMapeado = "CANCELADO";
            else if ("unpaid".equals(stripeStatus)) statusMapeado = "SUSPENSO";
            else if ("incomplete".equals(stripeStatus)) statusMapeado = "INCOMPLETO";
            else statusMapeado = "SUSPENSO";

            String proximaCobranca = sub.getCurrentPeriodEnd() != null
                    ? formatTimestamp(sub.getCurrentPeriodEnd()) : null;

            return ResponseEntity.ok(AssinaturaDTO.builder()
                    .plano(planName)
                    .status(statusMapeado)
                    .precoFormatado(preco)
                    .proximaCobranca(proximaCobranca)
                    .stripeCustomerId(customerId)
                    .stripeSubscriptionId(sub.getId())
                    .stripeProductId(productId)
                    .trial(isTrial)
                    .inicioTrial(inicioTrial)
                    .fimTrial(fimTrial)
                    .diasRestantesTrial(diasRestantesTrial)
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
            Long currentTenant = TenantContext.getCurrentTenant();
            if (currentTenant != null && !currentTenant.equals(empresaId)) {
                log.warn("Tentativa de abrir portal de outra empresa. User Tenant: {}, Requested: {}", currentTenant, empresaId);
                empresaId = currentTenant;
            }

            if (!this.stripeService.isConfigured()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "A API do Stripe não está configurada neste ambiente local."
                ));
            }

            Empresa empresa = this.empresaService.findById(empresaId);
            String customerId = empresa.getStripeCustomerId();

            if (customerId == null || customerId.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Empresa não possui cliente Stripe vinculado."
                ));
            }

            String returnUrl = body != null && body.containsKey("returnUrl")
                    ? body.get("returnUrl")
                    : "http://localhost:4200/configuracoes/assinatura";

            String portalUrl = this.stripeService.createBillingPortalSession(customerId, returnUrl);

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
