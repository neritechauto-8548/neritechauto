package com.neritech.saas.cliente.controller;

import com.neritech.saas.cliente.dto.DocumentoClienteResponse;
import com.neritech.saas.cliente.service.DocumentoClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/v1/clientes/{clienteId}/documentos")
public class DocumentoClienteController {
    private final DocumentoClienteService service;

    public DocumentoClienteController(DocumentoClienteService service) { this.service = service; }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public Page<DocumentoClienteResponse> list(@PathVariable Long clienteId, Pageable pageable) {
        return service.list(clienteId, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public DocumentoClienteResponse get(@PathVariable Long clienteId, @PathVariable Long id) {
        return service.get(clienteId, id);
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CLIENTE_EDITAR')")
    public ResponseEntity<DocumentoClienteResponse> upload(
            @PathVariable Long clienteId,
            @RequestPart("file") MultipartFile file,
            @RequestParam("tipoDocumento") String tipoDocumento,
            @RequestParam(value = "descricao", required = false) String descricao) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.upload(clienteId, file, tipoDocumento, descricao));
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public ResponseEntity<byte[]> download(@PathVariable Long clienteId, @PathVariable Long id) {
        var doc = service.requireDocumento(clienteId, id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(doc.getArquivoNome(), StandardCharsets.UTF_8).build());
        headers.setContentType(MediaType.parseMediaType(doc.getArquivoTipo()));
        headers.setContentLength(doc.getArquivoTamanho());
        headers.set("X-Content-Type-Options", "nosniff");
        return new ResponseEntity<>(doc.getArquivoConteudo(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CLIENTE_EDITAR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long clienteId, @PathVariable Long id) {
        service.delete(clienteId, id);
    }
}
