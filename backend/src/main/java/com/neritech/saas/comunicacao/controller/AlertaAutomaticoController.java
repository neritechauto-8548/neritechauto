package com.neritech.saas.comunicacao.controller;

import com.neritech.saas.comunicacao.dto.AlertaAutomaticoRequest;
import com.neritech.saas.comunicacao.dto.AlertaAutomaticoResponse;
import com.neritech.saas.comunicacao.service.AlertaAutomaticoService;
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
@RequestMapping("/v1/comunicacao/alertas")
@RequiredArgsConstructor
@Tag(name = "Alertas AutomÃ¡ticos", description = "Gerenciamento de regras de alertas automÃ¡ticos")
public class AlertaAutomaticoController {

    private final AlertaAutomaticoService service;

    @GetMapping
    @Operation(summary = "Listar alertas", description = "Retorna uma lista paginada de regras de alertas automÃ¡ticos")
    public ResponseEntity<Page<AlertaAutomaticoResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar alerta por ID", description = "Retorna os detalhes de uma regra de alerta especÃ­fica")
    public ResponseEntity<AlertaAutomaticoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar alerta", description = "Cria uma nova regra de alerta automÃ¡tico")
    public ResponseEntity<AlertaAutomaticoResponse> create(@RequestBody @Valid AlertaAutomaticoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar alerta", description = "Atualiza os dados de uma regra de alerta existente")
    public ResponseEntity<AlertaAutomaticoResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid AlertaAutomaticoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir alerta", description = "Remove uma regra de alerta do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
