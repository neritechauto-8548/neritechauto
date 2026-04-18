package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.FormaPagamentoRequest;
import com.neritech.saas.financeiro.dto.FormaPagamentoResponse;
import com.neritech.saas.financeiro.service.FormaPagamentoService;
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
@RequestMapping("/v1/financeiro/formas-pagamento")
@RequiredArgsConstructor
@Tag(name = "Formas de Pagamento", description = "GestÃ£o de formas de pagamento")
public class FormaPagamentoController {

    private final FormaPagamentoService service;

    @GetMapping
    @Operation(summary = "Listar formas de pagamento")
    public ResponseEntity<Page<FormaPagamentoResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar forma de pagamento por ID")
    public ResponseEntity<FormaPagamentoResponse> findById(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        return ResponseEntity.ok(service.findById(id, empresaId));
    }

    @PostMapping
    @Operation(summary = "Criar forma de pagamento")
    public ResponseEntity<FormaPagamentoResponse> create(
            @RequestParam Long empresaId,
            @Valid @RequestBody FormaPagamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(empresaId, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar forma de pagamento")
    public ResponseEntity<FormaPagamentoResponse> update(
            @PathVariable Long id,
            @RequestParam Long empresaId,
            @Valid @RequestBody FormaPagamentoRequest request) {
        return ResponseEntity.ok(service.update(id, empresaId, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir forma de pagamento")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        service.delete(id, empresaId);
        return ResponseEntity.noContent().build();
    }
}
