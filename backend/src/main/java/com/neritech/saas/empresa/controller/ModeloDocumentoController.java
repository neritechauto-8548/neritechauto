package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.domain.enums.TipoDocumento;
import com.neritech.saas.empresa.dto.ModeloDocumentoRequest;
import com.neritech.saas.empresa.dto.ModeloDocumentoResponse;
import com.neritech.saas.empresa.service.ModeloDocumentoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/modelos-documentos")
public class ModeloDocumentoController {

    private final ModeloDocumentoService service;

    public ModeloDocumentoController(ModeloDocumentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ModeloDocumentoResponse> create(@Valid @RequestBody ModeloDocumentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModeloDocumentoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ModeloDocumentoResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<ModeloDocumentoResponse>> findByEmpresaId(@PathVariable Long empresaId) {
        return ResponseEntity.ok(service.findByEmpresaId(empresaId));
    }

    @GetMapping("/empresa/{empresaId}/tipo/{tipoDocumento}")
    public ResponseEntity<List<ModeloDocumentoResponse>> findByEmpresaIdAndTipoDocumento(
            @PathVariable Long empresaId,
            @PathVariable TipoDocumento tipoDocumento) {
        return ResponseEntity.ok(service.findByEmpresaIdAndTipoDocumento(empresaId, tipoDocumento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModeloDocumentoResponse> update(@PathVariable Long id,
            @Valid @RequestBody ModeloDocumentoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
