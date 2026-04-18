package com.neritech.saas.produtoServico.controller;

import com.neritech.saas.produtoServico.dto.PedidoFornecedorRequest;
import com.neritech.saas.produtoServico.dto.PedidoFornecedorResponse;
import com.neritech.saas.produtoServico.service.PedidoFornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/pedidos-fornecedor")
@Tag(name = "Pedidos de Fornecedor", description = "Gerenciamento de pedidos de compra para fornecedores")
public class PedidoFornecedorController {

    private final PedidoFornecedorService service;

    public PedidoFornecedorController(PedidoFornecedorService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar novo pedido de fornecedor")
    public ResponseEntity<PedidoFornecedorResponse> create(@Valid @RequestBody PedidoFornecedorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido de fornecedor por ID")
    public ResponseEntity<PedidoFornecedorResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar pedidos de fornecedor por empresa (paginado)")
    public ResponseEntity<Page<PedidoFornecedorResponse>> findAll(
            @RequestParam Long empresaId,
            @RequestParam(required = false) String termo,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, termo, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pedido de fornecedor")
    public ResponseEntity<PedidoFornecedorResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PedidoFornecedorRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pedido de fornecedor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
