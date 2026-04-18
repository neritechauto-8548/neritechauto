package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.service.EspecialidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/rh/especialidades")
@RequiredArgsConstructor
@Tag(name = "Especialidades", description = "GestÃ£o de especialidades tÃ©cnicas")
public class EspecialidadeController {
    private final EspecialidadeService service;

    @GetMapping
    @Operation(summary = "Listar especialidades")
    public ResponseEntity<Page<EspecialidadeResponse>> findAll(@RequestParam Long empresaId, Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar especialidade por ID")
    public ResponseEntity<EspecialidadeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar especialidade")
    public ResponseEntity<EspecialidadeResponse> create(@Valid @RequestBody EspecialidadeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar especialidade")
    public ResponseEntity<EspecialidadeResponse> update(@PathVariable Long id,
            @Valid @RequestBody EspecialidadeRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir especialidade")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
