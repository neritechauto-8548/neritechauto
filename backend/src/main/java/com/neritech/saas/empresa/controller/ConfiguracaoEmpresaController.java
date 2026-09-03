package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.dto.ConfiguracaoEmpresaRequest;
import com.neritech.saas.empresa.dto.ConfiguracaoEmpresaResponse;
import com.neritech.saas.empresa.service.ConfiguracaoEmpresaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/configuracoes-empresa")
@PreAuthorize("hasAuthority('GERAL_CONFIG_SISTEMA')")
public class ConfiguracaoEmpresaController {

    private final ConfiguracaoEmpresaService configuracaoEmpresaService;

    public ConfiguracaoEmpresaController(ConfiguracaoEmpresaService configuracaoEmpresaService) {
        this.configuracaoEmpresaService = configuracaoEmpresaService;
    }

    @PostMapping
    public ResponseEntity<ConfiguracaoEmpresaResponse> create(@Valid @RequestBody ConfiguracaoEmpresaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(configuracaoEmpresaService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracaoEmpresaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(configuracaoEmpresaService.findById(id));
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<ConfiguracaoEmpresaResponse> findByEmpresaId(@PathVariable Long empresaId) {
        return ResponseEntity.ok(configuracaoEmpresaService.findByEmpresaId(empresaId));
    }

    @GetMapping
    public ResponseEntity<Page<ConfiguracaoEmpresaResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(configuracaoEmpresaService.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracaoEmpresaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ConfiguracaoEmpresaRequest request) {
        return ResponseEntity.ok(configuracaoEmpresaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        configuracaoEmpresaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
