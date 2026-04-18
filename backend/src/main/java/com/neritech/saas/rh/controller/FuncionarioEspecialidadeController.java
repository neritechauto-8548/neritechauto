package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.service.FuncionarioEspecialidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/rh/funcionarios-especialidades")
@RequiredArgsConstructor
@Tag(name = "FuncionÃ¡rios Especialidades", description = "GestÃ£o de especialidades dos funcionÃ¡rios")
public class FuncionarioEspecialidadeController {
    private final FuncionarioEspecialidadeService service;

    @GetMapping("/funcionario/{funcionarioId}")
    @Operation(summary = "Listar especialidades do funcionÃ¡rio")
    public ResponseEntity<List<FuncionarioEspecialidadeResponse>> findByFuncionario(@PathVariable Long funcionarioId) {
        return ResponseEntity.ok(service.findByFuncionario(funcionarioId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar especialidade do funcionÃ¡rio por ID")
    public ResponseEntity<FuncionarioEspecialidadeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar especialidade do funcionÃ¡rio")
    public ResponseEntity<FuncionarioEspecialidadeResponse> create(
            @Valid @RequestBody FuncionarioEspecialidadeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar especialidade do funcionÃ¡rio")
    public ResponseEntity<FuncionarioEspecialidadeResponse> update(@PathVariable Long id,
            @Valid @RequestBody FuncionarioEspecialidadeRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir especialidade do funcionÃ¡rio")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
