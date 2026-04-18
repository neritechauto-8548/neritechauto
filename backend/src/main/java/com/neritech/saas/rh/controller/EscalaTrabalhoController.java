package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.service.EscalaTrabalhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/rh/escalas-trabalho")
@RequiredArgsConstructor
@Tag(name = "Escalas de Trabalho", description = "GestÃ£o de escalas de trabalho")
public class EscalaTrabalhoController {
    private final EscalaTrabalhoService service;

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar escalas de trabalho da empresa")
    public ResponseEntity<List<EscalaTrabalhoResponse>> findByEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(service.findByEmpresa(empresaId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar escala de trabalho por ID")
    public ResponseEntity<EscalaTrabalhoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar escala de trabalho")
    public ResponseEntity<EscalaTrabalhoResponse> create(@Valid @RequestBody EscalaTrabalhoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar escala de trabalho")
    public ResponseEntity<EscalaTrabalhoResponse> update(@PathVariable Long id,
            @Valid @RequestBody EscalaTrabalhoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir escala de trabalho")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
