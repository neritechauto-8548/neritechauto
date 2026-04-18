package com.neritech.saas.produtoServico.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.neritech.saas.produtoServico.dto.ProdutoFornecedorRequest;
import com.neritech.saas.produtoServico.dto.ProdutoFornecedorResponse;
import com.neritech.saas.produtoServico.service.ProdutoFornecedorService;

@RestController
@RequestMapping("/v1/produtos-fornecedores")
@Tag(name = "Produtos e Fornecedores", description = "Gerenciamento da relaÃ§Ã£o entre produtos e fornecedores")
public class ProdutoFornecedorController {

    private final ProdutoFornecedorService service;

    public ProdutoFornecedorController(ProdutoFornecedorService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar nova relaÃ§Ã£o produto-fornecedor")
    public ResponseEntity<ProdutoFornecedorResponse> create(@Valid @RequestBody ProdutoFornecedorRequest request) {
        ProdutoFornecedorResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar relaÃ§Ã£o por ID")
    public ResponseEntity<ProdutoFornecedorResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/produto/{produtoId}")
    @Operation(summary = "Listar fornecedores de um produto (paginado)")
    public ResponseEntity<Page<ProdutoFornecedorResponse>> findByProduto(
            @PathVariable Long produtoId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByProduto(produtoId, pageable));
    }

    @GetMapping("/fornecedor/{fornecedorId}")
    @Operation(summary = "Listar produtos de um fornecedor (paginado)")
    public ResponseEntity<Page<ProdutoFornecedorResponse>> findByFornecedor(
            @PathVariable Long fornecedorId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByFornecedor(fornecedorId, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar relaÃ§Ã£o produto-fornecedor")
    public ResponseEntity<ProdutoFornecedorResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoFornecedorRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir relaÃ§Ã£o produto-fornecedor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
