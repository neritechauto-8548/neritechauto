package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.StatusOSRequest;
import com.neritech.saas.ordemservico.dto.StatusOSResponse;
import com.neritech.saas.ordemservico.service.StatusOSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/status-os")
@Tag(name = "Status OS", description = "Gerenciamento de status de ordens de serviÃƒÂ§o")
public class StatusOSController {

    private final StatusOSService service;

    public StatusOSController(StatusOSService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar novo status")
    public ResponseEntity<StatusOSResponse> create(@Valid @RequestBody StatusOSRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar status por ID")
    public ResponseEntity<StatusOSResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar status por empresa")
    public ResponseEntity<Page<StatusOSResponse>> findByEmpresaId(@PathVariable Long empresaId, Pageable pageable) {
        return ResponseEntity.ok(service.findByEmpresaId(empresaId, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar status")
    public ResponseEntity<StatusOSResponse> update(@PathVariable Long id, @Valid @RequestBody StatusOSRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar status")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
