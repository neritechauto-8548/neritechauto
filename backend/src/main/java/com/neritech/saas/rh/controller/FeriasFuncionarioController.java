package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.service.FeriasFuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/rh/ferias")
@RequiredArgsConstructor
@Tag(name = "FÃ©rias", description = "GestÃ£o de fÃ©rias de funcionÃ¡rios")
public class FeriasFuncionarioController {
    private final FeriasFuncionarioService service;

    @GetMapping("/funcionario/{funcionarioId}")
    @Operation(summary = "Listar fÃ©rias do funcionÃ¡rio")
    public ResponseEntity<List<FeriasFuncionarioResponse>> findByFuncionario(@PathVariable Long funcionarioId) {
        return ResponseEntity.ok(service.findByFuncionario(funcionarioId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar fÃ©rias por ID")
    public ResponseEntity<FeriasFuncionarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar fÃ©rias")
    public ResponseEntity<FeriasFuncionarioResponse> create(@Valid @RequestBody FeriasFuncionarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar fÃ©rias")
    public ResponseEntity<FeriasFuncionarioResponse> update(@PathVariable Long id,
            @Valid @RequestBody FeriasFuncionarioRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir fÃ©rias")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
