package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.FuncionarioRequest;
import com.neritech.saas.rh.dto.FuncionarioResponse;
import com.neritech.saas.rh.service.FuncionarioService;
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
@RequestMapping("/v1/rh/funcionarios")
@RequiredArgsConstructor
@Tag(name = "FuncionÃ¡rios", description = "GestÃ£o de funcionÃ¡rios")
public class FuncionarioController {

    private final FuncionarioService service;

    @GetMapping
    @Operation(summary = "Listar funcionÃ¡rios")
    public ResponseEntity<Page<FuncionarioResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar funcionÃ¡rio por ID")
    public ResponseEntity<FuncionarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar funcionÃ¡rio")
    public ResponseEntity<FuncionarioResponse> create(@Valid @RequestBody FuncionarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar funcionÃ¡rio")
    public ResponseEntity<FuncionarioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody FuncionarioRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir funcionÃ¡rio")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
