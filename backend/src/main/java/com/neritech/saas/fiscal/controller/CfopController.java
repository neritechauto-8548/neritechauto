package com.neritech.saas.fiscal.controller;

import com.neritech.saas.fiscal.dto.CfopRequest;
import com.neritech.saas.fiscal.dto.CfopResponse;
import com.neritech.saas.fiscal.service.CfopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/fiscal/cfop")
@RequiredArgsConstructor
@Tag(name = "CFOP", description = "Gerenciamento de CÃ³digos Fiscais de OperaÃ§Ãµes e PrestaÃ§Ãµes")
public class CfopController {

    private final CfopService service;

    @GetMapping
    @Operation(summary = "Listar todos os CFOPs")
    public ResponseEntity<List<CfopResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar CFOP por ID")
    public ResponseEntity<CfopResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo CFOP")
    public ResponseEntity<CfopResponse> create(@Valid @RequestBody CfopRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar CFOP")
    public ResponseEntity<CfopResponse> update(@PathVariable Long id, @Valid @RequestBody CfopRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar CFOP")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
