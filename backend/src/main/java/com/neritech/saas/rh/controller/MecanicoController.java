package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.service.MecanicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/rh/mecanicos")
@RequiredArgsConstructor
@Tag(name = "MecÃ¢nicos", description = "GestÃ£o de mecÃ¢nicos")
public class MecanicoController {
    private final MecanicoService service;

    @GetMapping
    @Operation(summary = "Listar mecÃ¢nicos")
    public ResponseEntity<Page<MecanicoResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar mecÃ¢nico por ID")
    public ResponseEntity<MecanicoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar mecÃ¢nico")
    public ResponseEntity<MecanicoResponse> create(@Valid @RequestBody MecanicoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar mecÃ¢nico")
    public ResponseEntity<MecanicoResponse> update(@PathVariable Long id, @Valid @RequestBody MecanicoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir mecÃ¢nico")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
