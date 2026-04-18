package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.ConciliacaoBancariaRequest;
import com.neritech.saas.financeiro.dto.ConciliacaoBancariaResponse;
import com.neritech.saas.financeiro.service.ConciliacaoBancariaService;
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
@RequestMapping("/v1/financeiro/conciliacoes")
@RequiredArgsConstructor
@Tag(name = "ConciliaÃ§Ã£o BancÃ¡ria", description = "GestÃ£o de conciliaÃ§Ãµes bancÃ¡rias")
public class ConciliacaoBancariaController {

    private final ConciliacaoBancariaService service;

    @GetMapping("/conta/{contaBancariaId}")
    @Operation(summary = "Listar conciliaÃ§Ãµes por conta bancÃ¡ria")
    public ResponseEntity<Page<ConciliacaoBancariaResponse>> findByContaBancariaId(
            @PathVariable Long contaBancariaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByContaBancariaId(contaBancariaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar conciliaÃ§Ã£o por ID")
    public ResponseEntity<ConciliacaoBancariaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar conciliaÃ§Ã£o")
    public ResponseEntity<ConciliacaoBancariaResponse> create(
            @RequestParam Long empresaId,
            @Valid @RequestBody ConciliacaoBancariaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(empresaId, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar conciliaÃ§Ã£o")
    public ResponseEntity<ConciliacaoBancariaResponse> update(
            @PathVariable Long id,
            @RequestParam Long empresaId,
            @Valid @RequestBody ConciliacaoBancariaRequest request) {
        return ResponseEntity.ok(service.update(id, empresaId, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir conciliaÃ§Ã£o")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        service.delete(id, empresaId);
        return ResponseEntity.noContent().build();
    }
}
