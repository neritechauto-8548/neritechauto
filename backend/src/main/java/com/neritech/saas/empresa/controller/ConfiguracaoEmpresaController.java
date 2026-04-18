package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.dto.ConfiguracaoEmpresaRequest;
import com.neritech.saas.empresa.dto.ConfiguracaoEmpresaResponse;
import com.neritech.saas.empresa.service.ConfiguracaoEmpresaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/configuracoes-empresa")
public class ConfiguracaoEmpresaController {

    private final ConfiguracaoEmpresaService configuracaoEmpresaService;

    public ConfiguracaoEmpresaController(ConfiguracaoEmpresaService configuracaoEmpresaService) {
        this.configuracaoEmpresaService = configuracaoEmpresaService;
    }

    @PostMapping
    public ResponseEntity<ConfiguracaoEmpresaResponse> create(@Valid @RequestBody ConfiguracaoEmpresaRequest request) {
        ConfiguracaoEmpresaResponse response = configuracaoEmpresaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracaoEmpresaResponse> findById(@PathVariable Long id) {
        ConfiguracaoEmpresaResponse response = configuracaoEmpresaService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<ConfiguracaoEmpresaResponse> findByEmpresaId(@PathVariable Long empresaId) {
        ConfiguracaoEmpresaResponse response = configuracaoEmpresaService.findByEmpresaId(empresaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ConfiguracaoEmpresaResponse>> findAll(Pageable pageable) {
        Page<ConfiguracaoEmpresaResponse> response = configuracaoEmpresaService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracaoEmpresaResponse> update(@PathVariable Long id,
            @Valid @RequestBody ConfiguracaoEmpresaRequest request) {
        ConfiguracaoEmpresaResponse response = configuracaoEmpresaService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        configuracaoEmpresaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
