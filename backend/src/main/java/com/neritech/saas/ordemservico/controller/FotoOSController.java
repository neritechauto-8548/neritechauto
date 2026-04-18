package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.FotoOSResponse;
import com.neritech.saas.ordemservico.service.FotoOSService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/ordens-servico")
public class FotoOSController {

    private final FotoOSService service;

    public FotoOSController(FotoOSService service) {
        this.service = service;
    }

    @PostMapping("/{osId}/fotos")
    public ResponseEntity<FotoOSResponse> upload(@PathVariable Long osId,
                                                 @RequestParam("file") MultipartFile file,
                                                 @RequestParam(value = "descricao", required = false) String descricao,
                                                 @RequestHeader(value = HttpHeaders.HOST, required = false) String host) {
        String base = "";
        base = "/api";
        FotoOSResponse res = service.upload(osId, file, descricao, base);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/{osId}/fotos")
    public ResponseEntity<List<FotoOSResponse>> list(@PathVariable Long osId) {
        return ResponseEntity.ok(service.list(osId));
    }

    @GetMapping("/fotos/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        Resource r = service.download(id);
        MediaType mt = MediaType.parseMediaType(service.getContentType(id));
        return ResponseEntity.ok()
                .contentType(mt)
                .body(r);
    }

    @DeleteMapping("/fotos/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
