package com.neritech.saas.estoque.controller;

import com.neritech.saas.estoque.domain.enums.TipoAlerta;
import com.neritech.saas.estoque.domain.*;
import com.neritech.saas.estoque.domain.enums.StatusAlerta;
import com.neritech.saas.estoque.dto.AlertaEstoqueRequest;
import com.neritech.saas.estoque.dto.AlertaEstoqueResponse;
import com.neritech.saas.estoque.service.AlertaEstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/alertas-estoque")
@Tag(name = "Alertas de Estoque", description = "Gerenciamento de alertas de estoque")
public class AlertaEstoqueController {

    private final AlertaEstoqueService service;

    public AlertaEstoqueController(AlertaEstoqueService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar novo alerta")
    public ResponseEntity<AlertaEstoqueResponse> create(@Valid @RequestBody AlertaEstoqueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar alerta por ID")
    public ResponseEntity<AlertaEstoqueResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar alertas por empresa")
    public ResponseEntity<Page<AlertaEstoqueResponse>> findByEmpresa(
            @PathVariable Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByEmpresa(empresaId, pageable));
    }

    @GetMapping("/empresa/{empresaId}/status/{status}")
    @Operation(summary = "Listar alertas por status")
    public ResponseEntity<Page<AlertaEstoqueResponse>> findByEmpresaAndStatus(
            @PathVariable Long empresaId,
            @PathVariable StatusAlerta status,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByEmpresaAndStatus(empresaId, status, pageable));
    }

    @GetMapping("/empresa/{empresaId}/tipo/{tipo}")
    @Operation(summary = "Listar alertas por tipo")
    public ResponseEntity<Page<AlertaEstoqueResponse>> findByEmpresaAndTipo(
            @PathVariable Long empresaId,
            @PathVariable TipoAlerta tipo,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByEmpresaAndTipo(empresaId, tipo, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar alerta")
    public ResponseEntity<AlertaEstoqueResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AlertaEstoqueRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir alerta")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
