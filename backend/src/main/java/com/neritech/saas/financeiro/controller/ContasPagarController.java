package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.ContasPagarRequest;
import com.neritech.saas.financeiro.dto.ContasPagarResponse;
import com.neritech.saas.financeiro.service.ContasPagarService;
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
@RequestMapping({"/v1/financeiro/contas-pagar", "/api/v1/financeiro/contas-pagar"})
@RequiredArgsConstructor
@Tag(name = "Contas a Pagar", description = "GestÃ£o de contas a pagar")
public class ContasPagarController {

    private final ContasPagarService service;
    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar contas a pagar")
    public ResponseEntity<Page<ContasPagarResponse>> findAll(
            @RequestParam(required = false) Long empresaId,
            Pageable pageable) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.findAll(empresa, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar conta a pagar por ID")
    public ResponseEntity<ContasPagarResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.findById(id, empresa));
    }

    @PostMapping
    @Operation(summary = "Criar conta a pagar")
    public ResponseEntity<ContasPagarResponse> create(
            @RequestParam(required = false) Long empresaId,
            @Valid @RequestBody ContasPagarRequest request) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(empresa, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar conta a pagar")
    public ResponseEntity<ContasPagarResponse> update(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId,
            @Valid @RequestBody ContasPagarRequest request) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.update(id, empresa, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir conta a pagar")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        service.delete(id, empresa);
        return ResponseEntity.noContent().build();
    }
}
