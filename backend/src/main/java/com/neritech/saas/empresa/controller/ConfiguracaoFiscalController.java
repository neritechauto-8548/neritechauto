package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.dto.ConfiguracaoFiscalRequest;
import com.neritech.saas.empresa.dto.ConfiguracaoFiscalResponse;
import com.neritech.saas.empresa.service.ConfiguracaoFiscalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/configuracoes-fiscais")
public class ConfiguracaoFiscalController {

    private final ConfiguracaoFiscalService service;

    public ConfiguracaoFiscalController(ConfiguracaoFiscalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ConfiguracaoFiscalResponse> create(@Valid @RequestBody ConfiguracaoFiscalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracaoFiscalResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<ConfiguracaoFiscalResponse> findByEmpresaId(@PathVariable Long empresaId) {
        return ResponseEntity.ok(service.findByEmpresaId(empresaId));
    }

    @GetMapping
    public ResponseEntity<Page<ConfiguracaoFiscalResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracaoFiscalResponse> update(@PathVariable Long id,
            @Valid @RequestBody ConfiguracaoFiscalRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
