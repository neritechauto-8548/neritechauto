package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.FotoOSResponse;
import com.neritech.saas.ordemservico.service.FotoOSService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_ENV_FOTOS')")
    public ResponseEntity<FotoOSResponse> upload(
            @PathVariable Long osId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestHeader(value = HttpHeaders.HOST, required = false) String host) {
        String base = "/api";
        FotoOSResponse res = service.upload(osId, file, descricao, base);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/{osId}/fotos")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public ResponseEntity<List<FotoOSResponse>> list(@PathVariable Long osId) {
        return ResponseEntity.ok(service.list(osId));
    }

    @GetMapping("/fotos/{id}/download")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        Resource resource = service.download(id);
        MediaType mediaType = MediaType.parseMediaType(service.getContentType(id));
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }

    @DeleteMapping("/fotos/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_ENV_FOTOS')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
