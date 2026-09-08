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

@RestController
@RequestMapping({"/v1/agendamentos","/api/agendamentos"})
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService service;

    @PostMapping
    @PreAuthorize("hasAuthority('GERAL_AGENDAMENTO_EDITAR')")
    public ResponseEntity<AgendamentoResponse> criar(@Valid @RequestBody AgendamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GERAL_AGENDAMENTO_VISUALIZAR')")
    public ResponseEntity<List<AgendamentoResponse>> listar() {
        return ResponseEntity.ok(service.listarAtual());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GERAL_AGENDAMENTO_VISUALIZAR')")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /** Compatibilidade: o empresaId do path é comparado ao TenantContext e nunca concede acesso. */
    @GetMapping("/empresa/{empresaId}")
    @PreAuthorize("hasAuthority('GERAL_AGENDAMENTO_VISUALIZAR')")
    public ResponseEntity<List<AgendamentoResponse>> listarPorEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(service.listarPorEmpresa(empresaId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('GERAL_AGENDAMENTO_EDITAR')")
    public ResponseEntity<AgendamentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AgendamentoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('GERAL_AGENDAMENTO_EDITAR')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
