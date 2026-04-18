package com.neritech.saas.integracao.controller;

import com.neritech.saas.integracao.dto.LogIntegracaoRequest;
import com.neritech.saas.integracao.dto.LogIntegracaoResponse;
import com.neritech.saas.integracao.service.LogIntegracaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/integracao/logs")
@RequiredArgsConstructor
@Tag(name = "Logs de IntegraÃ§Ã£o", description = "Gerenciamento de logs de integraÃ§Ãµes")
public class LogIntegracaoController {

    private final LogIntegracaoService service;

    @GetMapping
    @Operation(summary = "Listar todos os logs de integraÃ§Ã£o")
    public ResponseEntity<List<LogIntegracaoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar log de integraÃ§Ã£o por ID")
    public ResponseEntity<LogIntegracaoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo log de integraÃ§Ã£o")
    public ResponseEntity<LogIntegracaoResponse> create(@Valid @RequestBody LogIntegracaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar log de integraÃ§Ã£o")
    public ResponseEntity<LogIntegracaoResponse> update(@PathVariable Long id,
            @Valid @RequestBody LogIntegracaoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar log de integraÃ§Ã£o")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
