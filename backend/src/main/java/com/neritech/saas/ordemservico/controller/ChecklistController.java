package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.ChecklistRequest;
import com.neritech.saas.ordemservico.dto.ChecklistResponse;
import com.neritech.saas.ordemservico.service.ChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/ordens-servico/checklists")
@Tag(name = "Checklists", description = "Gerenciamento de checklists de ordens de serviço")
public class ChecklistController {

    private final ChecklistService service;

    public ChecklistController(ChecklistService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar checklists", description = "Lista paginada por empresa")
    public ResponseEntity<Page<ChecklistResponse>> findAll(
            @RequestParam(required = false) Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar checklist por ID")
    public ResponseEntity<ChecklistResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar checklist")
    public ResponseEntity<ChecklistResponse> create(@RequestBody @Valid ChecklistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar checklist")
    public ResponseEntity<ChecklistResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ChecklistRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir checklist")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

