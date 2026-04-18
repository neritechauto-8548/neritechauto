package com.neritech.saas.produtoServico.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.neritech.saas.produtoServico.dto.PromocaoRequest;
import com.neritech.saas.produtoServico.dto.PromocaoResponse;
import com.neritech.saas.produtoServico.service.PromocaoService;

@RestController
@RequestMapping("/v1/promocoes")
@Tag(name = "PromoÃ§Ãµes", description = "Gerenciamento de promoÃ§Ãµes e cupons")
public class PromocaoController {

    private final PromocaoService service;

    public PromocaoController(PromocaoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar nova promoÃ§Ã£o")
    public ResponseEntity<PromocaoResponse> create(@Valid @RequestBody PromocaoRequest request) {
        PromocaoResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar promoÃ§Ã£o por ID")
    public ResponseEntity<PromocaoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar promoÃ§Ãµes por empresa (paginado)")
    public ResponseEntity<Page<PromocaoResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar promoÃ§Ã£o")
    public ResponseEntity<PromocaoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PromocaoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir promoÃ§Ã£o")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
