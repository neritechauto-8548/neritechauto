package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.OrdemServicoExecutionResponse;
import com.neritech.saas.ordemservico.dto.WorkSessionResponse;
import com.neritech.saas.ordemservico.service.WorkSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Execução de Ordens de Serviço", description = "Sessões autoritativas e apontamentos da OS")
public class OrdemServicoExecutionController {

    private final WorkSessionService workSessionService;

    public OrdemServicoExecutionController(WorkSessionService workSessionService) {
        this.workSessionService = workSessionService;
    }

    @GetMapping("/{id}/execution")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Buscar read model de execução e apontamentos da Ordem de Serviço")
    public ResponseEntity<OrdemServicoExecutionResponse> findExecution(@PathVariable Long id) {
        return ResponseEntity.ok(workSessionService.findExecution(id));
    }

    @PostMapping("/{id}/services/{serviceId}/sessions/start")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    @Operation(summary = "Iniciar sessão de execução em um serviço autorizado")
    public ResponseEntity<WorkSessionResponse> start(
            @PathVariable Long id,
            @PathVariable Long serviceId,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workSessionService.start(id, serviceId, idempotencyKey));
    }
}
