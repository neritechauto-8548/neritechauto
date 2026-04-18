package com.neritech.saas.estoque.controller;

import com.neritech.saas.estoque.dto.LocalizacaoEstoqueRequest;
import com.neritech.saas.estoque.dto.LocalizacaoEstoqueResponse;
import com.neritech.saas.estoque.service.LocalizacaoEstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/localizacoes-estoque")
@Tag(name = "LocalizaÃ§Ãµes de Estoque", description = "Gerenciamento de localizaÃ§Ãµes de estoque")
public class LocalizacaoEstoqueController {

    private final LocalizacaoEstoqueService service;

    public LocalizacaoEstoqueController(LocalizacaoEstoqueService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar nova localizaÃ§Ã£o")
    public ResponseEntity<LocalizacaoEstoqueResponse> create(@Valid @RequestBody LocalizacaoEstoqueRequest request) {
        LocalizacaoEstoqueResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar localizaÃ§Ã£o por ID")
    public ResponseEntity<LocalizacaoEstoqueResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar localizaÃ§Ãµes por empresa (paginado)")
    public ResponseEntity<Page<LocalizacaoEstoqueResponse>> findByEmpresa(
            @PathVariable Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByEmpresa(empresaId, pageable));
    }

    @GetMapping("/empresa/{empresaId}/ativas")
    @Operation(summary = "Listar localizaÃ§Ãµes ativas por empresa (paginado)")
    public ResponseEntity<Page<LocalizacaoEstoqueResponse>> findByEmpresaAtivas(
            @PathVariable Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByEmpresaAtivas(empresaId, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar localizaÃ§Ã£o")
    public ResponseEntity<LocalizacaoEstoqueResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody LocalizacaoEstoqueRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir localizaÃ§Ã£o")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
