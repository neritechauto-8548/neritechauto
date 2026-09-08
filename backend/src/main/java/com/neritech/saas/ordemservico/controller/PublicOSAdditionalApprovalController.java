package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.OSAdditionalModels;
import com.neritech.saas.ordemservico.service.OSAdditionalRequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/public/v1/additional-approvals", "/api/public/v1/additional-approvals"})
public class PublicOSAdditionalApprovalController {

    private final OSAdditionalRequestService service;

    public PublicOSAdditionalApprovalController(OSAdditionalRequestService service) {
        this.service = service;
    }

    @GetMapping("/{token}")
    public ResponseEntity<OSAdditionalModels.PublicResponse> find(@PathVariable String token) {
        return ResponseEntity.ok(service.publicFind(token));
    }

    @PostMapping("/{token}/decision")
    public ResponseEntity<OSAdditionalModels.PublicResponse> decide(
            @PathVariable String token,
            @RequestBody @Valid OSAdditionalModels.PublicDecisionRequest request) {
        return ResponseEntity.ok(service.publicDecide(token, request));
    }
}
