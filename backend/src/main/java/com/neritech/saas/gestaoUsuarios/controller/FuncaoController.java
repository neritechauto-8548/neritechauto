package com.neritech.saas.gestaoUsuarios.controller;

import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.service.FuncaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/funcoes")
@RequiredArgsConstructor
@Tag(name = "Funções", description = "Gestão de funções (roles) e permissões associadas")
public class FuncaoController {

    private final FuncaoService funcaoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('FUNCAO.READ')")
    @Operation(summary = "Listar funções", description = "Retorna todas as funções cadastradas")
    public ResponseEntity<List<Funcao>> findAll() {
        return ResponseEntity.ok(funcaoService.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('FUNCAO.CREATE')")
    @Operation(summary = "Criar função", description = "Cria uma nova função")
    public ResponseEntity<Funcao> create(@RequestBody Funcao funcao) {
        return ResponseEntity.ok(funcaoService.save(funcao));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('FUNCAO.READ')")
    @Operation(summary = "Buscar função por ID", description = "Retorna os detalhes de uma função específica")
    public ResponseEntity<Funcao> findById(@PathVariable Long id) {
        return ResponseEntity.ok(funcaoService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('FUNCAO.UPDATE')")
    @Operation(summary = "Atualizar função", description = "Atualiza os dados e permissões de uma função")
    public ResponseEntity<Funcao> update(@PathVariable Long id, @RequestBody Funcao funcao) {
        return ResponseEntity.ok(funcaoService.update(id, funcao));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('FUNCAO.DELETE')")
    @Operation(summary = "Excluir função", description = "Remove uma função do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        funcaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
