package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.DepartamentoRequest;
import com.neritech.saas.rh.dto.DepartamentoResponse;
import com.neritech.saas.rh.service.DepartamentoService;
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
@RequestMapping("/v1/rh/departamentos")
@RequiredArgsConstructor
@Tag(name = "Departamentos", description = "GestÃ£o de departamentos")
public class DepartamentoController {
    private final DepartamentoService service;

    @GetMapping
    @Operation(summary = "Listar departamentos")
    public ResponseEntity<Page<DepartamentoResponse>> findAll(@RequestParam Long empresaId, Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar departamento por ID")
    public ResponseEntity<DepartamentoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar departamento")
    public ResponseEntity<DepartamentoResponse> create(@Valid @RequestBody DepartamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar departamento")
    public ResponseEntity<DepartamentoResponse> update(@PathVariable Long id,
            @Valid @RequestBody DepartamentoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir departamento")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
