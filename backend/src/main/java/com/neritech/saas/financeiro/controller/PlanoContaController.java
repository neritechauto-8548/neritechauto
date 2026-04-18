package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.PlanoContaRequest;
import com.neritech.saas.financeiro.dto.PlanoContaResponse;
import com.neritech.saas.financeiro.service.PlanoContaService;
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
@RequestMapping("/v1/financeiro/plano-contas")
@RequiredArgsConstructor
@Tag(name = "Plano de Contas", description = "GestÃ£o do plano de contas")
public class PlanoContaController {

    private final PlanoContaService service;
    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar plano de contas")
    public ResponseEntity<Page<PlanoContaResponse>> findAll(
            @RequestParam(required = false) Long empresaId,
            Pageable pageable) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.findAll(empresa, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar conta por ID")
    public ResponseEntity<PlanoContaResponse> findById(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar conta")
    public ResponseEntity<PlanoContaResponse> create(
            @Valid @RequestBody PlanoContaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar conta")
    public ResponseEntity<PlanoContaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PlanoContaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir conta")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
