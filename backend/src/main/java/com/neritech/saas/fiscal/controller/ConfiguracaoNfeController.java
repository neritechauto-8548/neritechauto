package com.neritech.saas.fiscal.controller;

import com.neritech.saas.fiscal.dto.ConfiguracaoNfeRequest;
import com.neritech.saas.fiscal.dto.ConfiguracaoNfeResponse;
import com.neritech.saas.fiscal.service.ConfiguracaoNfeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/fiscal/configuracoes-nfe")
@RequiredArgsConstructor
@Tag(name = "ConfiguraÃ§Ãµes NF-e", description = "Gerenciamento de configuraÃ§Ãµes de Nota Fiscal EletrÃ´nica")
public class ConfiguracaoNfeController {

    private final ConfiguracaoNfeService service;

    @GetMapping
    @Operation(summary = "Listar todas as configuraÃ§Ãµes de NF-e")
    public ResponseEntity<List<ConfiguracaoNfeResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar configuraÃ§Ã£o de NF-e por ID")
    public ResponseEntity<ConfiguracaoNfeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar nova configuraÃ§Ã£o de NF-e")
    public ResponseEntity<ConfiguracaoNfeResponse> create(@Valid @RequestBody ConfiguracaoNfeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar configuraÃ§Ã£o de NF-e")
    public ResponseEntity<ConfiguracaoNfeResponse> update(@PathVariable Long id,
            @Valid @RequestBody ConfiguracaoNfeRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar configuraÃ§Ã£o de NF-e")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
