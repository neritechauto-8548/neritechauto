package com.neritech.saas.fiscal.controller;

import com.neritech.saas.fiscal.dto.NcmProdutoRequest;
import com.neritech.saas.fiscal.dto.NcmProdutoResponse;
import com.neritech.saas.fiscal.service.NcmProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/fiscal/ncm")
@RequiredArgsConstructor
@Tag(name = "NCM", description = "Gerenciamento de Nomenclatura Comum do Mercosul")
public class NcmProdutoController {

    private final NcmProdutoService service;

    @GetMapping
    @Operation(summary = "Listar todos os NCMs")
    public ResponseEntity<List<NcmProdutoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar NCM por ID")
    public ResponseEntity<NcmProdutoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo NCM")
    public ResponseEntity<NcmProdutoResponse> create(@Valid @RequestBody NcmProdutoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar NCM")
    public ResponseEntity<NcmProdutoResponse> update(@PathVariable Long id,
            @Valid @RequestBody NcmProdutoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar NCM")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
