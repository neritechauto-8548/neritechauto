package com.neritech.saas.estoque.controller;

import com.neritech.saas.estoque.dto.LoteProdutoRequest;
import com.neritech.saas.estoque.dto.LoteProdutoResponse;
import com.neritech.saas.estoque.service.LoteProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/lotes-produtos")
@Tag(name = "Lotes de Produtos", description = "Gerenciamento de lotes de produtos")
public class LoteProdutoController {

    private final LoteProdutoService service;

    public LoteProdutoController(LoteProdutoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar novo lote")
    public ResponseEntity<LoteProdutoResponse> create(@Valid @RequestBody LoteProdutoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar lote por ID")
    public ResponseEntity<LoteProdutoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/produto/{produtoId}")
    @Operation(summary = "Listar lotes por produto")
    public ResponseEntity<Page<LoteProdutoResponse>> findByProduto(
            @PathVariable Long produtoId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByProduto(produtoId, pageable));
    }

    @GetMapping("/numero/{numeroLote}")
    @Operation(summary = "Listar lotes por nÃºmero")
    public ResponseEntity<Page<LoteProdutoResponse>> findByNumeroLote(
            @PathVariable String numeroLote,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByNumeroLote(numeroLote, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar lote")
    public ResponseEntity<LoteProdutoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody LoteProdutoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir lote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
