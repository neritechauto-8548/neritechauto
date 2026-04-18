package com.neritech.saas.estoque.controller;

import com.neritech.saas.estoque.domain.enums.TipoPerda;
import com.neritech.saas.estoque.dto.PerdaEstoqueRequest;
import com.neritech.saas.estoque.dto.PerdaEstoqueResponse;
import com.neritech.saas.estoque.service.PerdaEstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/perdas-estoque")
@Tag(name = "Perdas de Estoque", description = "Gerenciamento de perdas de estoque")
public class PerdaEstoqueController {

    private final PerdaEstoqueService service;

    public PerdaEstoqueController(PerdaEstoqueService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar nova perda")
    public ResponseEntity<PerdaEstoqueResponse> create(@Valid @RequestBody PerdaEstoqueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar perda por ID")
    public ResponseEntity<PerdaEstoqueResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/produto/{produtoId}")
    @Operation(summary = "Listar perdas por produto")
    public ResponseEntity<Page<PerdaEstoqueResponse>> findByProduto(
            @PathVariable Long produtoId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByProduto(produtoId, pageable));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar perdas por tipo")
    public ResponseEntity<Page<PerdaEstoqueResponse>> findByTipo(
            @PathVariable TipoPerda tipo,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByTipo(tipo, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar perda")
    public ResponseEntity<PerdaEstoqueResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PerdaEstoqueRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir perda")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
