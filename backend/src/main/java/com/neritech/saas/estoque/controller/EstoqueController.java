package com.neritech.saas.estoque.controller;

import com.neritech.saas.estoque.domain.enums.StatusEstoque;
import com.neritech.saas.estoque.dto.EstoqueRequest;
import com.neritech.saas.estoque.dto.EstoqueResponse;
import com.neritech.saas.estoque.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/estoques")
@Tag(name = "Estoques", description = "Gerenciamento de estoques")
public class EstoqueController {

    private final EstoqueService service;

    public EstoqueController(EstoqueService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar novo estoque")
    public ResponseEntity<EstoqueResponse> create(@Valid @RequestBody EstoqueRequest request) {
        EstoqueResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar estoque por ID")
    public ResponseEntity<EstoqueResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar estoques por empresa (paginado)")
    public ResponseEntity<Page<EstoqueResponse>> findByEmpresa(
            @PathVariable Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByEmpresa(empresaId, pageable));
    }

    @GetMapping("/empresa/{empresaId}/status/{status}")
    @Operation(summary = "Listar estoques por empresa e status (paginado)")
    public ResponseEntity<Page<EstoqueResponse>> findByEmpresaAndStatus(
            @PathVariable Long empresaId,
            @PathVariable StatusEstoque status,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByEmpresaAndStatus(empresaId, status, pageable));
    }

    @GetMapping("/empresa/{empresaId}/produto/{produtoId}")
    @Operation(summary = "Listar estoques por empresa e produto (paginado)")
    public ResponseEntity<Page<EstoqueResponse>> findByEmpresaAndProduto(
            @PathVariable Long empresaId,
            @PathVariable Long produtoId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByEmpresaAndProduto(empresaId, produtoId, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar estoque")
    public ResponseEntity<EstoqueResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EstoqueRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir estoque")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
