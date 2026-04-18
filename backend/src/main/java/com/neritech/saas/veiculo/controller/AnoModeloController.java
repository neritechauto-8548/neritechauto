package com.neritech.saas.veiculo.controller;

import com.neritech.saas.veiculo.dto.AnoModeloRequest;
import com.neritech.saas.veiculo.dto.AnoModeloResponse;
import com.neritech.saas.veiculo.service.AnoModeloService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/anos-modelo")
public class AnoModeloController {

    private final AnoModeloService service;

    public AnoModeloController(AnoModeloService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AnoModeloResponse> create(@RequestBody @Valid AnoModeloRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnoModeloResponse> update(@PathVariable Long id,
            @RequestBody @Valid AnoModeloRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnoModeloResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AnoModeloResponse>> findAll(@RequestParam(required = false) Long modeloId) {
        if (modeloId != null) {
            return ResponseEntity.ok(service.findByModelo(modeloId));
        }
        // Se nÃƒÂ£o passar modeloId, retorna vazio ou todos? Retornar vazio por seguranÃƒÂ§a
        // ou implementar paginaÃƒÂ§ÃƒÂ£o depois.
        // Por enquanto, retorna vazio se nÃƒÂ£o tiver filtro para evitar load full
        return ResponseEntity.ok(List.of());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

