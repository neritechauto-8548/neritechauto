package com.neritech.saas.estoque.controller;

import com.neritech.saas.estoque.domain.enums.StatusReserva;
import com.neritech.saas.estoque.dto.ReservaEstoqueRequest;
import com.neritech.saas.estoque.dto.ReservaEstoqueResponse;
import com.neritech.saas.estoque.service.ReservaEstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/reservas-estoque")
@Tag(name = "Reservas de Estoque", description = "Gerenciamento de reservas de estoque")
public class ReservaEstoqueController {

    private final ReservaEstoqueService service;

    public ReservaEstoqueController(ReservaEstoqueService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar nova reserva")
    public ResponseEntity<ReservaEstoqueResponse> create(@Valid @RequestBody ReservaEstoqueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva por ID")
    public ResponseEntity<ReservaEstoqueResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/produto/{produtoId}")
    @Operation(summary = "Listar reservas por produto")
    public ResponseEntity<Page<ReservaEstoqueResponse>> findByProduto(
            @PathVariable Long produtoId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByProduto(produtoId, pageable));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar reservas por status")
    public ResponseEntity<Page<ReservaEstoqueResponse>> findByStatus(
            @PathVariable StatusReserva status,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByStatus(status, pageable));
    }

    @GetMapping("/documento/{documentoTipo}/{documentoId}")
    @Operation(summary = "Listar reservas por documento")
    public ResponseEntity<Page<ReservaEstoqueResponse>> findByDocumento(
            @PathVariable String documentoTipo,
            @PathVariable Long documentoId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByDocumento(documentoTipo, documentoId, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar reserva")
    public ResponseEntity<ReservaEstoqueResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ReservaEstoqueRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir reserva")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
