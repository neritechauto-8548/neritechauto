package com.neritech.saas.garantia.controller;

import com.neritech.saas.garantia.dto.ItemGarantiaRequest;
import com.neritech.saas.garantia.dto.ItemGarantiaResponse;
import com.neritech.saas.garantia.service.ItemGarantiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para ItemGarantia
 */
@RestController
@RequestMapping("/v1/garantias/itens")
@RequiredArgsConstructor
public class ItemGarantiaController {

    private final ItemGarantiaService service;

    @GetMapping("/garantia/{garantiaId}")
    public ResponseEntity<List<ItemGarantiaResponse>> findByGarantiaId(@PathVariable Long garantiaId) {
        return ResponseEntity.ok(service.findByGarantiaId(garantiaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemGarantiaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ItemGarantiaResponse> create(@Valid @RequestBody ItemGarantiaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemGarantiaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ItemGarantiaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
