package com.neritech.saas.produtoServico.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.neritech.saas.produtoServico.dto.FornecedorRequest;
import com.neritech.saas.produtoServico.dto.FornecedorResponse;
import com.neritech.saas.produtoServico.service.FornecedorService;

@RestController
@RequestMapping("/v1/fornecedores")
@Tag(name = "Fornecedores", description = "Gerenciamento de fornecedores")
public class FornecedorController {

    private final FornecedorService service;

    public FornecedorController(FornecedorService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('GERAL_CONFIG_SISTEMA')")
    @Operation(summary = "Criar novo fornecedor")
    public ResponseEntity<FornecedorResponse> create(@Valid @RequestBody FornecedorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Buscar fornecedor por ID")
    public ResponseEntity<FornecedorResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Listar fornecedores da empresa autenticada")
    public ResponseEntity<Page<FornecedorResponse>> findAll(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(service.search(search, pageable));
        }
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('GERAL_CONFIG_SISTEMA')")
    @Operation(summary = "Atualizar fornecedor")
    public ResponseEntity<FornecedorResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody FornecedorRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('GERAL_CONFIG_SISTEMA')")
    @Operation(summary = "Excluir fornecedor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
