package com.neritech.saas.veiculo.controller;

import com.neritech.saas.veiculo.dto.TipoCombustivelRequest;
import com.neritech.saas.veiculo.dto.TipoCombustivelResponse;
import com.neritech.saas.veiculo.service.TipoCombustivelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tipos-combustivel")
public class TipoCombustivelController {

    private final TipoCombustivelService service;

    public TipoCombustivelController(TipoCombustivelService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TipoCombustivelResponse> create(@RequestBody @Valid TipoCombustivelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoCombustivelResponse> update(@PathVariable Long id,
            @RequestBody @Valid TipoCombustivelRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCombustivelResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TipoCombustivelResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
