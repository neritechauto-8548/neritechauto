package com.neritech.saas.fiscal.controller;

import com.neritech.saas.fiscal.dto.SpedFiscalRequest;
import com.neritech.saas.fiscal.dto.SpedFiscalResponse;
import com.neritech.saas.fiscal.service.SpedFiscalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/fiscal/sped")
@RequiredArgsConstructor
@Tag(name = "SPED Fiscal", description = "Gerenciamento de arquivos SPED Fiscal")
public class SpedFiscalController {

    private final SpedFiscalService service;

    @GetMapping
    @Operation(summary = "Listar todos os registros SPED Fiscal")
    public ResponseEntity<List<SpedFiscalResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar registro SPED Fiscal por ID")
    public ResponseEntity<SpedFiscalResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo registro SPED Fiscal")
    public ResponseEntity<SpedFiscalResponse> create(@Valid @RequestBody SpedFiscalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar registro SPED Fiscal")
    public ResponseEntity<SpedFiscalResponse> update(@PathVariable Long id,
            @Valid @RequestBody SpedFiscalRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar registro SPED Fiscal")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
