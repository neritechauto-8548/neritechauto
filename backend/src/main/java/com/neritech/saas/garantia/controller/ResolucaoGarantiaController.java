package com.neritech.saas.garantia.controller;

import com.neritech.saas.garantia.dto.ResolucaoGarantiaRequest;
import com.neritech.saas.garantia.dto.ResolucaoGarantiaResponse;
import com.neritech.saas.garantia.service.ResolucaoGarantiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para ResolucaoGarantia
 */
@RestController
@RequestMapping("/v1/garantias/resolucoes")
@RequiredArgsConstructor
public class ResolucaoGarantiaController {

    private final ResolucaoGarantiaService service;

    @GetMapping("/reclamacao/{reclamacaoId}")
    public ResponseEntity<ResolucaoGarantiaResponse> findByReclamacaoId(@PathVariable Long reclamacaoId) {
        return ResponseEntity.ok(service.findByReclamacaoId(reclamacaoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResolucaoGarantiaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ResolucaoGarantiaResponse> create(@Valid @RequestBody ResolucaoGarantiaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResolucaoGarantiaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ResolucaoGarantiaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
