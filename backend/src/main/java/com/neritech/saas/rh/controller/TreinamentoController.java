package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.service.TreinamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/rh/treinamentos")
@RequiredArgsConstructor
@Tag(name = "Treinamentos", description = "GestÃ£o de treinamentos")
public class TreinamentoController {
    private final TreinamentoService service;

    @GetMapping
    @Operation(summary = "Listar treinamentos")
    public ResponseEntity<Page<TreinamentoResponse>> findAll(@RequestParam Long empresaId, Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar treinamento por ID")
    public ResponseEntity<TreinamentoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar treinamento")
    public ResponseEntity<TreinamentoResponse> create(@Valid @RequestBody TreinamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar treinamento")
    public ResponseEntity<TreinamentoResponse> update(@PathVariable Long id,
            @Valid @RequestBody TreinamentoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir treinamento")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
