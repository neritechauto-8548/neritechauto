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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('GERAL_CONFIG_CHECKLIST','OS_ADC_CHECKLIST','OS_VIS_CHECKLIST')")
    @Operation(summary = "Listar checklists", description = "Lista paginada da empresa autenticada")
    public ResponseEntity<Page<ChecklistResponse>> findAll(
            @RequestParam(required = false) Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('GERAL_CONFIG_CHECKLIST','OS_ADC_CHECKLIST','OS_VIS_CHECKLIST')")
    @Operation(summary = "Buscar checklist por ID")
    public ResponseEntity<ChecklistResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_CHECKLIST')")
    @Operation(summary = "Criar checklist")
    public ResponseEntity<ChecklistResponse> create(@RequestBody @Valid ChecklistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_CHECKLIST')")
    @Operation(summary = "Atualizar checklist")
    public ResponseEntity<ChecklistResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ChecklistRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_CHECKLIST')")
    @Operation(summary = "Excluir checklist")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
