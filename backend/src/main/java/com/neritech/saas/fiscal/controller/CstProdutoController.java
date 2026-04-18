package com.neritech.saas.fiscal.controller;

import com.neritech.saas.fiscal.dto.CstProdutoRequest;
import com.neritech.saas.fiscal.dto.CstProdutoResponse;
import com.neritech.saas.fiscal.service.CstProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/fiscal/cst")
@RequiredArgsConstructor
@Tag(name = "CST", description = "Gerenciamento de CÃ³digos de SituaÃ§Ã£o TributÃ¡ria")
public class CstProdutoController {

    private final CstProdutoService service;

    @GetMapping
    @Operation(summary = "Listar todos os CSTs")
    public ResponseEntity<List<CstProdutoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar CST por ID")
    public ResponseEntity<CstProdutoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo CST")
    public ResponseEntity<CstProdutoResponse> create(@Valid @RequestBody CstProdutoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar CST")
    public ResponseEntity<CstProdutoResponse> update(@PathVariable Long id,
            @Valid @RequestBody CstProdutoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar CST")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
