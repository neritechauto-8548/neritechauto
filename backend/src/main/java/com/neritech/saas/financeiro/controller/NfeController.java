package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.NfeRequest;
import com.neritech.saas.financeiro.dto.NfeResponse;
import com.neritech.saas.financeiro.service.NfeService;
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
@RequestMapping("/v1/financeiro/nfe")
@RequiredArgsConstructor
@Tag(name = "Nota Fiscal EletrÃ´nica", description = "GestÃ£o de NFe")
public class NfeController {

    private final NfeService service;

    @GetMapping
    @Operation(summary = "Listar NFes")
    public ResponseEntity<Page<NfeResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar NFe por ID")
    public ResponseEntity<NfeResponse> findById(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        return ResponseEntity.ok(service.findById(id, empresaId));
    }

    @PostMapping
    @Operation(summary = "Criar NFe")
    public ResponseEntity<NfeResponse> create(
            @RequestParam Long empresaId,
            @Valid @RequestBody NfeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(empresaId, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar NFe")
    public ResponseEntity<NfeResponse> update(
            @PathVariable Long id,
            @RequestParam Long empresaId,
            @Valid @RequestBody NfeRequest request) {
        return ResponseEntity.ok(service.update(id, empresaId, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir NFe")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        service.delete(id, empresaId);
        return ResponseEntity.noContent().build();
    }
}
