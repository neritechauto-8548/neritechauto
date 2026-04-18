package com.neritech.saas.produtoServico.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.neritech.saas.produtoServico.dto.CategoriaProdutoRequest;
import com.neritech.saas.produtoServico.dto.CategoriaProdutoResponse;
import com.neritech.saas.produtoServico.service.CategoriaProdutoService;

import java.util.List;

@RestController
@RequestMapping("/v1/categorias-produtos")
@Tag(name = "Categorias de Produtos", description = "Gerenciamento de categorias de produtos")
public class CategoriaProdutoController {

    private final CategoriaProdutoService service;

    public CategoriaProdutoController(CategoriaProdutoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar nova categoria")
    public ResponseEntity<CategoriaProdutoResponse> create(@Valid @RequestBody CategoriaProdutoRequest request) {
        CategoriaProdutoResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID")
    public ResponseEntity<CategoriaProdutoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar categorias por empresa (paginado)")
    public ResponseEntity<Page<CategoriaProdutoResponse>> findAll(
            @RequestParam(required = false) Long empresaId,
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, search, pageable));
    }



    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria")
    public ResponseEntity<CategoriaProdutoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaProdutoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir categoria")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
