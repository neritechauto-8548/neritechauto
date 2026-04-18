package com.neritech.saas.produtoServico.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.neritech.saas.produtoServico.dto.TabelaPrecoRequest;
import com.neritech.saas.produtoServico.dto.TabelaPrecoResponse;
import com.neritech.saas.produtoServico.service.TabelaPrecoService;

@RestController
@RequestMapping("/v1/tabelas-precos")
@Tag(name = "Tabelas de PreÃ§os", description = "Gerenciamento de tabelas de preÃ§os")
public class TabelaPrecoController {

    private final TabelaPrecoService service;

    public TabelaPrecoController(TabelaPrecoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar nova tabela de preÃ§o")
    public ResponseEntity<TabelaPrecoResponse> create(@Valid @RequestBody TabelaPrecoRequest request) {
        TabelaPrecoResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tabela de preÃ§o por ID")
    public ResponseEntity<TabelaPrecoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar tabelas de preÃ§os por empresa (paginado)")
    public ResponseEntity<Page<TabelaPrecoResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tabela de preÃ§o")
    public ResponseEntity<TabelaPrecoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TabelaPrecoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir tabela de preÃ§o")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
