package com.neritech.saas.garantia.controller;

import com.neritech.saas.garantia.dto.GarantiaRequest;
import com.neritech.saas.garantia.dto.GarantiaResponse;
import com.neritech.saas.garantia.service.GarantiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para Garantia
 */
@RestController
@RequestMapping("/v1/garantias")
@RequiredArgsConstructor
public class GarantiaController {

    private final GarantiaService service;

    @GetMapping
    public ResponseEntity<Page<GarantiaResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarantiaResponse> findById(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        return ResponseEntity.ok(service.findById(id, empresaId));
    }

    @PostMapping
    public ResponseEntity<GarantiaResponse> create(
            @RequestParam Long empresaId,
            @Valid @RequestBody GarantiaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(empresaId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GarantiaResponse> update(
            @PathVariable Long id,
            @RequestParam Long empresaId,
            @Valid @RequestBody GarantiaRequest request) {
        return ResponseEntity.ok(service.update(id, empresaId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        service.delete(id, empresaId);
        return ResponseEntity.noContent().build();
    }
}
