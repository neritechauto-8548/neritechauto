package com.neritech.saas.fiscal.controller;

import com.neritech.saas.fiscal.dto.RegimeTributarioRequest;
import com.neritech.saas.fiscal.dto.RegimeTributarioResponse;
import com.neritech.saas.fiscal.service.RegimeTributarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/fiscal/regimes-tributarios")
@RequiredArgsConstructor
@Tag(name = "Regimes TributÃ¡rios", description = "Gerenciamento de regimes tributÃ¡rios")
public class RegimeTributarioController {

    private final RegimeTributarioService service;

    @GetMapping
    @Operation(summary = "Listar todos os regimes tributÃ¡rios")
    public ResponseEntity<List<RegimeTributarioResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar regime tributÃ¡rio por ID")
    public ResponseEntity<RegimeTributarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo regime tributÃ¡rio")
    public ResponseEntity<RegimeTributarioResponse> create(@Valid @RequestBody RegimeTributarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar regime tributÃ¡rio")
    public ResponseEntity<RegimeTributarioResponse> update(@PathVariable Long id,
            @Valid @RequestBody RegimeTributarioRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar regime tributÃ¡rio")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
