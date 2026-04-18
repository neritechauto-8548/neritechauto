package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.dto.EnderecoEmpresaRequest;
import com.neritech.saas.empresa.dto.EnderecoEmpresaResponse;
import com.neritech.saas.empresa.service.EnderecoEmpresaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/enderecos-empresa")
public class EnderecoEmpresaController {

    private final EnderecoEmpresaService service;

    public EnderecoEmpresaController(EnderecoEmpresaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EnderecoEmpresaResponse> create(@Valid @RequestBody EnderecoEmpresaRequest request) {
        EnderecoEmpresaResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoEmpresaResponse> findById(@PathVariable Long id) {
        EnderecoEmpresaResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<EnderecoEmpresaResponse>> findAll(Pageable pageable) {
        Page<EnderecoEmpresaResponse> response = service.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<EnderecoEmpresaResponse>> findByEmpresaId(@PathVariable Long empresaId) {
        List<EnderecoEmpresaResponse> response = service.findByEmpresaId(empresaId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoEmpresaResponse> update(@PathVariable Long id,
            @Valid @RequestBody EnderecoEmpresaRequest request) {
        EnderecoEmpresaResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
