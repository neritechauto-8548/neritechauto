package com.neritech.saas.comunicacao.controller;

import com.neritech.saas.comunicacao.dto.ItQuestionarioRequest;
import com.neritech.saas.comunicacao.dto.ItQuestionarioResponse;
import com.neritech.saas.comunicacao.service.ItQuestionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("comunicacaoItQuestionarioController")
@RequestMapping("/v1/comunicacao/it-questionario")
@RequiredArgsConstructor
@Tag(name = "Itens de Checklist", description = "Gerenciamento de itens de checklist em questionários")
public class ItQuestionarioController {

    private final ItQuestionarioService service;

    @GetMapping
    @Operation(summary = "Listar itens de checklist", description = "Retorna lista paginada de itens")
    public ResponseEntity<Page<ItQuestionarioResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item por ID")
    public ResponseEntity<ItQuestionarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/questionario/{questionarioId}")
    @Operation(summary = "Listar itens por questionário")
    public ResponseEntity<List<ItQuestionarioResponse>> findByQuestionarioId(@PathVariable Long questionarioId) {
        return ResponseEntity.ok(service.findByQuestionarioId(questionarioId));
    }

    @PostMapping
    @Operation(summary = "Criar item de checklist")
    public ResponseEntity<ItQuestionarioResponse> create(@RequestBody @Valid ItQuestionarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item de checklist")
    public ResponseEntity<ItQuestionarioResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ItQuestionarioRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir item de checklist")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
