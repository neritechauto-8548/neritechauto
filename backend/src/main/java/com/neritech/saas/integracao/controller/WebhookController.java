package com.neritech.saas.integracao.controller;

import com.neritech.saas.integracao.dto.WebhookRequest;
import com.neritech.saas.integracao.dto.WebhookResponse;
import com.neritech.saas.integracao.service.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/integracao/webhooks")
@RequiredArgsConstructor
@Tag(name = "Webhooks", description = "Gerenciamento de webhooks")
public class WebhookController {

    private final WebhookService service;

    @GetMapping
    @Operation(summary = "Listar todos os webhooks")
    public ResponseEntity<List<WebhookResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar webhook por ID")
    public ResponseEntity<WebhookResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo webhook")
    public ResponseEntity<WebhookResponse> create(@Valid @RequestBody WebhookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar webhook")
    public ResponseEntity<WebhookResponse> update(@PathVariable Long id, @Valid @RequestBody WebhookRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar webhook")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
