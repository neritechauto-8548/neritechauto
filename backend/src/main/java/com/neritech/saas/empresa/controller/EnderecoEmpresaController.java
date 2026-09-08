package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.dto.EnderecoEmpresaRequest;
import com.neritech.saas.empresa.dto.EnderecoEmpresaResponse;
import com.neritech.saas.empresa.service.EnderecoEmpresaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/enderecos-empresa")
@PreAuthorize("hasAuthority('GERAL_CONFIG_SISTEMA')")
public class EnderecoEmpresaController {

    private final EnderecoEmpresaService service;

    public EnderecoEmpresaController(EnderecoEmpresaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EnderecoEmpresaResponse> create(@Valid @RequestBody EnderecoEmpresaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoEmpresaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<EnderecoEmpresaResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<EnderecoEmpresaResponse>> findByEmpresaId(@PathVariable Long empresaId) {
        return ResponseEntity.ok(service.findByEmpresaId(empresaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoEmpresaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EnderecoEmpresaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
