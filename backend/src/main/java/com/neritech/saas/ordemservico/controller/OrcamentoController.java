package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.OrcamentoRequest;
import com.neritech.saas.ordemservico.dto.OrcamentoResponse;
import com.neritech.saas.ordemservico.service.OrcamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/orcamentos")
@Tag(name = "OrÃ§amentos", description = "Gerenciamento de orÃ§amentos de ordens de serviÃ§o")
public class OrcamentoController {

    private final OrcamentoService service;

    public OrcamentoController(OrcamentoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar novo orÃ§amento")
    public ResponseEntity<OrcamentoResponse> create(@Valid @RequestBody OrcamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar orÃ§amento por ID")
    public ResponseEntity<OrcamentoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/ordem-servico/{ordemServicoId}")
    @Operation(summary = "Listar orÃ§amentos por ordem de serviÃ§o")
    public ResponseEntity<List<OrcamentoResponse>> findByOrdemServicoId(@PathVariable Long ordemServicoId) {
        return ResponseEntity.ok(service.findByOrdemServicoId(ordemServicoId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar orÃ§amento")
    public ResponseEntity<OrcamentoResponse> update(@PathVariable Long id,
            @Valid @RequestBody OrcamentoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar orÃ§amento")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
