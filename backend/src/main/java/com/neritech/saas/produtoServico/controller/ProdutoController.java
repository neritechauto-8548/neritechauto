package com.neritech.saas.produtoServico.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.neritech.saas.produtoServico.service.ProdutoFotoStorageService;

import com.neritech.saas.produtoServico.dto.ProdutoRequest;
import com.neritech.saas.produtoServico.dto.ProdutoResponse;
import com.neritech.saas.produtoServico.service.ProdutoService;

@RestController
@RequestMapping("/v1/produtos")
@Tag(name = "Produtos", description = "Gerenciamento de produtos")
public class ProdutoController {

    private final ProdutoService service;
    private final ProdutoFotoStorageService fotoStorageService;

    public ProdutoController(ProdutoService service, ProdutoFotoStorageService fotoStorageService) {
        this.service = service;
        this.fotoStorageService = fotoStorageService;
    }

    @PostMapping
    @Operation(summary = "Criar novo produto")
    public ResponseEntity<ProdutoResponse> create(@Valid @RequestBody ProdutoRequest request) {
        ProdutoResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<ProdutoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar produtos por empresa (paginado)")
    public ResponseEntity<Page<ProdutoResponse>> findAll(
            @RequestParam Long empresaId,
            @RequestParam(required = false) String search,
            Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(service.search(empresaId, search, pageable));
        }
        return ResponseEntity.ok(service.findAll(empresaId, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto")
    public ResponseEntity<ProdutoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PostMapping("/{id}/duplicar")
    @Operation(summary = "Duplicar produto")
    public ResponseEntity<ProdutoResponse> duplicar(@PathVariable Long id) {
        ProdutoResponse response = service.duplicar(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir produto")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/foto")
    @Operation(summary = "Upload da foto", description = "Realiza o upload da foto do produto")
    public ResponseEntity<ProdutoResponse> uploadFoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        String path = fotoStorageService.store(id, file);
        ProdutoResponse saved = service.updateFotoPath(id, path);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}/foto")
    @Operation(summary = "Remover foto do produto", description = "Remove a imagem atual do produto")
    public ResponseEntity<Void> removerFoto(@PathVariable Long id) {
        service.updateFotoPath(id, null);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/foto")
    @Operation(summary = "Obter foto do produto", description = "Retorna a imagem do produto")
    public ResponseEntity<org.springframework.core.io.Resource> getFoto(@PathVariable Long id) {
        ProdutoResponse p = service.findById(id);
        if (p.fotoPrincipalUrl() == null || p.fotoPrincipalUrl().isBlank()) {
            return ResponseEntity.notFound().build();
        }
        org.springframework.core.io.Resource r = fotoStorageService.load(p.fotoPrincipalUrl());
        String contentType = "image/png";
        if (p.fotoPrincipalUrl().toLowerCase().endsWith(".jpg") || p.fotoPrincipalUrl().toLowerCase().endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (p.fotoPrincipalUrl().toLowerCase().endsWith(".gif")) {
            contentType = "image/gif";
        } else if (p.fotoPrincipalUrl().toLowerCase().endsWith(".svg")) {
            contentType = "image/svg+xml";
        } else if (p.fotoPrincipalUrl().toLowerCase().endsWith(".webp")) {
            contentType = "image/webp";
        }
        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .body(r);
    }
}
