package com.neritech.saas.estoque.controller;

import com.neritech.saas.estoque.domain.enums.StatusItemInventario;
import com.neritech.saas.estoque.dto.ItemInventarioRequest;
import com.neritech.saas.estoque.dto.ItemInventarioResponse;
import com.neritech.saas.estoque.service.ItemInventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/itens-inventario")
@Tag(name = "Itens de InventÃ¡rio", description = "Gerenciamento de itens de inventÃ¡rio")
public class ItemInventarioController {

    private final ItemInventarioService service;

    public ItemInventarioController(ItemInventarioService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar novo item de inventÃ¡rio")
    public ResponseEntity<ItemInventarioResponse> create(@Valid @RequestBody ItemInventarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item por ID")
    public ResponseEntity<ItemInventarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/inventario/{inventarioId}")
    @Operation(summary = "Listar itens por inventÃ¡rio")
    public ResponseEntity<Page<ItemInventarioResponse>> findByInventario(
            @PathVariable Long inventarioId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByInventario(inventarioId, pageable));
    }

    @GetMapping("/inventario/{inventarioId}/status/{status}")
    @Operation(summary = "Listar itens por inventÃ¡rio e status")
    public ResponseEntity<Page<ItemInventarioResponse>> findByInventarioAndStatus(
            @PathVariable Long inventarioId,
            @PathVariable StatusItemInventario status,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByInventarioAndStatus(inventarioId, status, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item de inventÃ¡rio")
    public ResponseEntity<ItemInventarioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ItemInventarioRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir item de inventÃ¡rio")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
