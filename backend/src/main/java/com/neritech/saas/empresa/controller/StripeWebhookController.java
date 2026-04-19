package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.service.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/assinatura/webhook")
@Tag(name = "Stripe Webhook", description = "Endpoints de integração para eventos do Stripe")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

    private final StripeService stripeService;

    @Value("${stripe.webhook.secret:}")
    private String endpointSecret;

    @PostMapping
    @Operation(summary = "Receber eventos", description = "Recebe e processa eventos do Stripe (Ex: assinatura atualizada)")
    public ResponseEntity<String> handleStripeEvent(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        log.info("Recebendo webhook do Stripe...");

        if (endpointSecret == null || endpointSecret.isBlank()) {
            log.error("Stripe Webhook Secret não está configurado!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook secret not configured");
        }

        Event event;
        try {
            // Verifica a assinatura
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            log.warn("Falha ao verificar assinatura do Webhook", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception e) {
            log.error("Falha ao processar a carga do Webhook", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payload format error");
        }

        // Passa o evento validado para o serviço tratar
        try {
            stripeService.handleWebhookEvent(event);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            log.error("Erro no processamento do evento", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing event");
        }
    }
}
