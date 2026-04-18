package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.DiagnosticoRequest;
import com.neritech.saas.ordemservico.dto.DiagnosticoResponse;
import com.neritech.saas.ordemservico.service.DiagnosticoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/diagnosticos")
@Tag(name = "DiagnÃ³sticos", description = "Gerenciamento de diagnÃ³sticos de ordens de serviÃ§o")
public class DiagnosticoController {

    private final DiagnosticoService service;

    public DiagnosticoController(DiagnosticoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar novo diagnÃ³stico")
    public ResponseEntity<DiagnosticoResponse> create(@Valid @RequestBody DiagnosticoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar diagnÃ³stico por ID")
    public ResponseEntity<DiagnosticoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/ordem-servico/{ordemServicoId}")
    @Operation(summary = "Listar diagnÃ³sticos por ordem de serviÃ§o")
    public ResponseEntity<List<DiagnosticoResponse>> findByOrdemServicoId(@PathVariable Long ordemServicoId) {
        return ResponseEntity.ok(service.findByOrdemServicoId(ordemServicoId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar diagnÃ³stico")
    public ResponseEntity<DiagnosticoResponse> update(@PathVariable Long id,
            @Valid @RequestBody DiagnosticoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar diagnÃ³stico")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
