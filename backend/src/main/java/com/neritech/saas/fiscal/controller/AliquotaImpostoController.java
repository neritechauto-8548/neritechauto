package com.neritech.saas.fiscal.controller;

import com.neritech.saas.fiscal.dto.AliquotaImpostoRequest;
import com.neritech.saas.fiscal.dto.AliquotaImpostoResponse;
import com.neritech.saas.fiscal.service.AliquotaImpostoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/fiscal/aliquotas-impostos")
@RequiredArgsConstructor
@Tag(name = "AlÃ­quotas de Impostos", description = "Gerenciamento de alÃ­quotas de impostos")
public class AliquotaImpostoController {

    private final AliquotaImpostoService service;

    @GetMapping
    @Operation(summary = "Listar todas as alÃ­quotas de impostos")
    public ResponseEntity<List<AliquotaImpostoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar alÃ­quota de imposto por ID")
    public ResponseEntity<AliquotaImpostoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar nova alÃ­quota de imposto")
    public ResponseEntity<AliquotaImpostoResponse> create(@Valid @RequestBody AliquotaImpostoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar alÃ­quota de imposto")
    public ResponseEntity<AliquotaImpostoResponse> update(@PathVariable Long id,
            @Valid @RequestBody AliquotaImpostoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar alÃ­quota de imposto")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
