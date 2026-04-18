package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.ContasReceberRequest;
import com.neritech.saas.financeiro.dto.ContasReceberResponse;
import com.neritech.saas.financeiro.service.ContasReceberService;
import com.neritech.saas.gestaoUsuarios.service.UsuarioService;
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
@RequestMapping({"/v1/financeiro/contas-receber", "/api/v1/financeiro/contas-receber"})
@RequiredArgsConstructor
@Tag(name = "Contas a Receber", description = "GestÃ£o de contas a receber")
public class ContasReceberController {

    private final ContasReceberService service;
    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar contas a receber")
    public ResponseEntity<Page<ContasReceberResponse>> findAll(
            @RequestParam(required = false) Long empresaId,
            Pageable pageable) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.findAll(empresa, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar conta a receber por ID")
    public ResponseEntity<ContasReceberResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.findById(id, empresa));
    }

    @PostMapping
    @Operation(summary = "Criar conta a receber")
    public ResponseEntity<ContasReceberResponse> create(
            @RequestParam(required = false) Long empresaId,
            @Valid @RequestBody ContasReceberRequest request) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(empresa, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar conta a receber")
    public ResponseEntity<ContasReceberResponse> update(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId,
            @Valid @RequestBody ContasReceberRequest request) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.update(id, empresa, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir conta a receber")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        service.delete(id, empresa);
        return ResponseEntity.noContent().build();
    }
}
