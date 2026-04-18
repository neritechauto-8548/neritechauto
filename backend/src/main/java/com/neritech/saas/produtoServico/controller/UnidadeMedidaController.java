package com.neritech.saas.produtoServico.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.neritech.saas.produtoServico.dto.UnidadeMedidaRequest;
import com.neritech.saas.produtoServico.dto.UnidadeMedidaResponse;
import com.neritech.saas.produtoServico.service.UnidadeMedidaService;

import java.util.List;

@RestController
@RequestMapping("/v1/unidades-medida")
@Tag(name = "Unidades de Medida", description = "Gerenciamento de unidades de medida")
public class UnidadeMedidaController {

    private final UnidadeMedidaService service;

    public UnidadeMedidaController(UnidadeMedidaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar nova unidade de medida")
    public ResponseEntity<UnidadeMedidaResponse> create(@Valid @RequestBody UnidadeMedidaRequest request) {
        UnidadeMedidaResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar unidade de medida por ID")
    public ResponseEntity<UnidadeMedidaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar unidades de medida (paginado)")
    public ResponseEntity<Page<UnidadeMedidaResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/ativas")
    @Operation(summary = "Listar unidades de medida ativas")
    public ResponseEntity<List<UnidadeMedidaResponse>> findActive() {
        return ResponseEntity.ok(service.findActive());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar unidade de medida")
    public ResponseEntity<UnidadeMedidaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UnidadeMedidaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir unidade de medida")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
