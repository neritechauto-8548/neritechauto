package com.neritech.saas.agendamento.controller;

import com.neritech.saas.agendamento.dto.ReagendamentoRequest;
import com.neritech.saas.agendamento.dto.ReagendamentoResponse;
import com.neritech.saas.agendamento.service.ReagendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/agendamentos/reagendamentos")
@RequiredArgsConstructor
public class ReagendamentoController {

    private final ReagendamentoService service;

    @PostMapping
    public ResponseEntity<ReagendamentoResponse> criar(@Valid @RequestBody ReagendamentoRequest request) {
        ReagendamentoResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReagendamentoResponse> buscarPorId(@PathVariable Long id) {
        ReagendamentoResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/agendamento/{agendamentoOriginalId}")
    public ResponseEntity<List<ReagendamentoResponse>> listarPorAgendamentoOriginal(
            @PathVariable Long agendamentoOriginalId) {
        List<ReagendamentoResponse> responses = service.listarPorAgendamentoOriginal(agendamentoOriginalId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
