package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.dto.SituacaoRequest;
import com.neritech.saas.empresa.dto.SituacaoResponse;
import com.neritech.saas.empresa.service.SituacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/situacoes-empresa")
@Tag(name = "Situações da Empresa", description = "Endpoints para gestão de situações por empresa")
public class SituacaoController {

    private final SituacaoService service;

    public SituacaoController(SituacaoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar situação", description = "Cria uma nova situação vinculada a uma empresa")
    public ResponseEntity<SituacaoResponse> create(@Valid @RequestBody SituacaoRequest request) {
        SituacaoResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar situação", description = "Busca uma situação pelo ID")
    public ResponseEntity<SituacaoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar situações", description = "Lista situações com paginação")
    public ResponseEntity<Page<SituacaoResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar situações por empresa", description = "Lista situações de uma empresa específica")
    public ResponseEntity<List<SituacaoResponse>> findByEmpresaId(@PathVariable Long empresaId) {
        return ResponseEntity.ok(service.findByEmpresaId(empresaId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar situação", description = "Atualiza os dados de uma situação")
    public ResponseEntity<SituacaoResponse> update(@PathVariable Long id, @Valid @RequestBody SituacaoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir situação", description = "Remove uma situação pelo ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

