package com.neritech.saas.empresa.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import com.stripe.model.billingportal.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.SubscriptionListParams;
import com.stripe.param.billingportal.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import com.neritech.saas.empresa.repository.AssinaturaEmpresaRepository;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.empresa.repository.PlanoAssinaturaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeService {

    private final EmpresaRepository empresaRepository;
    private final AssinaturaEmpresaRepository assinaturaEmpresaRepository;
    private final PlanoAssinaturaRepository planoAssinaturaRepository;

    @Value("${stripe.api.key:}")
    private String stripeApiKey;

    // Optional: If there's a specific default plan price ID for the trial
    @Value("${stripe.default.price.id:}")
    private String defaultPriceId;

    @Value("${stripe.products.start:}")
    private String productIdStart;

    @Value("${stripe.products.pro:}")
    private String productIdPro;

    @Value("${stripe.products.elite:}")
    private String productIdElite;

    @PostConstruct
    public void init() {
        if (stripeApiKey != null && !stripeApiKey.isBlank()) {
            Stripe.apiKey = stripeApiKey;
            log.info("Stripe SDK initialized.");
        } else {
            log.warn("Stripe API key not configured. Stripe operations will fail.");
        }
    }

    /**
     * Creates a new customer in Stripe.
     */
    public Customer createCustomer(String email, String name, String phone) throws StripeException {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .setName(name)
                .setPhone(phone)
                .build();
        
        return Customer.create(params);
    }

    /**
     * Creates a subscription with a 7-day free trial.
     */
    public Subscription createTrialSubscription(String customerId) throws StripeException {
        if (defaultPriceId == null || defaultPriceId.isBlank()) {
            log.warn("No default price ID configured for Stripe. Trial subscription cannot be fully created.");
            return null;
        }

        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(customerId)
                .addItem(
                        SubscriptionCreateParams.Item.builder()
                                .setPrice(defaultPriceId)
                                .build()
                )
                .setTrialPeriodDays(7L) // 7 days trial
                .build();

        return Subscription.create(params);
    }

    /**
     * Gets active subscriptions for a Stripe customer.
     */
    public List<Subscription> getSubscriptions(String customerId) throws StripeException {
        SubscriptionListParams params = SubscriptionListParams.builder()
                .setCustomer(customerId)
                .setLimit(10L)
                .build();

        return Subscription.list(params).getData();
    }

    /**
     * Creates a Stripe Billing Portal session so the customer can manage their subscription.
     */
    public String createBillingPortalSession(String customerId, String returnUrl) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setCustomer(customerId)
                .setReturnUrl(returnUrl)
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }

    /**
     * Finds a Stripe customer by email.
     */
    public Customer findCustomerByEmail(String email) throws StripeException {
        CustomerListParams params = CustomerListParams.builder()
                .setEmail(email)
                .setLimit(1L)
                .build();

        List<Customer> customers = Customer.list(params).getData();
        return customers.isEmpty() ? null : customers.get(0);
    }

    /**
     * Gets a Stripe Product by ID.
     */
    public Product getProduct(String productId) throws StripeException {
        return Product.retrieve(productId);
    }

    /**
     * Resolves a plan name from a Stripe product ID.
     */
    public String resolvePlanName(String productId) {
        if (productId == null) return "Sem plano";
        if (productId.equals(productIdStart)) return "neri start";
        if (productId.equals(productIdPro)) return "neri pro";
        if (productId.equals(productIdElite)) return "neri elite";
        return "Plano desconhecido";
    }

    public String getProductIdStart() { return productIdStart; }
    public String getProductIdPro() { return productIdPro; }
    public String getProductIdElite() { return productIdElite; }

    public void handleWebhookEvent(com.stripe.model.Event event) {
        if ("customer.subscription.created".equals(event.getType())
                || "customer.subscription.updated".equals(event.getType())
                || "customer.subscription.deleted".equals(event.getType())) {

            com.stripe.model.Subscription stripeSub = (com.stripe.model.Subscription) event.getDataObjectDeserializer()
                    .getObject().orElse(null);

            if (stripeSub != null) {
                syncSubscriptionWithDatabase(stripeSub);
            } else {
                log.warn("Falha ao desserializar objeto Subscription do evento webhook");
            }
        }
    }

    private void syncSubscriptionWithDatabase(com.stripe.model.Subscription stripeSub) {
        String customerId = stripeSub.getCustomer();
        Empresa empresa = empresaRepository.findByStripeCustomerId(customerId).orElse(null);

        if (empresa == null) {
            log.warn("Webhook recebido para customer {} que não existe no banco local", customerId);
            return;
        }

        AssinaturaEmpresa assinatura = assinaturaEmpresaRepository
                .findByStripeSubscriptionId(stripeSub.getId())
                .orElseGet(() -> {
                    AssinaturaEmpresa nova = new AssinaturaEmpresa();
                    nova.setEmpresa(empresa);
                    nova.setStripeSubscriptionId(stripeSub.getId());
                    return nova;
                });

        String productId = stripeSub.getItems().getData().get(0).getPrice().getProduct();
        assinatura.setStripeProductId(productId);

        PlanoAssinatura plano = planoAssinaturaRepository.findAll().stream()
                .filter(p -> p.getNome().equalsIgnoreCase(resolvePlanName(productId)))
                .findFirst()
                .orElseGet(() -> planoAssinaturaRepository.findById(1L).orElse(null));

        if (plano != null) {
            assinatura.setPlano(plano);
            assinatura.setValorMensal(plano.getPrecoMensal() != null ? plano.getPrecoMensal() : BigDecimal.ZERO);
        } else {
            log.error("Plano não encontrado para resolver assinatura. Assinatura não será sincronizada corretamente.");
            return;
        }

        LocalDate dataInicio = Instant.ofEpochSecond(stripeSub.getCurrentPeriodStart()).atZone(ZoneId.of("America/Sao_Paulo")).toLocalDate();
        LocalDate dataFim = Instant.ofEpochSecond(stripeSub.getCurrentPeriodEnd()).atZone(ZoneId.of("America/Sao_Paulo")).toLocalDate();
        
        assinatura.setDataInicio(dataInicio);
        assinatura.setDataFim(dataFim);

        if ("active".equals(stripeSub.getStatus()) || "trialing".equals(stripeSub.getStatus())) {
            assinatura.setStatus(StatusAssinatura.ATIVO);
        } else if ("canceled".equals(stripeSub.getStatus())) {
            assinatura.setStatus(StatusAssinatura.CANCELADO);
            assinatura.setDataCancelamento(LocalDate.now());
        } else if ("past_due".equals(stripeSub.getStatus()) || "unpaid".equals(stripeSub.getStatus())) {
            assinatura.setStatus(StatusAssinatura.INADIMPLENTE);
        } else {
            assinatura.setStatus(StatusAssinatura.INATIVO);
        }

        assinaturaEmpresaRepository.save(assinatura);
        log.info("Assinatura sincronizada com sucesso para empresa {} (Stripe Sub: {})", empresa.getId(), stripeSub.getId());
    }
}
