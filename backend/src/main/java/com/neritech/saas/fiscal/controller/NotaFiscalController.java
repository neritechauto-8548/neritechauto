package com.neritech.saas.fiscal.controller;

import com.neritech.saas.fiscal.dto.NotaFiscalResponse;
import com.neritech.saas.fiscal.service.NotaFiscalService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/ordens-servico")
public class NotaFiscalController {

    private final NotaFiscalService service;

    public NotaFiscalController(NotaFiscalService service) {
        this.service = service;
    }

    @PostMapping("/{osId}/nfe/emitir")
    public ResponseEntity<NotaFiscalResponse> emitir(@PathVariable Long osId) {
        return ResponseEntity.ok(service.emitir(osId));
    }

    @GetMapping(value = "/{osId}/nfe/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> pdf(@PathVariable Long osId) {
        Resource r = service.gerarPdf(osId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(r);
    }

    @GetMapping(value = "/nfe/{id}/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(service.gerarPdfByNotaId(id));
    }
}
