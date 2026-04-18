package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.PagamentoRequest;
import com.neritech.saas.financeiro.dto.PagamentoResponse;
import com.neritech.saas.financeiro.service.PagamentoService;
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
@RequestMapping("/v1/financeiro/pagamentos")
@RequiredArgsConstructor
@Tag(name = "Pagamentos", description = "GestÃ£o de pagamentos")
public class PagamentoController {

    private final PagamentoService service;

    @GetMapping
    @Operation(summary = "Listar pagamentos")
    public ResponseEntity<Page<PagamentoResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/fatura/{faturaId}")
    @Operation(summary = "Listar pagamentos por fatura")
    public ResponseEntity<Page<PagamentoResponse>> findByFatura(
            @PathVariable Long faturaId,
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByFatura(empresaId, faturaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pagamento por ID")
    public ResponseEntity<PagamentoResponse> findById(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        return ResponseEntity.ok(service.findById(id, empresaId));
    }

    @PostMapping
    @Operation(summary = "Criar pagamento")
    public ResponseEntity<PagamentoResponse> create(
            @RequestParam Long empresaId,
            @Valid @RequestBody PagamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(empresaId, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pagamento")
    public ResponseEntity<PagamentoResponse> update(
            @PathVariable Long id,
            @RequestParam Long empresaId,
            @Valid @RequestBody PagamentoRequest request) {
        return ResponseEntity.ok(service.update(id, empresaId, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pagamento")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        service.delete(id, empresaId);
        return ResponseEntity.noContent().build();
    }
}
