package com.neritech.saas.comunicacao.controller;

import com.neritech.saas.comunicacao.dto.ListaContatosRequest;
import com.neritech.saas.comunicacao.dto.ListaContatosResponse;
import com.neritech.saas.comunicacao.service.ListaContatosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comunicacao/listas")
@RequiredArgsConstructor
@Tag(name = "Listas de Contatos", description = "Gerenciamento de listas de contatos para campanhas")
public class ListaContatosController {

    private final ListaContatosService service;

    @GetMapping
    @Operation(summary = "Listar listas de contatos", description = "Retorna uma lista paginada de listas de contatos")
    public ResponseEntity<Page<ListaContatosResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar lista por ID", description = "Retorna os detalhes de uma lista de contatos especÃ­fica")
    public ResponseEntity<ListaContatosResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar lista de contatos", description = "Cria uma nova lista de contatos")
    public ResponseEntity<ListaContatosResponse> create(@RequestBody @Valid ListaContatosRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar lista de contatos", description = "Atualiza os dados de uma lista de contatos existente")
    public ResponseEntity<ListaContatosResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ListaContatosRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir lista de contatos", description = "Remove uma lista de contatos do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
