package com.neritech.saas.garantia.controller;

import com.neritech.saas.garantia.dto.TipoGarantiaRequest;
import com.neritech.saas.garantia.dto.TipoGarantiaResponse;
import com.neritech.saas.garantia.service.TipoGarantiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para TipoGarantia
 */
@RestController
@RequestMapping("/v1/garantias/tipos")
@RequiredArgsConstructor
public class TipoGarantiaController {

    private final TipoGarantiaService service;

    @GetMapping
    public ResponseEntity<Page<TipoGarantiaResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<TipoGarantiaResponse>> findAtivos(@RequestParam Long empresaId) {
        return ResponseEntity.ok(service.findAtivos(empresaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoGarantiaResponse> findById(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        return ResponseEntity.ok(service.findById(id, empresaId));
    }

    @PostMapping
    public ResponseEntity<TipoGarantiaResponse> create(
            @RequestParam Long empresaId,
            @Valid @RequestBody TipoGarantiaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(empresaId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoGarantiaResponse> update(
            @PathVariable Long id,
            @RequestParam Long empresaId,
            @Valid @RequestBody TipoGarantiaRequest request) {
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
