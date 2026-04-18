package com.neritech.saas.produtoServico.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.neritech.saas.produtoServico.dto.ServicoRequest;
import com.neritech.saas.produtoServico.dto.ServicoResponse;
import com.neritech.saas.produtoServico.service.ServicoService;

@RestController
@RequestMapping("/v1/servicos")
@Tag(name = "ServiÃ§os", description = "Gerenciamento de serviÃ§os")
public class ServicoController {

    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar novo serviÃ§o")
    public ResponseEntity<ServicoResponse> create(@Valid @RequestBody ServicoRequest request) {
        ServicoResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar serviÃ§o por ID")
    public ResponseEntity<ServicoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar serviÃ§os por empresa (paginado)")
    public ResponseEntity<Page<ServicoResponse>> findAll(
            @RequestParam Long empresaId,
            @RequestParam(required = false) String search,
            Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(service.search(empresaId, search, pageable));
        }
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar serviÃ§o")
    public ResponseEntity<ServicoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ServicoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir serviÃ§o")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
