package com.neritech.saas.comunicacao.controller;

import com.neritech.saas.comunicacao.dto.CampanhaMarketingRequest;
import com.neritech.saas.comunicacao.dto.CampanhaMarketingResponse;
import com.neritech.saas.comunicacao.service.CampanhaMarketingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comunicacao/campanhas")
@RequiredArgsConstructor
@Tag(name = "Campanhas de Marketing", description = "Gerenciamento de campanhas de marketing")
public class CampanhaMarketingController {

    private final CampanhaMarketingService service;

    @GetMapping
    @Operation(summary = "Listar campanhas", description = "Retorna uma lista paginada de campanhas de marketing")
    public ResponseEntity<Page<CampanhaMarketingResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar campanha por ID", description = "Retorna os detalhes de uma campanha especÃ­fica")
    public ResponseEntity<CampanhaMarketingResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar campanha", description = "Cria uma nova campanha de marketing")
    public ResponseEntity<CampanhaMarketingResponse> create(@RequestBody @Valid CampanhaMarketingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar campanha", description = "Atualiza os dados de uma campanha existente")
    public ResponseEntity<CampanhaMarketingResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid CampanhaMarketingRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir campanha", description = "Remove uma campanha do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
