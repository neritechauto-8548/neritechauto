package com.neritech.saas.produtoServico.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('PS_INC_FORN') or hasAuthority('FORNECEDOR_CRIAR')")
    @PostMapping
    @Operation(summary = "Criar novo fornecedor")
    public ResponseEntity<FornecedorResponse> create(@Valid @RequestBody FornecedorRequest request) {
        FornecedorResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('PS_LISTAR_FORN') or hasAuthority('FORNECEDOR_VISUALIZAR') or hasAuthority('PS_INC_FORN')")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar fornecedor por ID")
    public ResponseEntity<FornecedorResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('PS_LISTAR_FORN') or hasAuthority('FORNECEDOR_VISUALIZAR') or hasAuthority('PS_INC_FORN')")
    @GetMapping
    @Operation(summary = "Listar fornecedores por empresa (paginado)")
    public ResponseEntity<Page<FornecedorResponse>> findAll(
            @RequestParam Long empresaId,
            @RequestParam(required = false) String search,
            Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(service.search(empresaId, search, pageable));
        }
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('PS_EDIT_FORN') or hasAuthority('FORNECEDOR_EDITAR')")
    @GetMapping("/{id}/historico")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('PS_LISTAR_FORN') or hasAuthority('FORNECEDOR_VISUALIZAR')")
    @Operation(summary = "Histórico versionado do fornecedor")
    public ResponseEntity<Page<com.neritech.saas.produtoServico.service.FornecedorHistoricoService.Alteracao>>
            historico(@PathVariable Long id,
                     @RequestParam(defaultValue = "0") int page,
                     @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.historico(id, page, size));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar fornecedor")
    public ResponseEntity<FornecedorResponse> update(
            @PathVariable Long id,
            @RequestHeader(value = "If-Match", required = false) Integer expectedVersion,
            @Valid @RequestBody FornecedorRequest request) {
        return ResponseEntity.ok(service.update(id, request, expectedVersion));
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('PS_EDIT_FORN') or hasAuthority('FORNECEDOR_EDITAR')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Inativar fornecedor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
