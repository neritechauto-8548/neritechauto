package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.ImpostoRequest;
import com.neritech.saas.financeiro.dto.ImpostoResponse;
import com.neritech.saas.financeiro.service.ImpostoService;
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
@RequestMapping("/v1/financeiro/impostos")
@RequiredArgsConstructor
@Tag(name = "Impostos", description = "GestÃ£o de impostos")
public class ImpostoController {

    private final ImpostoService service;

    @GetMapping
    @Operation(summary = "Listar impostos")
    public ResponseEntity<Page<ImpostoResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar imposto por ID")
    public ResponseEntity<ImpostoResponse> findById(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar imposto")
    public ResponseEntity<ImpostoResponse> create(
            @Valid @RequestBody ImpostoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar imposto")
    public ResponseEntity<ImpostoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ImpostoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir imposto")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        service.delete(id, empresaId);
        return ResponseEntity.noContent().build();
    }
}
