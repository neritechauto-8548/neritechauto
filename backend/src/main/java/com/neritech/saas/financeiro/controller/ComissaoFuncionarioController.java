package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.ComissaoFuncionarioRequest;
import com.neritech.saas.financeiro.dto.ComissaoFuncionarioResponse;
import com.neritech.saas.financeiro.service.ComissaoFuncionarioService;
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
@RequestMapping("/v1/financeiro/comissoes")
@RequiredArgsConstructor
@Tag(name = "ComissÃµes", description = "GestÃ£o de comissÃµes de funcionÃ¡rios")
public class ComissaoFuncionarioController {

    private final ComissaoFuncionarioService service;

    @GetMapping
    @Operation(summary = "Listar comissÃµes")
    public ResponseEntity<Page<ComissaoFuncionarioResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        // Using findAllByEmpresa if I implemented it, or findAll if I didn't.
        // I implemented findAllByEmpresa in service.
        return ResponseEntity.ok(service.findAllByEmpresa(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar comissÃ£o por ID")
    public ResponseEntity<ComissaoFuncionarioResponse> findById(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        return ResponseEntity.ok(service.findById(id, empresaId));
    }

    @PostMapping
    @Operation(summary = "Criar comissÃ£o")
    public ResponseEntity<ComissaoFuncionarioResponse> create(
            @RequestParam Long empresaId,
            @Valid @RequestBody ComissaoFuncionarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(empresaId, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar comissÃ£o")
    public ResponseEntity<ComissaoFuncionarioResponse> update(
            @PathVariable Long id,
            @RequestParam Long empresaId,
            @Valid @RequestBody ComissaoFuncionarioRequest request) {
        return ResponseEntity.ok(service.update(id, empresaId, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir comissÃ£o")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        service.delete(id, empresaId);
        return ResponseEntity.noContent().build();
    }
}
