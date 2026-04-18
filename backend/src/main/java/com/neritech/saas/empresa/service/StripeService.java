package com.neritech.saas.empresa.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StripeService {

    @Value("${stripe.api.key:}")
    private String stripeApiKey;

    // Optional: If there's a specific default plan price ID for the trial
    @Value("${stripe.default.price.id:}")
    private String defaultPriceId;

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
        // We need a price ID to create a subscription. 
        // If it's not configured, we might just skip subscription creation or throw an error.
        if (defaultPriceId == null || defaultPriceId.isBlank()) {
            log.warn("No default price ID configured for Stripe. Trial subscription cannot be fully created.");
            // For the sake of the flow, if no price ID is present, we return null or throw. 
            // In a real scenario, we must have a product/price.
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
}
