package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.OSClosureModels;
import com.neritech.saas.ordemservico.service.OSClosureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/v1/ordens-servico", "/api/v1/ordens-servico"})
@Tag(name = "Fechamento de Ordens de Serviço", description = "Revisão e conclusão operacional versionada da OS")
public class OSClosureController {

    private final OSClosureService closureService;

    public OSClosureController(OSClosureService closureService) {
        this.closureService = closureService;
    }

    @GetMapping("/{id}/closure-review")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Buscar guardas autoritativas da revisão de fechamento")
    public ResponseEntity<OSClosureModels.Review> review(@PathVariable Long id) {
        return ResponseEntity.ok(closureService.review(id));
    }

    @PostMapping("/{id}/closure-review/validate")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Recalcular as guardas autoritativas antes do fechamento")
    public ResponseEntity<OSClosureModels.Review> validate(@PathVariable Long id) {
        return ResponseEntity.ok(closureService.validate(id));
    }

    @PostMapping("/{id}/complete-operationally")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    @Operation(summary = "Concluir operacionalmente a OS sem gerar efeitos financeiros ou fiscais")
    public ResponseEntity<OSClosureModels.CommandResult> completeOperationally(
            @PathVariable Long id,
            @RequestHeader("If-Match") String ifMatch,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return ResponseEntity.ok(closureService.completeOperationally(id, ifMatch, idempotencyKey));
    }
}
