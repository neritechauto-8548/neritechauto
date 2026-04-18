package com.neritech.saas.garantia.controller;

import com.neritech.saas.garantia.dto.ReclamacaoGarantiaRequest;
import com.neritech.saas.garantia.dto.ReclamacaoGarantiaResponse;
import com.neritech.saas.garantia.service.ReclamacaoGarantiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para ReclamacaoGarantia
 */
@RestController
@RequestMapping("/v1/garantias/reclamacoes")
@RequiredArgsConstructor
public class ReclamacaoGarantiaController {

    private final ReclamacaoGarantiaService service;

    @GetMapping
    public ResponseEntity<Page<ReclamacaoGarantiaResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReclamacaoGarantiaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReclamacaoGarantiaResponse> create(@Valid @RequestBody ReclamacaoGarantiaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReclamacaoGarantiaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ReclamacaoGarantiaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
