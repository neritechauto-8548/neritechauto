package com.neritech.saas.comunicacao.controller;

import com.neritech.saas.comunicacao.dto.ConfiguracaoEmailRequest;
import com.neritech.saas.comunicacao.dto.ConfiguracaoEmailResponse;
import com.neritech.saas.comunicacao.service.ConfiguracaoEmailService;
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
@RequestMapping("/v1/comunicacao/config/email")
@RequiredArgsConstructor
@Tag(name = "ConfiguraÃ§Ãµes Email", description = "Gerenciamento de configuraÃ§Ãµes de provedores de Email")
public class ConfiguracaoEmailController {

    private final ConfiguracaoEmailService service;

    @GetMapping
    @Operation(summary = "Listar configuraÃ§Ãµes de Email", description = "Retorna uma lista paginada de configuraÃ§Ãµes de Email")
    public ResponseEntity<Page<ConfiguracaoEmailResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar configuraÃ§Ã£o de Email por ID", description = "Retorna os detalhes de uma configuraÃ§Ã£o de Email especÃ­fica")
    public ResponseEntity<ConfiguracaoEmailResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar configuraÃ§Ã£o de Email", description = "Cria uma nova configuraÃ§Ã£o de provedor de Email")
    public ResponseEntity<ConfiguracaoEmailResponse> create(@RequestBody @Valid ConfiguracaoEmailRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar configuraÃ§Ã£o de Email", description = "Atualiza os dados de uma configuraÃ§Ã£o de Email existente")
    public ResponseEntity<ConfiguracaoEmailResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ConfiguracaoEmailRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir configuraÃ§Ã£o de Email", description = "Remove uma configuraÃ§Ã£o de Email do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
