package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.dto.PlanoAssinaturaRequest;
import com.neritech.saas.empresa.dto.PlanoAssinaturaResponse;
import com.neritech.saas.empresa.service.PlanoAssinaturaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/planos-assinatura")
public class PlanoAssinaturaController {

    private final PlanoAssinaturaService service;

    public PlanoAssinaturaController(PlanoAssinaturaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PlanoAssinaturaResponse> create(@Valid @RequestBody PlanoAssinaturaRequest request) {
        PlanoAssinaturaResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoAssinaturaResponse> findById(@PathVariable Long id) {
        PlanoAssinaturaResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PlanoAssinaturaResponse>> findAll(Pageable pageable) {
        Page<PlanoAssinaturaResponse> response = service.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<PlanoAssinaturaResponse>> findAtivos() {
        List<PlanoAssinaturaResponse> response = service.findAtivos();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanoAssinaturaResponse> update(@PathVariable Long id,
            @Valid @RequestBody PlanoAssinaturaRequest request) {
        PlanoAssinaturaResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
