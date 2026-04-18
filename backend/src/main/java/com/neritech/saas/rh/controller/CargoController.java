package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.CargoRequest;
import com.neritech.saas.rh.dto.CargoResponse;
import com.neritech.saas.rh.service.CargoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/rh/cargos")
@RequiredArgsConstructor
@Tag(name = "Cargos", description = "GestÃ£o de cargos")
public class CargoController {
    private final CargoService service;

    @GetMapping
    @Operation(summary = "Listar cargos")
    public ResponseEntity<Page<CargoResponse>> findAll(@RequestParam Long empresaId, Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cargo por ID")
    public ResponseEntity<CargoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar cargo")
    public ResponseEntity<CargoResponse> create(@Valid @RequestBody CargoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cargo")
    public ResponseEntity<CargoResponse> update(@PathVariable Long id, @Valid @RequestBody CargoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir cargo")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
