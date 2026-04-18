package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.LancamentoContabilRequest;
import com.neritech.saas.financeiro.dto.LancamentoContabilResponse;
import com.neritech.saas.financeiro.service.LancamentoContabilService;
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
@RequestMapping("/v1/financeiro/lancamentos-contabeis")
@RequiredArgsConstructor
@Tag(name = "LanÃ§amentos ContÃ¡beis", description = "GestÃ£o de lanÃ§amentos contÃ¡beis")
public class LancamentoContabilController {

    private final LancamentoContabilService service;

    @GetMapping
    @Operation(summary = "Listar lanÃ§amentos contÃ¡beis")
    public ResponseEntity<Page<LancamentoContabilResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar lanÃ§amento por ID")
    public ResponseEntity<LancamentoContabilResponse> findById(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        return ResponseEntity.ok(service.findById(id, empresaId));
    }

    @PostMapping
    @Operation(summary = "Criar lanÃ§amento")
    public ResponseEntity<LancamentoContabilResponse> create(
            @Valid @RequestBody LancamentoContabilRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar lanÃ§amento")
    public ResponseEntity<LancamentoContabilResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody LancamentoContabilRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir lanÃ§amento")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        service.delete(id, empresaId);
        return ResponseEntity.noContent().build();
    }
}
