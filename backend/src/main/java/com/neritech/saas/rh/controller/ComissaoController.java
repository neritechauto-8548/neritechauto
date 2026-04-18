package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.service.ComissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/rh/comissoes")
@RequiredArgsConstructor
@Tag(name = "ComissÃµes", description = "GestÃ£o de comissÃµes")
public class ComissaoController {
    private final ComissaoService service;

    @GetMapping
    @Operation(summary = "Listar comissÃµes")
    public ResponseEntity<Page<ComissaoResponse>> findAll(@RequestParam Long empresaId, Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar comissÃ£o por ID")
    public ResponseEntity<ComissaoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar comissÃ£o")
    public ResponseEntity<ComissaoResponse> create(@Valid @RequestBody ComissaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar comissÃ£o")
    public ResponseEntity<ComissaoResponse> update(@PathVariable Long id, @Valid @RequestBody ComissaoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir comissÃ£o")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
