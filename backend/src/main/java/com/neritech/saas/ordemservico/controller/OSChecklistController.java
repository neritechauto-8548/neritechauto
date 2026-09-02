package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.OSChecklistCopyRequest;
import com.neritech.saas.ordemservico.dto.OSChecklistItemRequest;
import com.neritech.saas.ordemservico.dto.OSChecklistItemResponse;
import com.neritech.saas.ordemservico.service.OSChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/ordens-servico/os-checklist")
@Tag(name = "Checklist da OS", description = "Itens de checklist específicos por Ordem de Serviço")
public class OSChecklistController {

    private final OSChecklistService service;

    public OSChecklistController(OSChecklistService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_ADC_CHECKLIST')")
    @Operation(summary = "Copiar itens do checklist modelo para a OS")
    public ResponseEntity<List<OSChecklistItemResponse>> copy(@RequestBody @Valid OSChecklistCopyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.copyFromChecklist(request));
    }

    @GetMapping("/ordem-servico/{osId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_VIS_CHECKLIST')")
    @Operation(summary = "Listar itens de checklist da OS")
    public ResponseEntity<List<OSChecklistItemResponse>> listByOS(@PathVariable Long osId) {
        return ResponseEntity.ok(service.listByOS(osId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDIT_CHECKLIST')")
    @Operation(summary = "Atualizar item de checklist da OS")
    public ResponseEntity<OSChecklistItemResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid OSChecklistItemRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDIT_CHECKLIST')")
    @Operation(summary = "Excluir item de checklist da OS")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
