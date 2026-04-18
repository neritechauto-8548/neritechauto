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
}
