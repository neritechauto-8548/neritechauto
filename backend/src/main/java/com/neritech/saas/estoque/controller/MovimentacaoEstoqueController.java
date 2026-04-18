package com.neritech.saas.estoque.controller;

import com.neritech.saas.estoque.domain.enums.TipoMovimentacao;
import com.neritech.saas.estoque.dto.MovimentacaoEstoqueRequest;
import com.neritech.saas.estoque.dto.MovimentacaoEstoqueResponse;
import com.neritech.saas.estoque.service.MovimentacaoEstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/movimentacoes-estoque")
@Tag(name = "MovimentaÃ§Ãµes de Estoque", description = "Gerenciamento de movimentaÃ§Ãµes de estoque")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService service;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Registrar nova movimentaÃ§Ã£o")
    public ResponseEntity<MovimentacaoEstoqueResponse> create(@Valid @RequestBody MovimentacaoEstoqueRequest request) {
        MovimentacaoEstoqueResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar movimentaÃ§Ã£o por ID")
    public ResponseEntity<MovimentacaoEstoqueResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar movimentaÃ§Ãµes por empresa")
    public ResponseEntity<Page<MovimentacaoEstoqueResponse>> findByEmpresa(
            @PathVariable Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByEmpresa(empresaId, pageable));
    }

    @GetMapping("/empresa/{empresaId}/produto/{produtoId}")
    @Operation(summary = "Listar movimentaÃ§Ãµes por produto")
    public ResponseEntity<Page<MovimentacaoEstoqueResponse>> findByEmpresaAndProduto(
            @PathVariable Long empresaId,
            @PathVariable Long produtoId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByEmpresaAndProduto(empresaId, produtoId, pageable));
    }

    @GetMapping("/empresa/{empresaId}/tipo/{tipo}")
    @Operation(summary = "Listar movimentaÃ§Ãµes por tipo")
    public ResponseEntity<Page<MovimentacaoEstoqueResponse>> findByEmpresaAndTipo(
            @PathVariable Long empresaId,
            @PathVariable TipoMovimentacao tipo,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByEmpresaAndTipo(empresaId, tipo, pageable));
    }
}
