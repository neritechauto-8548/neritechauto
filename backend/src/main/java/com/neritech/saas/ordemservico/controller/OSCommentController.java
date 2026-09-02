package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.OSCommentCreateRequest;
import com.neritech.saas.ordemservico.dto.OSCommentResponse;
import com.neritech.saas.ordemservico.service.OSCommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/ordens-servico/{ordemServicoId}/comments")
public class OSCommentController {

    private final OSCommentService service;

    public OSCommentController(OSCommentService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('OS_COMENTARIOS','OS_COMENTARIOS_OUTROS')")
    public ResponseEntity<List<OSCommentResponse>> list(@PathVariable Long ordemServicoId) {
        return ResponseEntity.ok(service.list(ordemServicoId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_COMENTARIOS')")
    public ResponseEntity<OSCommentResponse> create(
            @PathVariable Long ordemServicoId,
            @RequestBody @Valid OSCommentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(ordemServicoId, request));
    }
}
