package com.neritech.saas.trial.controller;

import com.neritech.saas.trial.dto.TrialRegisterRequest;
import com.neritech.saas.trial.dto.TrialRegisterResponse;
import com.neritech.saas.trial.service.TrialService;
import com.neritech.saas.trial.service.TrialRequestThrottle;
import com.neritech.saas.empresa.dto.PlanoAssinaturaResponse;
import com.neritech.saas.empresa.service.PlanoAssinaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/trial")
@RequiredArgsConstructor
@Tag(name = "Trial", description = "Endpoints para registro de teste gratuito")
public class TrialController {

    private final TrialService trialService;
    private final PlanoAssinaturaService planoAssinaturaService;
    private final TrialRequestThrottle throttle;

    @org.springframework.beans.factory.annotation.Value("${trial.legal.version:}")
    private String legalVersion;
    @org.springframework.beans.factory.annotation.Value("${trial.legal.terms-url:}")
    private String termsUrl;
    @org.springframework.beans.factory.annotation.Value("${trial.legal.privacy-url:}")
    private String privacyUrl;

    public record TrialStatus(boolean available, String legalVersion, String termsUrl, String privacyUrl) {}

    @GetMapping("/status")
    public TrialStatus status() {
        boolean available = !legalVersion.isBlank() && termsUrl.startsWith("https://") && privacyUrl.startsWith("https://");
        return new TrialStatus(available, available ? legalVersion : null,
                available ? termsUrl : null, available ? privacyUrl : null);
    }

    @GetMapping("/plans")
    @Operation(summary = "Listar os dois planos comerciais públicos")
    public java.util.List<PlanoAssinaturaResponse> plans() {
        return planoAssinaturaService.findPublicPlans();
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nova empresa para 14 dias de teste")
    public ResponseEntity<TrialRegisterResponse> register(@Valid @RequestBody TrialRegisterRequest request,
            jakarta.servlet.http.HttpServletRequest servletRequest) {
        if (!status().available()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE, "Cadastro ainda não disponível.");
        }
        throttle.checkAndRecord(request.getEmail(), servletRequest.getRemoteAddr());
        try {
            TrialRegisterResponse response = trialService.registerTrial(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    TrialRegisterResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
}
