package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.WorkSessionPauseRequest;
import com.neritech.saas.ordemservico.dto.WorkSessionResponse;
import com.neritech.saas.ordemservico.service.WorkSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/v1/work-sessions", "/api/v1/work-sessions"})
@Tag(name = "Work Sessions", description = "Comandos de sessão de trabalho da execução da OS")
public class WorkSessionController {

    private final WorkSessionService workSessionService;

    public WorkSessionController(WorkSessionService workSessionService) {
        this.workSessionService = workSessionService;
    }

    @PostMapping("/{sessionId}/pause")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    @Operation(summary = "Pausar sessão ativa com motivo controlado")
    public ResponseEntity<WorkSessionResponse> pause(
            @PathVariable Long sessionId,
            @Valid @RequestBody WorkSessionPauseRequest request,
            @RequestHeader("If-Match") String ifMatch,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return ResponseEntity.ok(workSessionService.pause(sessionId, request, ifMatch, idempotencyKey));
    }

    @PostMapping("/{sessionId}/resume")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    @Operation(summary = "Retomar sessão pausada revalidando o serviço")
    public ResponseEntity<WorkSessionResponse> resume(
            @PathVariable Long sessionId,
            @RequestHeader("If-Match") String ifMatch,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return ResponseEntity.ok(workSessionService.resume(sessionId, ifMatch, idempotencyKey));
    }

    @PostMapping("/{sessionId}/finish")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    @Operation(summary = "Finalizar o relógio sem concluir automaticamente o serviço ou a OS")
    public ResponseEntity<WorkSessionResponse> finish(
            @PathVariable Long sessionId,
            @RequestHeader("If-Match") String ifMatch,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return ResponseEntity.ok(workSessionService.finish(sessionId, ifMatch, idempotencyKey));
    }
}
