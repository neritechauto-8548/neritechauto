package com.neritech.saas.integracao.controller;

import com.neritech.saas.integracao.dto.MapeamentoDadosRequest;
import com.neritech.saas.integracao.dto.MapeamentoDadosResponse;
import com.neritech.saas.integracao.service.MapeamentoDadosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/integracao/mapeamentos")
@RequiredArgsConstructor
@Tag(name = "Mapeamentos de Dados", description = "Gerenciamento de mapeamentos de campos entre sistemas")
public class MapeamentoDadosController {

    private final MapeamentoDadosService service;

    @GetMapping
    @Operation(summary = "Listar todos os mapeamentos de dados")
    public ResponseEntity<List<MapeamentoDadosResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar mapeamento de dados por ID")
    public ResponseEntity<MapeamentoDadosResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo mapeamento de dados")
    public ResponseEntity<MapeamentoDadosResponse> create(@Valid @RequestBody MapeamentoDadosRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar mapeamento de dados")
    public ResponseEntity<MapeamentoDadosResponse> update(@PathVariable Long id,
            @Valid @RequestBody MapeamentoDadosRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar mapeamento de dados")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
