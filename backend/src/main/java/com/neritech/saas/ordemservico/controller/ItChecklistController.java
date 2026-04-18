package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.ItChecklistRequest;
import com.neritech.saas.ordemservico.dto.ItChecklistResponse;
import com.neritech.saas.ordemservico.service.ItChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("ordemServicoItChecklistController")
@RequestMapping("/v1/ordens-servico/it-checklist")
@Tag(name = "Itens de Checklist (OS)", description = "Gerenciamento de itens de checklist em ordens de serviço")
public class ItChecklistController {

    private final ItChecklistService service;

    public ItChecklistController(ItChecklistService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar itens de checklist", description = "Lista paginada de itens")
    public ResponseEntity<Page<ItChecklistResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item por ID")
    public ResponseEntity<ItChecklistResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/checklist/{checklistId}")
    @Operation(summary = "Listar itens por checklist")
    public ResponseEntity<List<ItChecklistResponse>> findByChecklistId(@PathVariable Long checklistId) {
        return ResponseEntity.ok(service.findByChecklistId(checklistId));
    }

    @PostMapping
    @Operation(summary = "Criar item de checklist")
    public ResponseEntity<ItChecklistResponse> create(@RequestBody @Valid ItChecklistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item de checklist")
    public ResponseEntity<ItChecklistResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ItChecklistRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir item de checklist")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
