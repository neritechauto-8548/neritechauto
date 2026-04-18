package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.dto.SetorRequest;
import com.neritech.saas.empresa.dto.SetorResponse;
import com.neritech.saas.empresa.service.SetorService;
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
@RequestMapping("/v1/setores-empresa")
@Tag(name = "Setores da Empresa", description = "Endpoints para gestão de setores por empresa")
public class SetorController {

    private final SetorService service;

    public SetorController(SetorService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar setor", description = "Cria um novo setor vinculado a uma empresa")
    public ResponseEntity<SetorResponse> create(@Valid @RequestBody SetorRequest request) {
        SetorResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar setor", description = "Busca um setor pelo ID")
    public ResponseEntity<SetorResponse> findById(@PathVariable Long id) {
        SetorResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar setores", description = "Lista setores com paginação")
    public ResponseEntity<Page<SetorResponse>> findAll(Pageable pageable) {
        Page<SetorResponse> response = service.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar setores por empresa", description = "Lista setores de uma empresa específica")
    public ResponseEntity<List<SetorResponse>> findByEmpresaId(@PathVariable Long empresaId) {
        List<SetorResponse> response = service.findByEmpresaId(empresaId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar setor", description = "Atualiza os dados de um setor")
    public ResponseEntity<SetorResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody SetorRequest request) {
        SetorResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir setor", description = "Remove um setor pelo ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
