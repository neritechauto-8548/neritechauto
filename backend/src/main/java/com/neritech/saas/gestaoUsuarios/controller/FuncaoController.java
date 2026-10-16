package com.neritech.saas.gestaoUsuarios.controller;

import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.dto.FuncaoRequest;
import com.neritech.saas.gestaoUsuarios.dto.FuncaoResponse;
import com.neritech.saas.gestaoUsuarios.service.FuncaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/v1/funcoes")
@RequiredArgsConstructor
@Tag(name = "Funções", description = "Gestão de funções (roles) e permissões associadas")
public class FuncaoController {

    private final FuncaoService funcaoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar funções", description = "Retorna todas as funções cadastradas")
    public ResponseEntity<Page<FuncaoResponse>> findAll(@RequestParam(required = false) String q,
            @RequestParam(required = false) Boolean ativo, Pageable pageable) {
        return ResponseEntity.ok(funcaoService.search(q, ativo, pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar função", description = "Cria uma nova função")
    public ResponseEntity<FuncaoResponse> create(@Valid @RequestBody FuncaoRequest request) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(funcaoService.create(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar função por ID", description = "Retorna os detalhes de uma função específica")
    public ResponseEntity<FuncaoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(funcaoService.get(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar função", description = "Atualiza os dados e permissões de uma função")
    public ResponseEntity<FuncaoResponse> update(@PathVariable Long id, @Valid @RequestBody FuncaoRequest request) {
        return ResponseEntity.ok(funcaoService.updateSecure(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Excluir função", description = "Remove uma função do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        funcaoService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/historico")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<com.neritech.saas.gestaoUsuarios.dto.FuncaoAlteracaoResponse>> history(
            @PathVariable Long id, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(funcaoService.history(id, page, size));
    }
}
