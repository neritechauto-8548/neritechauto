package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.service.DocumentoFuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/rh/documentos")
@RequiredArgsConstructor
@Tag(name = "Documentos", description = "GestÃ£o de documentos de funcionÃ¡rios")
public class DocumentoFuncionarioController {
    private final DocumentoFuncionarioService service;

    @GetMapping("/funcionario/{funcionarioId}")
    @Operation(summary = "Listar documentos do funcionÃ¡rio")
    public ResponseEntity<List<DocumentoFuncionarioResponse>> findByFuncionario(@PathVariable Long funcionarioId) {
        return ResponseEntity.ok(service.findByFuncionario(funcionarioId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar documento por ID")
    public ResponseEntity<DocumentoFuncionarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar documento")
    public ResponseEntity<DocumentoFuncionarioResponse> create(
            @Valid @RequestBody DocumentoFuncionarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar documento")
    public ResponseEntity<DocumentoFuncionarioResponse> update(@PathVariable Long id,
            @Valid @RequestBody DocumentoFuncionarioRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir documento")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
