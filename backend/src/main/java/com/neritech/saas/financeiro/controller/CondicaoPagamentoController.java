package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.CondicaoPagamentoRequest;
import com.neritech.saas.financeiro.dto.CondicaoPagamentoResponse;
import com.neritech.saas.financeiro.service.CondicaoPagamentoService;
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
@RequestMapping("/v1/financeiro/condicoes-pagamento")
@RequiredArgsConstructor
@Tag(name = "CondiÃ§Ãµes de Pagamento", description = "GestÃ£o de condiÃ§Ãµes de pagamento")
public class CondicaoPagamentoController {

    private final CondicaoPagamentoService service;

    @GetMapping
    @Operation(summary = "Listar condiÃ§Ãµes de pagamento")
    public ResponseEntity<Page<CondicaoPagamentoResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar condiÃ§Ã£o de pagamento por ID")
    public ResponseEntity<CondicaoPagamentoResponse> findById(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        return ResponseEntity.ok(service.findById(id, empresaId));
    }

    @PostMapping
    @Operation(summary = "Criar condiÃ§Ã£o de pagamento")
    public ResponseEntity<CondicaoPagamentoResponse> create(
            @RequestParam Long empresaId,
            @Valid @RequestBody CondicaoPagamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(empresaId, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar condiÃ§Ã£o de pagamento")
    public ResponseEntity<CondicaoPagamentoResponse> update(
            @PathVariable Long id,
            @RequestParam Long empresaId,
            @Valid @RequestBody CondicaoPagamentoRequest request) {
        return ResponseEntity.ok(service.update(id, empresaId, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir condiÃ§Ã£o de pagamento")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        service.delete(id, empresaId);
        return ResponseEntity.noContent().build();
    }
}
