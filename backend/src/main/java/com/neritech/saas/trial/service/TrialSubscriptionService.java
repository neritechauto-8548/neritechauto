package com.neritech.saas.trial.service;

import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import com.neritech.saas.empresa.repository.AssinaturaEmpresaRepository;
import com.neritech.saas.empresa.repository.PlanoAssinaturaRepository;
import com.neritech.saas.empresa.service.StripeService;
import com.stripe.model.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrialSubscriptionService {

    private static final int DEFAULT_TRIAL_DAYS = 180;
    private static final int DEFAULT_PLAN_LEVEL = 1;
    private static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Sao_Paulo");

    private final AssinaturaEmpresaRepository assinaturaEmpresaRepository;
    private final PlanoAssinaturaRepository planoAssinaturaRepository;
    private final StripeService stripeService;

    public AssinaturaEmpresa ensureTrialSubscription(
            Empresa empresa,
            String stripeCustomerId,
            Subscription stripeSubscription
    ) {
        if (empresa == null || empresa.getId() == null) {
            throw new IllegalArgumentException("Empresa persistida é obrigatória para criar a assinatura de trial.");
        }

        AssinaturaEmpresa assinatura = findExisting(stripeSubscription);
        PlanoAssinatura plano = resolvePlan(stripeSubscription);

        assinatura.setEmpresa(empresa);
        assinatura.setPlano(plano);
        assinatura.setStripeCustomerId(stripeCustomerId);
        assinatura.setValorMensal(plano.getPrecoMensal() != null ? plano.getPrecoMensal() : BigDecimal.ZERO);

        if (stripeSubscription != null) {
            hydrateFromStripe(assinatura, stripeSubscription);
        } else {
            hydrateLocalFallback(assinatura);
        }

        AssinaturaEmpresa saved = assinaturaEmpresaRepository.save(assinatura);
        log.info("Assinatura de trial garantida para empresa {} no plano {}", empresa.getId(), plano.getNome());
        return saved;
    }

    private AssinaturaEmpresa findExisting(Subscription stripeSubscription) {
        if (stripeSubscription == null || stripeSubscription.getId() == null) {
            return new AssinaturaEmpresa();
        }

        return assinaturaEmpresaRepository.findByStripeSubscriptionId(stripeSubscription.getId())
                .orElseGet(AssinaturaEmpresa::new);
    }

    private PlanoAssinatura resolvePlan(Subscription stripeSubscription) {
        String productId = extractProductId(stripeSubscription);

        if (productId != null) {
            String planName = stripeService.resolvePlanName(productId);
            if (planName != null && !"Plano desconhecido".equalsIgnoreCase(planName)) {
                var mappedPlan = planoAssinaturaRepository
                        .findFirstByNomeIgnoreCaseAndAtivoTrueOrderByIdAsc(planName);
                if (mappedPlan.isPresent()) {
                    return mappedPlan.get();
                }
            }
        }

        return planoAssinaturaRepository
                .findFirstByNivelAndAtivoTrueOrderByIdAsc(DEFAULT_PLAN_LEVEL)
                .orElseThrow(() -> new IllegalStateException(
                        "Plano padrão de trial (nível " + DEFAULT_PLAN_LEVEL + ") não encontrado ou inativo."
                ));
    }

    private void hydrateFromStripe(AssinaturaEmpresa assinatura, Subscription stripeSubscription) {
        assinatura.setStripeSubscriptionId(stripeSubscription.getId());
        assinatura.setStripeProductId(extractProductId(stripeSubscription));

        LocalDateTime now = LocalDateTime.now(BUSINESS_ZONE);
        LocalDateTime start = toDateTime(stripeSubscription.getCurrentPeriodStart(), now);
        LocalDateTime trialEnd = toDateTime(
                stripeSubscription.getTrialEnd(),
                now.plusDays(DEFAULT_TRIAL_DAYS)
        );
        LocalDateTime periodEnd = toDateTime(stripeSubscription.getCurrentPeriodEnd(), trialEnd);

        assinatura.setDataInicio(start.toLocalDate());
        assinatura.setDataFim(periodEnd.toLocalDate());
        assinatura.setTrialEndsAt(trialEnd);
        assinatura.setSubscriptionEndsAt(periodEnd);
        assinatura.setStatus(mapStatus(stripeSubscription.getStatus()));
    }

    private void hydrateLocalFallback(AssinaturaEmpresa assinatura) {
        LocalDateTime now = LocalDateTime.now(BUSINESS_ZONE);
        LocalDateTime trialEnd = now.plusDays(DEFAULT_TRIAL_DAYS);

        assinatura.setDataInicio(now.toLocalDate());
        assinatura.setDataFim(trialEnd.toLocalDate());
        assinatura.setTrialEndsAt(trialEnd);
        assinatura.setSubscriptionEndsAt(trialEnd);
        assinatura.setStatus(StatusAssinatura.TESTE);
    }

    private StatusAssinatura mapStatus(String stripeStatus) {
        if (stripeStatus == null || "trialing".equalsIgnoreCase(stripeStatus)) {
            return StatusAssinatura.TESTE;
        }
        if ("active".equalsIgnoreCase(stripeStatus)) {
            return StatusAssinatura.ATIVO;
        }
        if ("past_due".equalsIgnoreCase(stripeStatus)) {
            return StatusAssinatura.ATRASADO;
        }
        if ("canceled".equalsIgnoreCase(stripeStatus)) {
            return StatusAssinatura.CANCELADO;
        }
        if ("incomplete".equalsIgnoreCase(stripeStatus)) {
            return StatusAssinatura.INCOMPLETO;
        }
        if ("unpaid".equalsIgnoreCase(stripeStatus)) {
            return StatusAssinatura.SUSPENSO;
        }
        return StatusAssinatura.SUSPENSO;
    }

    private String extractProductId(Subscription stripeSubscription) {
        if (stripeSubscription == null
                || stripeSubscription.getItems() == null
                || stripeSubscription.getItems().getData() == null
                || stripeSubscription.getItems().getData().isEmpty()
                || stripeSubscription.getItems().getData().get(0).getPrice() == null) {
            return null;
        }

        return stripeSubscription.getItems().getData().get(0).getPrice().getProduct();
    }

    private LocalDateTime toDateTime(Long epochSeconds, LocalDateTime fallback) {
        if (epochSeconds == null) {
            return fallback;
        }
        return Instant.ofEpochSecond(epochSeconds).atZone(BUSINESS_ZONE).toLocalDateTime();
    }
}
