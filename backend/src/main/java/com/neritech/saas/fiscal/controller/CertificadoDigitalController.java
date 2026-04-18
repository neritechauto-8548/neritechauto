package com.neritech.saas.fiscal.controller;

import com.neritech.saas.fiscal.dto.CertificadoDigitalRequest;
import com.neritech.saas.fiscal.dto.CertificadoDigitalResponse;
import com.neritech.saas.fiscal.service.CertificadoDigitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/fiscal/certificados-digitais")
@RequiredArgsConstructor
@Tag(name = "Certificados Digitais", description = "Gerenciamento de certificados digitais")
public class CertificadoDigitalController {

    private final CertificadoDigitalService service;

    @GetMapping
    @Operation(summary = "Listar todos os certificados digitais")
    public ResponseEntity<List<CertificadoDigitalResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar certificado digital por ID")
    public ResponseEntity<CertificadoDigitalResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo certificado digital")
    public ResponseEntity<CertificadoDigitalResponse> create(@Valid @RequestBody CertificadoDigitalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar certificado digital")
    public ResponseEntity<CertificadoDigitalResponse> update(@PathVariable Long id,
            @Valid @RequestBody CertificadoDigitalRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar certificado digital")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
