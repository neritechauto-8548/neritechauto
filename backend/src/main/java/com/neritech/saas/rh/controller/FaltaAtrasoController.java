package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.service.FaltaAtrasoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/rh/faltas-atrasos")
@RequiredArgsConstructor
@Tag(name = "Faltas e Atrasos", description = "GestÃ£o de faltas e atrasos")
public class FaltaAtrasoController {
    private final FaltaAtrasoService service;

    @GetMapping("/funcionario/{funcionarioId}")
    @Operation(summary = "Listar faltas/atrasos do funcionÃ¡rio")
    public ResponseEntity<List<FaltaAtrasoResponse>> findByFuncionario(@PathVariable Long funcionarioId) {
        return ResponseEntity.ok(service.findByFuncionario(funcionarioId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar falta/atraso por ID")
    public ResponseEntity<FaltaAtrasoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar falta/atraso")
    public ResponseEntity<FaltaAtrasoResponse> create(@Valid @RequestBody FaltaAtrasoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar falta/atraso")
    public ResponseEntity<FaltaAtrasoResponse> update(@PathVariable Long id,
            @Valid @RequestBody FaltaAtrasoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir falta/atraso")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
