package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.service.HorarioTrabalhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/rh/horarios-trabalho")
@RequiredArgsConstructor
@Tag(name = "HorÃ¡rios de Trabalho", description = "GestÃ£o de horÃ¡rios de trabalho")
public class HorarioTrabalhoController {
    private final HorarioTrabalhoService service;

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar horÃ¡rios de trabalho da empresa")
    public ResponseEntity<List<HorarioTrabalhoResponse>> findByEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(service.findByEmpresa(empresaId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar horÃ¡rio de trabalho por ID")
    public ResponseEntity<HorarioTrabalhoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar horÃ¡rio de trabalho")
    public ResponseEntity<HorarioTrabalhoResponse> create(@Valid @RequestBody HorarioTrabalhoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar horÃ¡rio de trabalho")
    public ResponseEntity<HorarioTrabalhoResponse> update(@PathVariable Long id,
            @Valid @RequestBody HorarioTrabalhoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir horÃ¡rio de trabalho")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
