package com.neritech.saas.agendamento.controller;

import com.neritech.saas.agendamento.dto.LembreteAgendamentoRequest;
import com.neritech.saas.agendamento.dto.LembreteAgendamentoResponse;
import com.neritech.saas.agendamento.service.LembreteAgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/agendamentos/lembretes")
@RequiredArgsConstructor
public class LembreteAgendamentoController {

    private final LembreteAgendamentoService service;

    @PostMapping
    public ResponseEntity<LembreteAgendamentoResponse> criar(@Valid @RequestBody LembreteAgendamentoRequest request) {
        LembreteAgendamentoResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LembreteAgendamentoResponse> buscarPorId(@PathVariable Long id) {
        LembreteAgendamentoResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/agendamento/{agendamentoId}")
    public ResponseEntity<List<LembreteAgendamentoResponse>> listarPorAgendamento(@PathVariable Long agendamentoId) {
        List<LembreteAgendamentoResponse> responses = service.listarPorAgendamento(agendamentoId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
