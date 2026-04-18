package com.neritech.saas.comunicacao.controller;

import com.neritech.saas.comunicacao.dto.TemplateComunicacaoRequest;
import com.neritech.saas.comunicacao.dto.TemplateComunicacaoResponse;
import com.neritech.saas.comunicacao.service.TemplateComunicacaoService;
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
@RequestMapping("/v1/comunicacao/templates")
@RequiredArgsConstructor
@Tag(name = "Templates de ComunicaÃ§Ã£o", description = "Gerenciamento de templates para emails, SMS, WhatsApp, etc.")
public class TemplateComunicacaoController {

    private final TemplateComunicacaoService service;

    @GetMapping
    @Operation(summary = "Listar templates", description = "Retorna uma lista paginada de templates de comunicaÃ§Ã£o")
    public ResponseEntity<Page<TemplateComunicacaoResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar template por ID", description = "Retorna os detalhes de um template especÃ­fico")
    public ResponseEntity<TemplateComunicacaoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar template", description = "Cria um novo template de comunicaÃ§Ã£o")
    public ResponseEntity<TemplateComunicacaoResponse> create(@RequestBody @Valid TemplateComunicacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar template", description = "Atualiza os dados de um template existente")
    public ResponseEntity<TemplateComunicacaoResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid TemplateComunicacaoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir template", description = "Remove um template do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
