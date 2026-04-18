package com.neritech.saas.agendamento.controller;

import com.neritech.saas.agendamento.dto.RecursoAgendaRequest;
import com.neritech.saas.agendamento.dto.RecursoAgendaResponse;
import com.neritech.saas.agendamento.service.RecursoAgendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/agendamentos/recursos")
@RequiredArgsConstructor
public class RecursoAgendaController {

    private final RecursoAgendaService service;

    @PostMapping
    public ResponseEntity<RecursoAgendaResponse> criar(@Valid @RequestBody RecursoAgendaRequest request) {
        RecursoAgendaResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoAgendaResponse> buscarPorId(@PathVariable Long id) {
        RecursoAgendaResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<RecursoAgendaResponse>> listarPorEmpresa(@PathVariable Long empresaId) {
        List<RecursoAgendaResponse> responses = service.listarPorEmpresa(empresaId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
