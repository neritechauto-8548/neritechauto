package com.neritech.saas.veiculo.controller;

import com.neritech.saas.veiculo.dto.ModeloVeiculoRequest;
import com.neritech.saas.veiculo.dto.ModeloVeiculoResponse;
import com.neritech.saas.veiculo.service.ModeloVeiculoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/modelos-veiculos")
public class ModeloVeiculoController {

    private final ModeloVeiculoService service;

    public ModeloVeiculoController(ModeloVeiculoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ModeloVeiculoResponse> create(@RequestBody @Valid ModeloVeiculoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModeloVeiculoResponse> update(@PathVariable Long id,
            @RequestBody @Valid ModeloVeiculoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModeloVeiculoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ModeloVeiculoResponse>> findAll(@RequestParam(required = false) Long marcaId) {
        if (marcaId != null) {
            return ResponseEntity.ok(service.findByMarca(marcaId));
        }
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
