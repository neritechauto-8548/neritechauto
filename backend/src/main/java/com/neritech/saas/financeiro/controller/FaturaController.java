package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.FaturaRequest;
import com.neritech.saas.financeiro.dto.FaturaResponse;
import com.neritech.saas.financeiro.service.FaturaService;
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
@RequestMapping("/v1/financeiro/faturas")
@RequiredArgsConstructor
@Tag(name = "Faturas", description = "GestÃ£o de faturas")
public class FaturaController {

    private final FaturaService service;

    @GetMapping
    @Operation(summary = "Listar faturas")
    public ResponseEntity<Page<FaturaResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar fatura por ID")
    public ResponseEntity<FaturaResponse> findById(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        return ResponseEntity.ok(service.findById(id, empresaId));
    }

    @GetMapping("/ordem-servico/{osId}")
    @Operation(summary = "Buscar fatura por OS")
    public ResponseEntity<FaturaResponse> findByOrdemServico(
            @PathVariable Long osId,
            @RequestParam Long empresaId) {
        try {
            return ResponseEntity.ok(service.findByOrdemServico(osId, empresaId));
        } catch (jakarta.persistence.EntityNotFoundException ex) {
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping
    @Operation(summary = "Criar fatura")
    public ResponseEntity<FaturaResponse> create(
            @RequestParam Long empresaId,
            @Valid @RequestBody FaturaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(empresaId, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar fatura")
    public ResponseEntity<FaturaResponse> update(
            @PathVariable Long id,
            @RequestParam Long empresaId,
            @Valid @RequestBody FaturaRequest request) {
        return ResponseEntity.ok(service.update(id, empresaId, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir fatura")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        service.delete(id, empresaId);
        return ResponseEntity.noContent().build();
    }
}
