package com.neritech.saas.comunicacao.controller;

import com.neritech.saas.comunicacao.dto.QuestionarioRequest;
import com.neritech.saas.comunicacao.dto.QuestionarioResponse;
import com.neritech.saas.comunicacao.service.QuestionarioService;
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
@RequestMapping("/v1/comunicacao/questionarios")
@RequiredArgsConstructor
@Tag(name = "Questionários", description = "Gerenciamento de questionários de comunicação")
public class QuestionarioController {

    private final QuestionarioService service;

    @GetMapping
    @Operation(summary = "Listar questionários", description = "Retorna lista paginada de questionários por empresa")
    public ResponseEntity<Page<QuestionarioResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar questionário por ID")
    public ResponseEntity<QuestionarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar questionário")
    public ResponseEntity<QuestionarioResponse> create(@RequestBody @Valid QuestionarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar questionário")
    public ResponseEntity<QuestionarioResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid QuestionarioRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir questionário")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

