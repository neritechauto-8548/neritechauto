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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('FORN_INC_PEDIDOS')")
    @Operation(summary = "Criar novo pedido de fornecedor")
    public ResponseEntity<PedidoFornecedorResponse> create(@Valid @RequestBody PedidoFornecedorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('FORN_LISTAR_PEDIDOS','FORN_VER_ITENS')")
    @Operation(summary = "Buscar pedido de fornecedor por ID")
    public ResponseEntity<PedidoFornecedorResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FORN_LISTAR_PEDIDOS')")
    @Operation(summary = "Listar pedidos da empresa autenticada")
    public ResponseEntity<Page<PedidoFornecedorResponse>> findAll(
            @RequestParam(required = false) String termo,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(termo, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('FORN_EDIT_PEDIDOS')")
    @Operation(summary = "Atualizar pedido de fornecedor")
    public ResponseEntity<PedidoFornecedorResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PedidoFornecedorRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('FORN_ALT_STATUS')")
    @Operation(summary = "Atualizar status do pedido de fornecedor")
    public ResponseEntity<PedidoFornecedorResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam com.neritech.saas.produtoServico.domain.enums.StatusPedidoFornecedor status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('FORN_EXC_PEDIDOS')")
    @Operation(summary = "Excluir pedido de fornecedor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
