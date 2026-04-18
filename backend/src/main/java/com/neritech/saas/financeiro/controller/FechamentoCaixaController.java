package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.FechamentoCaixaRequest;
import com.neritech.saas.financeiro.dto.FechamentoCaixaResponse;
import com.neritech.saas.financeiro.service.FechamentoCaixaService;
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

import java.time.LocalDate;

@RestController
@RequestMapping({"/v1/financeiro/fechamento-caixa", "/api/v1/financeiro/fechamento-caixa"})
@RequiredArgsConstructor
@Tag(name = "Fechamento de Caixa", description = "Gestão de fechamento de caixa")
public class FechamentoCaixaController {

    private final FechamentoCaixaService service;
    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar fechamentos de caixa")
    public ResponseEntity<Page<FechamentoCaixaResponse>> findAll(
            @RequestParam(required = false) Long empresaId,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Pageable pageable) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.findAll(empresa, dataInicio, dataFim, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar fechamento por ID")
    public ResponseEntity<FechamentoCaixaResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.findById(id, empresa));
    }

    @PostMapping
    @Operation(summary = "Criar fechamento de caixa")
    public ResponseEntity<FechamentoCaixaResponse> create(
            @RequestParam(required = false) Long empresaId,
            @Valid @RequestBody FechamentoCaixaRequest request) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(empresa, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar fechamento de caixa")
    public ResponseEntity<FechamentoCaixaResponse> update(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId,
            @Valid @RequestBody FechamentoCaixaRequest request) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.update(id, empresa, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir fechamento de caixa")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        service.delete(id, empresa);
        return ResponseEntity.noContent().build();
    }
}
