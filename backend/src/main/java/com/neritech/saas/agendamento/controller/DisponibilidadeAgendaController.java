package com.neritech.saas.agendamento.controller;

import com.neritech.saas.agendamento.dto.DisponibilidadeAgendaRequest;
import com.neritech.saas.agendamento.dto.DisponibilidadeAgendaResponse;
import com.neritech.saas.agendamento.service.DisponibilidadeAgendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/agendamentos/disponibilidade")
@RequiredArgsConstructor
public class DisponibilidadeAgendaController {

    private final DisponibilidadeAgendaService service;

    @PostMapping
    public ResponseEntity<DisponibilidadeAgendaResponse> criar(
            @Valid @RequestBody DisponibilidadeAgendaRequest request) {
        DisponibilidadeAgendaResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisponibilidadeAgendaResponse> buscarPorId(@PathVariable Long id) {
        DisponibilidadeAgendaResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<DisponibilidadeAgendaResponse>> listarPorFuncionario(@PathVariable Long funcionarioId) {
        List<DisponibilidadeAgendaResponse> responses = service.listarPorFuncionario(funcionarioId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
