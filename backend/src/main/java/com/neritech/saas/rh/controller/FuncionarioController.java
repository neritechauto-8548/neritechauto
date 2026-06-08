package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.FuncionarioRequest;
import com.neritech.saas.rh.dto.FuncionarioResponse;
import com.neritech.saas.rh.service.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.neritech.saas.rh.service.FuncionarioFotoStorageService;

@RestController
@RequestMapping("/v1/rh/funcionarios")
@RequiredArgsConstructor
@Tag(name = "Funcionários", description = "Gestão de funcionários")
public class FuncionarioController {

    private final FuncionarioService service;
    private final FuncionarioFotoStorageService fotoStorageService;

    @GetMapping
    @Operation(summary = "Listar funcionÃ¡rios")
    public ResponseEntity<Page<FuncionarioResponse>> findAll(
            @RequestParam Long empresaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar funcionÃ¡rio por ID")
    public ResponseEntity<FuncionarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar funcionário por ID de usuário")
    public ResponseEntity<FuncionarioResponse> findByUsuarioId(
            @PathVariable Long usuarioId,
            @RequestParam Long empresaId) {
        return ResponseEntity.ok(service.findByUsuarioId(empresaId, usuarioId));
    }

    @PostMapping
    @Operation(summary = "Criar funcionÃ¡rio")
    public ResponseEntity<FuncionarioResponse> create(@Valid @RequestBody FuncionarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar funcionÃ¡rio")
    public ResponseEntity<FuncionarioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody FuncionarioRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir funcionário")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/foto")
    @Operation(summary = "Upload da foto", description = "Realiza o upload da foto do funcionário")
    public ResponseEntity<FuncionarioResponse> uploadFoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        String path = fotoStorageService.store(id, file);
        FuncionarioResponse saved = service.updateFotoPath(id, path);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}/foto")
    @Operation(summary = "Remover foto do funcionário", description = "Remove a imagem atual do funcionário")
    public ResponseEntity<Void> removerFoto(@PathVariable Long id) {
        service.updateFotoPath(id, null);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/foto")
    @Operation(summary = "Obter foto do funcionário", description = "Retorna a imagem do funcionário")
    public ResponseEntity<org.springframework.core.io.Resource> getFoto(@PathVariable Long id) {
        FuncionarioResponse f = service.findById(id);
        if (f.fotoFuncionarioUrl() == null || f.fotoFuncionarioUrl().isBlank()) {
            return ResponseEntity.notFound().build();
        }
        org.springframework.core.io.Resource r = fotoStorageService.load(f.fotoFuncionarioUrl());
        String contentType = "image/png";
        if (f.fotoFuncionarioUrl().toLowerCase().endsWith(".jpg") || f.fotoFuncionarioUrl().toLowerCase().endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (f.fotoFuncionarioUrl().toLowerCase().endsWith(".gif")) {
            contentType = "image/gif";
        } else if (f.fotoFuncionarioUrl().toLowerCase().endsWith(".svg")) {
            contentType = "image/svg+xml";
        }
        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .body(r);
    }
}
