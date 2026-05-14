package com.neritech.saas.agendamento.controller;

import com.neritech.saas.agendamento.dto.AgendamentoRequest;
import com.neritech.saas.agendamento.dto.AgendamentoResponse;
import com.neritech.saas.agendamento.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Agendamento
 */
@RestController
@RequestMapping({"/v1/agendamentos","/api/agendamentos"})
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_AGENDAMENTO_VISUALIZAR')")
    public ResponseEntity<AgendamentoResponse> criar(@Valid @RequestBody AgendamentoRequest request) {
        AgendamentoResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_AGENDAMENTO_VISUALIZAR')")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        AgendamentoResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{empresaId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_AGENDAMENTO_VISUALIZAR')")
    public ResponseEntity<List<AgendamentoResponse>> listarPorEmpresa(@PathVariable Long empresaId) {
        List<AgendamentoResponse> responses = service.listarPorEmpresa(empresaId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_AGENDAMENTO_EDITAR')")
    public ResponseEntity<AgendamentoResponse> atualizar(@PathVariable Long id,
            @Valid @RequestBody AgendamentoRequest request) {
        AgendamentoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_AGENDAMENTO_EDITAR')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
