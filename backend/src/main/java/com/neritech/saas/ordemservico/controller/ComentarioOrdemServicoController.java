package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.ComentarioOrdemServicoCriacaoRequest;
import com.neritech.saas.ordemservico.dto.ComentarioOrdemServicoResposta;
import com.neritech.saas.ordemservico.service.ComentarioOrdemServicoService;
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
@RequestMapping("/v1/ordens-servico/{ordemServicoId}/comentarios")
public class ComentarioOrdemServicoController {

    private final ComentarioOrdemServicoService servico;

    public ComentarioOrdemServicoController(ComentarioOrdemServicoService servico) {
        this.servico = servico;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('OS_COMENTARIOS','OS_COMENTARIOS_OUTROS')")
    public ResponseEntity<List<ComentarioOrdemServicoResposta>> listar(@PathVariable Long ordemServicoId) {
        return ResponseEntity.ok(servico.listar(ordemServicoId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_COMENTARIOS')")
    public ResponseEntity<ComentarioOrdemServicoResposta> criar(
            @PathVariable Long ordemServicoId,
            @RequestBody @Valid ComentarioOrdemServicoCriacaoRequest requisicao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servico.criar(ordemServicoId, requisicao));
    }
}
