package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.OrdemServicoRequest;
import com.neritech.saas.ordemservico.dto.OrdemServicoResponse;
import com.neritech.saas.ordemservico.service.OrdemServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/v1/ordens-servico","/api/v1/ordens-servico"})
@Tag(name = "Ordens de Serviço", description = "Gerenciamento de ordens de serviço")
public class OrdemServicoController {

    private final OrdemServicoService service;

    public OrdemServicoController(OrdemServicoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar nova ordem de serviço")
    public ResponseEntity<OrdemServicoResponse> create(@Valid @RequestBody OrdemServicoRequest request) {
        OrdemServicoResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ordem de serviço por ID")
    public ResponseEntity<OrdemServicoResponse> findById(@PathVariable Long id) {
        OrdemServicoResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar ordens de serviço por empresa")
    public ResponseEntity<Page<OrdemServicoResponse>> findByEmpresaId(
            @PathVariable Long empresaId,
            Pageable pageable) {
        Page<OrdemServicoResponse> response = service.findByEmpresaId(empresaId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar ordens de serviço por cliente")
    public ResponseEntity<Page<OrdemServicoResponse>> findByClienteId(
            @PathVariable Long clienteId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByClienteId(clienteId, pageable));
    }

    @GetMapping("/empresa/{empresaId}/status/{statusId}")
    @Operation(summary = "Listar ordens de serviço por empresa e status")
    public ResponseEntity<Page<OrdemServicoResponse>> findByEmpresaIdAndStatusId(
            @PathVariable Long empresaId,
            @PathVariable Long statusId,
            Pageable pageable) {
        Page<OrdemServicoResponse> response = service.findByEmpresaIdAndStatusId(empresaId, statusId, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar ordem de serviço")
    public ResponseEntity<OrdemServicoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody OrdemServicoRequest request) {
        OrdemServicoResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar ordem de serviço")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
