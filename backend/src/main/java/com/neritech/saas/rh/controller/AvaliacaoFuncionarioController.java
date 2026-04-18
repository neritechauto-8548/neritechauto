package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.service.AvaliacaoFuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/rh/avaliacoes")
@RequiredArgsConstructor
@Tag(name = "AvaliaÃ§Ãµes", description = "GestÃ£o de avaliaÃ§Ãµes de funcionÃ¡rios")
public class AvaliacaoFuncionarioController {
    private final AvaliacaoFuncionarioService service;

    @GetMapping("/funcionario/{funcionarioId}")
    @Operation(summary = "Listar avaliaÃ§Ãµes do funcionÃ¡rio")
    public ResponseEntity<List<AvaliacaoFuncionarioResponse>> findByFuncionario(@PathVariable Long funcionarioId) {
        return ResponseEntity.ok(service.findByFuncionario(funcionarioId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar avaliaÃ§Ã£o por ID")
    public ResponseEntity<AvaliacaoFuncionarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar avaliaÃ§Ã£o")
    public ResponseEntity<AvaliacaoFuncionarioResponse> create(
            @Valid @RequestBody AvaliacaoFuncionarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar avaliaÃ§Ã£o")
    public ResponseEntity<AvaliacaoFuncionarioResponse> update(@PathVariable Long id,
            @Valid @RequestBody AvaliacaoFuncionarioRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir avaliaÃ§Ã£o")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
