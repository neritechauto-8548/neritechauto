package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.OSAdditionalModels;
import com.neritech.saas.ordemservico.service.OSAdditionalRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/v1", "/api/v1"})
public class OSAdditionalRequestController {

    private final OSAdditionalRequestService service;

    public OSAdditionalRequestController(OSAdditionalRequestService service) {
        this.service = service;
    }

    @GetMapping("/ordens-servico/{orderId}/additional-requests")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO') or hasAuthority('OS_VIS_SOLICITACOES')")
    public ResponseEntity<List<OSAdditionalModels.Response>> list(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.list(orderId));
    }

    @GetMapping("/additional-requests/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO') or hasAuthority('OS_VIS_SOLICITACOES')")
    public ResponseEntity<OSAdditionalModels.Response> find(@PathVariable Long id) {
        return ResponseEntity.ok(service.find(id));
    }

    @PostMapping("/ordens-servico/{orderId}/additional-requests")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    public ResponseEntity<OSAdditionalModels.Response> create(
            @PathVariable Long orderId,
            @RequestBody @Valid OSAdditionalModels.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(orderId, request));
    }

    @PatchMapping("/additional-requests/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    public ResponseEntity<OSAdditionalModels.Response> update(
            @PathVariable Long id,
            @RequestBody @Valid OSAdditionalModels.UpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PostMapping("/additional-requests/{id}/submit")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    public ResponseEntity<OSAdditionalModels.SubmitResponse> submit(
            @PathVariable Long id,
            @RequestBody @Valid OSAdditionalModels.SubmitRequest request) {
        return ResponseEntity.ok(service.submit(id, request));
    }

    @PostMapping("/additional-requests/{id}/revoke")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    public ResponseEntity<OSAdditionalModels.Response> revoke(@PathVariable Long id) {
        return ResponseEntity.ok(service.revoke(id));
    }
}
