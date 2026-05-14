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

    public boolean isConfigured() {
        return stripeApiKey != null && !stripeApiKey.isBlank();
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
        // Mapeando Pro para Nível 1
        if (productId.equals(productIdPro)) return "Pro";
        // Mapeando Elite para Nível 2
        if (productId.equals(productIdElite)) return "Elite";
        return "Plano desconhecido";
    }

    public String getProductIdPro() { return productIdPro; }
    public String getProductIdElite() { return productIdElite; }

    public void handleWebhookEvent(com.stripe.model.Event event) {
        String type = event.getType();
        log.info("Recebido evento Stripe: {}", type);

        if (type.startsWith("customer.subscription.")) {
            com.stripe.model.Subscription stripeSub = (com.stripe.model.Subscription) event.getDataObjectDeserializer()
                    .getObject().orElse(null);
            if (stripeSub != null) {
                syncSubscriptionWithDatabase(stripeSub);
            }
        } else if (type.startsWith("invoice.")) {
            com.stripe.model.Invoice invoice = (com.stripe.model.Invoice) event.getDataObjectDeserializer()
                    .getObject().orElse(null);
            if (invoice != null && invoice.getSubscription() != null) {
                try {
                    com.stripe.model.Subscription sub = com.stripe.model.Subscription.retrieve(invoice.getSubscription());
                    syncSubscriptionWithDatabase(sub);
                } catch (StripeException e) {
                    log.error("Erro ao buscar assinatura {} após evento {}", invoice.getSubscription(), type, e);
                }
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

        assinatura.setStripeCustomerId(customerId);
        String productId = stripeSub.getItems().getData().get(0).getPrice().getProduct();
        assinatura.setStripeProductId(productId);

        PlanoAssinatura plano = planoAssinaturaRepository.findAll().stream()
                .filter(p -> p.getNome().equalsIgnoreCase(resolvePlanName(productId)))
                .findFirst()
                .orElseGet(() -> planoAssinaturaRepository.findById(1L).orElse(null));

        if (plano != null) {
            assinatura.setPlano(plano);
            assinatura.setValorMensal(plano.getPrecoMensal() != null ? plano.getPrecoMensal() : BigDecimal.ZERO);
        }

        java.time.LocalDateTime dataInicio = java.time.Instant.ofEpochSecond(stripeSub.getCurrentPeriodStart()).atZone(java.time.ZoneId.of("America/Sao_Paulo")).toLocalDateTime();
        java.time.LocalDateTime dataFim = java.time.Instant.ofEpochSecond(stripeSub.getCurrentPeriodEnd()).atZone(java.time.ZoneId.of("America/Sao_Paulo")).toLocalDateTime();
        
        assinatura.setDataInicio(dataInicio.toLocalDate());
        assinatura.setDataFim(dataFim.toLocalDate());
        assinatura.setSubscriptionEndsAt(dataFim);

        if (stripeSub.getTrialEnd() != null) {
            assinatura.setTrialEndsAt(java.time.Instant.ofEpochSecond(stripeSub.getTrialEnd()).atZone(java.time.ZoneId.of("America/Sao_Paulo")).toLocalDateTime());
        }

        String stripeStatus = stripeSub.getStatus();
        if ("active".equals(stripeStatus)) {
            assinatura.setStatus(StatusAssinatura.ATIVO);
        } else if ("trialing".equals(stripeStatus)) {
            assinatura.setStatus(StatusAssinatura.TESTE);
        } else if ("canceled".equals(stripeStatus)) {
            assinatura.setStatus(StatusAssinatura.CANCELADO);
            assinatura.setDataCancelamento(LocalDate.now());
        } else if ("past_due".equals(stripeStatus)) {
            assinatura.setStatus(StatusAssinatura.ATRASADO);
            // Grace period of 3 days
            assinatura.setGracePeriodEndsAt(java.time.LocalDateTime.now().plusDays(3));
        } else if ("unpaid".equals(stripeStatus)) {
            assinatura.setStatus(StatusAssinatura.SUSPENSO);
        } else if ("incomplete".equals(stripeStatus)) {
            assinatura.setStatus(StatusAssinatura.INCOMPLETO);
        } else {
            assinatura.setStatus(StatusAssinatura.SUSPENSO);
        }

        assinaturaEmpresaRepository.save(assinatura);
        log.info("Assinatura sincronizada com sucesso para empresa {} (Stripe Sub: {})", empresa.getId(), stripeSub.getId());
    }
}
