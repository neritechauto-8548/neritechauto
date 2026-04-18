package com.neritech.saas.estoque.controller;

import com.neritech.saas.estoque.domain.enums.StatusInventario;
import com.neritech.saas.estoque.dto.InventarioRequest;
import com.neritech.saas.estoque.dto.InventarioResponse;
import com.neritech.saas.estoque.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/inventarios")
@Tag(name = "InventÃ¡rios", description = "Gerenciamento de inventÃ¡rios")
public class InventarioController {

    private final InventarioService service;

    public InventarioController(InventarioService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar novo inventÃ¡rio")
    public ResponseEntity<InventarioResponse> create(@Valid @RequestBody InventarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar inventÃ¡rio por ID")
    public ResponseEntity<InventarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar inventÃ¡rios por empresa")
    public ResponseEntity<Page<InventarioResponse>> findByEmpresa(
            @PathVariable Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByEmpresa(empresaId, pageable));
    }

    @GetMapping("/empresa/{empresaId}/status/{status}")
    @Operation(summary = "Listar inventÃ¡rios por status")
    public ResponseEntity<Page<InventarioResponse>> findByEmpresaAndStatus(
            @PathVariable Long empresaId,
            @PathVariable StatusInventario status,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByEmpresaAndStatus(empresaId, status, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar inventÃ¡rio")
    public ResponseEntity<InventarioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody InventarioRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir inventÃ¡rio")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
