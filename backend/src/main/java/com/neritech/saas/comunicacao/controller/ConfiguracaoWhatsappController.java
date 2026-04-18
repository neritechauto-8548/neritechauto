package com.neritech.saas.comunicacao.controller;

import com.neritech.saas.comunicacao.dto.ConfiguracaoWhatsappRequest;
import com.neritech.saas.comunicacao.dto.ConfiguracaoWhatsappResponse;
import com.neritech.saas.comunicacao.service.ConfiguracaoWhatsappService;
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
@RequestMapping("/v1/comunicacao/config/whatsapp")
@RequiredArgsConstructor
@Tag(name = "ConfiguraÃ§Ãµes WhatsApp", description = "Gerenciamento de configuraÃ§Ãµes de integraÃ§Ã£o com WhatsApp")
public class ConfiguracaoWhatsappController {

    private final ConfiguracaoWhatsappService service;

    @GetMapping
    @Operation(summary = "Listar configuraÃ§Ãµes WhatsApp", description = "Retorna uma lista paginada de configuraÃ§Ãµes WhatsApp")
    public ResponseEntity<Page<ConfiguracaoWhatsappResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar configuraÃ§Ã£o WhatsApp por ID", description = "Retorna os detalhes de uma configuraÃ§Ã£o WhatsApp especÃ­fica")
    public ResponseEntity<ConfiguracaoWhatsappResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar configuraÃ§Ã£o WhatsApp", description = "Cria uma nova configuraÃ§Ã£o de integraÃ§Ã£o com WhatsApp")
    public ResponseEntity<ConfiguracaoWhatsappResponse> create(
            @RequestBody @Valid ConfiguracaoWhatsappRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar configuraÃ§Ã£o WhatsApp", description = "Atualiza os dados de uma configuraÃ§Ã£o WhatsApp existente")
    public ResponseEntity<ConfiguracaoWhatsappResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ConfiguracaoWhatsappRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir configuraÃ§Ã£o WhatsApp", description = "Remove uma configuraÃ§Ã£o WhatsApp do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
