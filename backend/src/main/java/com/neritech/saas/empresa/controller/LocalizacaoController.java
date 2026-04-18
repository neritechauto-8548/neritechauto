package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.dto.LocalizacaoRequest;
import com.neritech.saas.empresa.dto.LocalizacaoResponse;
import com.neritech.saas.empresa.service.LocalizacaoService;
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
@RequestMapping("/v1/localizacoes-empresa")
@Tag(name = "Localizações da Empresa", description = "Endpoints para gestão de localizações por empresa")
public class LocalizacaoController {

    private final LocalizacaoService service;

    public LocalizacaoController(LocalizacaoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar localização", description = "Cria uma nova localização vinculada a uma empresa")
    public ResponseEntity<LocalizacaoResponse> create(@Valid @RequestBody LocalizacaoRequest request) {
        LocalizacaoResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar localização", description = "Busca uma localização pelo ID")
    public ResponseEntity<LocalizacaoResponse> findById(@PathVariable Long id) {
        LocalizacaoResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar localizações", description = "Lista localizações com paginação")
    public ResponseEntity<Page<LocalizacaoResponse>> findAll(Pageable pageable) {
        Page<LocalizacaoResponse> response = service.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar localizações por empresa", description = "Lista localizações de uma empresa específica")
    public ResponseEntity<List<LocalizacaoResponse>> findByEmpresaId(@PathVariable Long empresaId) {
        List<LocalizacaoResponse> response = service.findByEmpresaId(empresaId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar localização", description = "Atualiza os dados de uma localização")
    public ResponseEntity<LocalizacaoResponse> update(@PathVariable Long id,
                                                      @Valid @RequestBody LocalizacaoRequest request) {
        LocalizacaoResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir localização", description = "Remove uma localização pelo ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

