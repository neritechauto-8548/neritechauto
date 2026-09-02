package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.ItemOSProdutoRequest;
import com.neritech.saas.ordemservico.dto.ItemOSProdutoResponse;
import com.neritech.saas.ordemservico.service.ItemOSProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/itens-os-produtos")
@Tag(name = "Itens OS Produtos", description = "Gerenciamento de produtos vinculados às ordens de serviço")
public class ItemOSProdutoController {

    private final ItemOSProdutoService service;

    public ItemOSProdutoController(ItemOSProdutoService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_INC_ITENS')")
    @Operation(summary = "Criar novo item de produto")
    public ResponseEntity<ItemOSProdutoResponse> create(@Valid @RequestBody ItemOSProdutoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Buscar item de produto por ID")
    public ResponseEntity<ItemOSProdutoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/ordem-servico/{ordemServicoId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Listar itens de produto por ordem de serviço")
    public ResponseEntity<List<ItemOSProdutoResponse>> findByOrdemServicoId(@PathVariable Long ordemServicoId) {
        return ResponseEntity.ok(service.findByOrdemServicoId(ordemServicoId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_INC_ITENS')")
    @Operation(summary = "Atualizar item de produto")
    public ResponseEntity<ItemOSProdutoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ItemOSProdutoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_INC_ITENS')")
    @Operation(summary = "Excluir item de produto")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
