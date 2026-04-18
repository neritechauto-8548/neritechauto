package com.neritech.saas.trial.controller;

import com.neritech.saas.trial.dto.TrialRegisterRequest;
import com.neritech.saas.trial.dto.TrialRegisterResponse;
import com.neritech.saas.trial.service.TrialService;
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

    @PostMapping("/register")
    @Operation(summary = "Registrar nova empresa para 7 dias de teste")
    public ResponseEntity<TrialRegisterResponse> register(@Valid @RequestBody TrialRegisterRequest request) {
        try {
            TrialRegisterResponse response = trialService.registerTrial(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    TrialRegisterResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}
