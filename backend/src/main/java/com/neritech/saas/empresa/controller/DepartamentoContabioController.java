package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.dto.DepartamentoContabioRequest;
import com.neritech.saas.empresa.dto.DepartamentoContabioResponse;
import com.neritech.saas.empresa.service.DepartamentoContabioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/departamentos-contabio-empresa")
@Tag(name = "Departamentos Contábio", description = "Endpoints para gestão de departamentos contábios por empresa")
public class DepartamentoContabioController {

    private final DepartamentoContabioService service;

    public DepartamentoContabioController(DepartamentoContabioService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar departamento contábio", description = "Cria um novo departamento contábio vinculado a uma empresa")
    public ResponseEntity<DepartamentoContabioResponse> create(@Valid @RequestBody DepartamentoContabioRequest request) {
        DepartamentoContabioResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar departamento contábio", description = "Busca um departamento contábio pelo ID")
    public ResponseEntity<DepartamentoContabioResponse> findById(@PathVariable Long id) {
        DepartamentoContabioResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar departamentos contábios", description = "Lista departamentos contábios com paginação")
    public ResponseEntity<Page<DepartamentoContabioResponse>> findAll(Pageable pageable) {
        Page<DepartamentoContabioResponse> response = service.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar departamentos por empresa", description = "Lista departamentos contábios de uma empresa específica")
    public ResponseEntity<List<DepartamentoContabioResponse>> findByEmpresaId(@PathVariable Long empresaId) {
        List<DepartamentoContabioResponse> response = service.findByEmpresaId(empresaId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar departamento contábio", description = "Atualiza os dados de um departamento contábio")
    public ResponseEntity<DepartamentoContabioResponse> update(@PathVariable Long id,
                                                               @Valid @RequestBody DepartamentoContabioRequest request) {
        DepartamentoContabioResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir departamento contábio", description = "Remove um departamento contábio pelo ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

