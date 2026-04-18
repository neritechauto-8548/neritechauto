package com.neritech.saas.integracao.controller;

import com.neritech.saas.integracao.dto.IntegracaoAtivaRequest;
import com.neritech.saas.integracao.dto.IntegracaoAtivaResponse;
import com.neritech.saas.integracao.service.IntegracaoAtivaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/integracao/integracoes")
@RequiredArgsConstructor
@Tag(name = "IntegraÃ§Ãµes Ativas", description = "Gerenciamento de integraÃ§Ãµes com sistemas externos")
public class IntegracaoAtivaController {

    private final IntegracaoAtivaService service;

    @GetMapping
    @Operation(summary = "Listar todas as integraÃ§Ãµes")
    public ResponseEntity<List<IntegracaoAtivaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar integraÃ§Ã£o por ID")
    public ResponseEntity<IntegracaoAtivaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar nova integraÃ§Ã£o")
    public ResponseEntity<IntegracaoAtivaResponse> create(@Valid @RequestBody IntegracaoAtivaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar integraÃ§Ã£o")
    public ResponseEntity<IntegracaoAtivaResponse> update(@PathVariable Long id,
            @Valid @RequestBody IntegracaoAtivaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar integraÃ§Ã£o")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
