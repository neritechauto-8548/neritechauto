package com.neritech.saas.integracao.controller;

import com.neritech.saas.integracao.dto.SincronizacaoRequest;
import com.neritech.saas.integracao.dto.SincronizacaoResponse;
import com.neritech.saas.integracao.service.SincronizacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/integracao/sincronizacoes")
@RequiredArgsConstructor
@Tag(name = "SincronizaÃ§Ãµes", description = "Gerenciamento de processos de sincronizaÃ§Ã£o")
public class SincronizacaoController {

    private final SincronizacaoService service;

    @GetMapping
    @Operation(summary = "Listar todas as sincronizaÃ§Ãµes")
    public ResponseEntity<List<SincronizacaoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sincronizaÃ§Ã£o por ID")
    public ResponseEntity<SincronizacaoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar nova sincronizaÃ§Ã£o")
    public ResponseEntity<SincronizacaoResponse> create(@Valid @RequestBody SincronizacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sincronizaÃ§Ã£o")
    public ResponseEntity<SincronizacaoResponse> update(@PathVariable Long id,
            @Valid @RequestBody SincronizacaoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar sincronizaÃ§Ã£o")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
