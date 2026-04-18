package com.neritech.saas.integracao.controller;

import com.neritech.saas.integracao.dto.TokenApiRequest;
import com.neritech.saas.integracao.dto.TokenApiResponse;
import com.neritech.saas.integracao.service.TokenApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/integracao/tokens")
@RequiredArgsConstructor
@Tag(name = "Tokens API", description = "Gerenciamento de tokens de API")
public class TokenApiController {

    private final TokenApiService service;

    @GetMapping
    @Operation(summary = "Listar todos os tokens de API")
    public ResponseEntity<List<TokenApiResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar token de API por ID")
    public ResponseEntity<TokenApiResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo token de API")
    public ResponseEntity<TokenApiResponse> create(@Valid @RequestBody TokenApiRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar token de API")
    public ResponseEntity<TokenApiResponse> update(@PathVariable Long id, @Valid @RequestBody TokenApiRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar token de API")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
