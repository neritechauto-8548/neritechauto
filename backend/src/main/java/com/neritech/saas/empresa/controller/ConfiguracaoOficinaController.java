package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.dto.ConfiguracaoOficinaRequest;
import com.neritech.saas.empresa.dto.ConfiguracaoOficinaResponse;
import com.neritech.saas.empresa.service.ConfiguracaoOficinaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/configuracoes-oficina")
public class ConfiguracaoOficinaController {

    private final ConfiguracaoOficinaService service;

    public ConfiguracaoOficinaController(ConfiguracaoOficinaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ConfiguracaoOficinaResponse> create(@Valid @RequestBody ConfiguracaoOficinaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracaoOficinaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<ConfiguracaoOficinaResponse> findByEmpresaId(@PathVariable Long empresaId) {
        return ResponseEntity.ok(service.findByEmpresaId(empresaId));
    }

    @GetMapping
    public ResponseEntity<Page<ConfiguracaoOficinaResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracaoOficinaResponse> update(@PathVariable Long id,
            @Valid @RequestBody ConfiguracaoOficinaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
