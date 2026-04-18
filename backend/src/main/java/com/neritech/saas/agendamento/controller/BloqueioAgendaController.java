package com.neritech.saas.agendamento.controller;

import com.neritech.saas.agendamento.dto.BloqueioAgendaRequest;
import com.neritech.saas.agendamento.dto.BloqueioAgendaResponse;
import com.neritech.saas.agendamento.service.BloqueioAgendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/agendamentos/bloqueios")
@RequiredArgsConstructor
public class BloqueioAgendaController {

    private final BloqueioAgendaService service;

    @PostMapping
    public ResponseEntity<BloqueioAgendaResponse> criar(@Valid @RequestBody BloqueioAgendaRequest request) {
        BloqueioAgendaResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloqueioAgendaResponse> buscarPorId(@PathVariable Long id) {
        BloqueioAgendaResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<BloqueioAgendaResponse>> listarPorEmpresa(@PathVariable Long empresaId) {
        List<BloqueioAgendaResponse> responses = service.listarPorEmpresa(empresaId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
