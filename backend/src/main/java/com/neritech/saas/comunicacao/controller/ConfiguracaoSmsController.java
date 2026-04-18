package com.neritech.saas.comunicacao.controller;

import com.neritech.saas.comunicacao.dto.ConfiguracaoSmsRequest;
import com.neritech.saas.comunicacao.dto.ConfiguracaoSmsResponse;
import com.neritech.saas.comunicacao.service.ConfiguracaoSmsService;
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
@RequestMapping("/v1/comunicacao/config/sms")
@RequiredArgsConstructor
@Tag(name = "ConfiguraÃ§Ãµes SMS", description = "Gerenciamento de configuraÃ§Ãµes de provedores SMS")
public class ConfiguracaoSmsController {

    private final ConfiguracaoSmsService service;

    @GetMapping
    @Operation(summary = "Listar configuraÃ§Ãµes SMS", description = "Retorna uma lista paginada de configuraÃ§Ãµes SMS")
    public ResponseEntity<Page<ConfiguracaoSmsResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar configuraÃ§Ã£o SMS por ID", description = "Retorna os detalhes de uma configuraÃ§Ã£o SMS especÃ­fica")
    public ResponseEntity<ConfiguracaoSmsResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar configuraÃ§Ã£o SMS", description = "Cria uma nova configuraÃ§Ã£o de provedor SMS")
    public ResponseEntity<ConfiguracaoSmsResponse> create(@RequestBody @Valid ConfiguracaoSmsRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar configuraÃ§Ã£o SMS", description = "Atualiza os dados de uma configuraÃ§Ã£o SMS existente")
    public ResponseEntity<ConfiguracaoSmsResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ConfiguracaoSmsRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir configuraÃ§Ã£o SMS", description = "Remove uma configuraÃ§Ã£o SMS do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
