package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.dto.AssinaturaEmpresaRequest;
import com.neritech.saas.empresa.dto.AssinaturaEmpresaResponse;
import com.neritech.saas.empresa.service.AssinaturaEmpresaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/assinaturas-empresas")
public class AssinaturaEmpresaController {

    private final AssinaturaEmpresaService service;

    public AssinaturaEmpresaController(AssinaturaEmpresaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AssinaturaEmpresaResponse> create(@Valid @RequestBody AssinaturaEmpresaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssinaturaEmpresaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<AssinaturaEmpresaResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<AssinaturaEmpresaResponse>> findByEmpresaId(@PathVariable Long empresaId) {
        return ResponseEntity.ok(service.findByEmpresaId(empresaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssinaturaEmpresaResponse> update(@PathVariable Long id,
            @Valid @RequestBody AssinaturaEmpresaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
