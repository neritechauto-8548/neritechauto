package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.DiagnosticoRequest;
import com.neritech.saas.ordemservico.dto.DiagnosticoResponse;
import com.neritech.saas.ordemservico.service.DiagnosticoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/diagnosticos")
@Tag(name = "Diagnósticos", description = "Gerenciamento de diagnósticos de ordens de serviço")
public class DiagnosticoController {

    private final DiagnosticoService service;

    public DiagnosticoController(DiagnosticoService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    @Operation(summary = "Criar novo diagnóstico")
    public ResponseEntity<DiagnosticoResponse> create(@Valid @RequestBody DiagnosticoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_VIS_SOLICITACOES')")
    @Operation(summary = "Buscar diagnóstico por ID")
    public ResponseEntity<DiagnosticoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/ordem-servico/{ordemServicoId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_VIS_SOLICITACOES')")
    @Operation(summary = "Listar diagnósticos por ordem de serviço")
    public ResponseEntity<List<DiagnosticoResponse>> findByOrdemServicoId(@PathVariable Long ordemServicoId) {
        return ResponseEntity.ok(service.findByOrdemServicoId(ordemServicoId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    @Operation(summary = "Atualizar diagnóstico")
    public ResponseEntity<DiagnosticoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DiagnosticoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EXC_SOLICITACOES')")
    @Operation(summary = "Excluir diagnóstico")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
