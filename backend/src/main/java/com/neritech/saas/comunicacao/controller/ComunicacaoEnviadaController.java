package com.neritech.saas.comunicacao.controller;

import com.neritech.saas.comunicacao.dto.ComunicacaoEnviadaRequest;
import com.neritech.saas.comunicacao.dto.ComunicacaoEnviadaResponse;
import com.neritech.saas.comunicacao.service.ComunicacaoEnviadaService;
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
@RequestMapping("/v1/comunicacao/envios")
@RequiredArgsConstructor
@Tag(name = "ComunicaÃ§Ãµes Enviadas", description = "Gerenciamento e histÃ³rico de comunicaÃ§Ãµes enviadas")
public class ComunicacaoEnviadaController {

    private final ComunicacaoEnviadaService service;

    @GetMapping
    @Operation(summary = "Listar comunicaÃ§Ãµes", description = "Retorna uma lista paginada de comunicaÃ§Ãµes enviadas")
    public ResponseEntity<Page<ComunicacaoEnviadaResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar comunicaÃ§Ã£o por ID", description = "Retorna os detalhes de uma comunicaÃ§Ã£o especÃ­fica")
    public ResponseEntity<ComunicacaoEnviadaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Enviar comunicaÃ§Ã£o", description = "Registra e envia uma nova comunicaÃ§Ã£o")
    public ResponseEntity<ComunicacaoEnviadaResponse> create(@RequestBody @Valid ComunicacaoEnviadaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar comunicaÃ§Ã£o", description = "Atualiza os dados de uma comunicaÃ§Ã£o (ex: status)")
    public ResponseEntity<ComunicacaoEnviadaResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ComunicacaoEnviadaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir comunicaÃ§Ã£o", description = "Remove um registro de comunicaÃ§Ã£o do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
