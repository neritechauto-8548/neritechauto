package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.CentroCustoRequest;
import com.neritech.saas.financeiro.dto.CentroCustoResponse;
import com.neritech.saas.financeiro.service.CentroCustoService;
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
@RequestMapping("/v1/financeiro/centros-custo")
@RequiredArgsConstructor
@Tag(name = "Centros de Custo", description = "GestÃ£o de centros de custo")
public class CentroCustoController {

    private final CentroCustoService service;

    @GetMapping
    @Operation(summary = "Listar centros de custo")
    public ResponseEntity<Page<CentroCustoResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar centro de custo por ID")
    public ResponseEntity<CentroCustoResponse> findById(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar centro de custo")
    public ResponseEntity<CentroCustoResponse> create(
            @RequestParam Long empresaId,
            @Valid @RequestBody CentroCustoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(empresaId, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar centro de custo")
    public ResponseEntity<CentroCustoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CentroCustoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir centro de custo")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
