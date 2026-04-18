package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.ContaBancariaRequest;
import com.neritech.saas.financeiro.dto.ContaBancariaResponse;
import com.neritech.saas.financeiro.service.ContaBancariaService;
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
@RequestMapping("/v1/financeiro/contas-bancarias")
@RequiredArgsConstructor
@Tag(name = "Contas BancÃ¡rias", description = "GestÃ£o de contas bancÃ¡rias")
public class ContaBancariaController {

    private final ContaBancariaService service;
    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar contas bancÃ¡rias")
    public ResponseEntity<Page<ContaBancariaResponse>> findAll(
            @RequestParam(required = false) Long empresaId,
            Pageable pageable) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.findAll(empresa, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar conta bancÃ¡ria por ID")
    public ResponseEntity<ContaBancariaResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.findById(id, empresa));
    }

    @PostMapping
    @Operation(summary = "Criar conta bancÃ¡ria")
    public ResponseEntity<ContaBancariaResponse> create(
            @RequestParam(required = false) Long empresaId,
            @Valid @RequestBody ContaBancariaRequest request) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(empresa, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar conta bancÃ¡ria")
    public ResponseEntity<ContaBancariaResponse> update(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId,
            @Valid @RequestBody ContaBancariaRequest request) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.update(id, empresa, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir conta bancÃ¡ria")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        service.delete(id, empresa);
        return ResponseEntity.noContent().build();
    }
}
