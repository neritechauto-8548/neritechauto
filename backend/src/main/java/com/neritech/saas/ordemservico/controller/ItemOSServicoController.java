package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.ItemOSServicoRequest;
import com.neritech.saas.ordemservico.dto.ItemOSServicoResponse;
import com.neritech.saas.ordemservico.service.ItemOSServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/itens-os-servicos")
@Tag(name = "Itens OS ServiÃ§os", description = "Gerenciamento de itens de serviÃ§os em ordens de serviÃ§o")
public class ItemOSServicoController {

    private final ItemOSServicoService service;

    public ItemOSServicoController(ItemOSServicoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar novo item de serviÃ§o")
    public ResponseEntity<ItemOSServicoResponse> create(@Valid @RequestBody ItemOSServicoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item por ID")
    public ResponseEntity<ItemOSServicoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/ordem-servico/{ordemServicoId}")
    @Operation(summary = "Listar itens por ordem de serviÃ§o")
    public ResponseEntity<List<ItemOSServicoResponse>> findByOrdemServicoId(@PathVariable Long ordemServicoId) {
        return ResponseEntity.ok(service.findByOrdemServicoId(ordemServicoId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item de serviÃ§o")
    public ResponseEntity<ItemOSServicoResponse> update(@PathVariable Long id,
            @Valid @RequestBody ItemOSServicoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar item de serviÃ§o")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
